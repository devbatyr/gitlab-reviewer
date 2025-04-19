package kz.shakenov.gitlab.reviewer.service;

import kz.shakenov.gitlab.reviewer.client.GitLabApiClient;
import kz.shakenov.gitlab.reviewer.config.PmdConfigProperties;
import kz.shakenov.gitlab.reviewer.model.GitLabMRContext;
import com.shakenov.pmdcore.config.PmdConfig;
import com.shakenov.pmdcore.model.PmdResponse;
import com.shakenov.pmdcore.service.PmdService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * Service responsible for analyzing GitLab merge requests using PMD.
 * <p>
 * This service:
 * <ul>
 *     <li>Retrieves a list of changed files in the MR</li>
 *     <li>Filters for Java source files</li>
 *     <li>Fetches file content based on target ref</li>
 *     <li>Runs PMD analysis on the content</li>
 *     <li>Posts the formatted result back to the merge request as a comment</li>
 * </ul>
 */
@ApplicationScoped
public class GitLabService {

    private final PmdService pmdService = new PmdService();
    private static final Logger log = Logger.getLogger(GitLabService.class);

    @Inject
    DiffParserService diffParser;

    @Inject
    GitLabCommentPublisher commentPublisher;

    @Inject
    PmdConfigProperties pmdProperties;

    @Inject
    @RestClient
    GitLabApiClient apiClient;

    /**
     * Analyzes the specified GitLab merge request for Java file changes
     * and posts PMD results back to the merge request.
     *
     * @param context context containing project ID, MR IID, and ref
     */
    public void analyzeMergeRequest(GitLabMRContext context) {
        int projectId = context.getProjectId();
        int mrIid = context.getMrIid();
        String ref = context.getRef();

        Map<String, Object> changes = apiClient.getMergeRequestChanges(projectId, mrIid);

        List<String> paths = diffParser.extractModifiedPaths(changes).stream()
                .filter(path -> path.endsWith(".java"))
                .toList();

        if (paths.isEmpty()) {
            log.warnf("No modified Java files found in MR !%d", mrIid);
            return;
        }

        paths.forEach(path -> {
            try {
                Map<String, Object> contentMap = apiClient.getFileContent(projectId, path, ref);
                String content = decodeBase64((String) contentMap.get("content"));

                PmdConfig config = createPmdConfig(path, content);
                PmdResponse response = pmdService.runPmd(config);

                commentPublisher.publish(context, response.getFormattedOutput());
            } catch (Exception e) {
                log.errorf("Failed to analyze file %s: %s", path, e.getMessage());
            }
        });
    }

    /**
     * Builds a {@link PmdConfig} object with provided file data and configured paths.
     *
     * @param path file path
     * @param code file content
     * @return configured {@link PmdConfig} instance
     */
    private PmdConfig createPmdConfig(String path, String code) {
        return PmdConfig.builder()
                .fileName(path)
                .code(code)
                .pmdPath(pmdProperties.path())
                .rulesetPath(pmdProperties.ruleset())
                .suppressionsPath(pmdProperties.suppressions())
                .build();
    }

    /**
     * Decodes base64-encoded file content returned from GitLab.
     *
     * @param encoded base64 string
     * @return decoded plain text content
     */
    private String decodeBase64(String encoded) {
        return new String(Base64.getDecoder().decode(encoded));
    }
}
