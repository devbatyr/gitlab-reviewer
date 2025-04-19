package kz.shakenov.gitlab.reviewer.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import kz.shakenov.gitlab.reviewer.client.GitLabApiClient;
import kz.shakenov.gitlab.reviewer.model.GitLabMRContext;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.Map;

/**
 * Responsible for publishing comments to a GitLab merge request.
 * This service uses the GitLab REST API to post general comments.
 */
@ApplicationScoped
public class GitLabCommentPublisher {

    private static final Logger log = Logger.getLogger(GitLabCommentPublisher.class);

    @Inject
    @RestClient
    GitLabApiClient apiClient;

    /**
     * Publishes a general comment to the given merge request.
     *
     * @param context the merge request context (project ID, MR IID)
     * @param message the comment text to post
     */
    public void publish(GitLabMRContext context, String message) {
        try {
            apiClient.commentOnMergeRequest(
                    context.getProjectId(),
                    context.getMrIid(),
                    Map.of("body", message)
            );
        } catch (Exception e) {
            log.errorf("Failed to publish comment to MR !%d: %s", context.getMrIid(), e.getMessage());
        }
    }
}
