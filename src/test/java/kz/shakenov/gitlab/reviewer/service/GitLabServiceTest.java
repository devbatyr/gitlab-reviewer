package kz.shakenov.gitlab.reviewer.service;

import com.shakenov.pmdcore.config.PmdConfig;
import com.shakenov.pmdcore.model.PmdResponse;
import com.shakenov.pmdcore.model.PmdViolation;
import com.shakenov.pmdcore.service.PmdService;
import kz.shakenov.gitlab.reviewer.client.GitLabApiClient;
import kz.shakenov.gitlab.reviewer.config.PmdConfigProperties;
import kz.shakenov.gitlab.reviewer.model.GitLabMRContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

class GitLabServiceTest {

    private GitLabService service;
    private GitLabApiClient apiClient;
    private DiffParserService diffParser;
    private GitLabCommentPublisher commentPublisher;
    private PmdConfigProperties pmdProps;
    private PmdService pmdService;
    private AiRecommendationService aiRecommendationService;

    @BeforeEach
    void setUp() throws Exception {
        service = spy(new GitLabService());
        apiClient = mock(GitLabApiClient.class);
        diffParser = mock(DiffParserService.class);
        commentPublisher = mock(GitLabCommentPublisher.class);
        pmdProps = mock(PmdConfigProperties.class);
        pmdService = mock(PmdService.class);
        aiRecommendationService = mock(AiRecommendationService.class);

        Field field = GitLabService.class.getDeclaredField("pmdService");
        field.setAccessible(true);
        field.set(service, pmdService);

        service.apiClient = apiClient;
        service.diffParser = diffParser;
        service.commentPublisher = commentPublisher;
        service.pmdProperties = pmdProps;
        service.aiRecommendationService = aiRecommendationService;
    }

    @Test
    void testAnalyzeMergeRequest_shouldPostPmdResults() {
        GitLabMRContext context = new GitLabMRContext(123, 456, "main");

        Map<String, Object> changesJson = Map.of("changes", List.of(Map.of("new_path", "src/Test.java")));
        when(apiClient.getMergeRequestChanges(123, 456)).thenReturn(changesJson);
        when(diffParser.extractModifiedPaths(changesJson)).thenReturn(List.of("src/Test.java"));

        String base64Code = Base64.getEncoder().encodeToString("public class Test {}".getBytes());
        when(apiClient.getFileContent(123, "src/Test.java", "main"))
                .thenReturn(Map.of("content", base64Code));

        when(pmdProps.path()).thenReturn("/fake/pmd");
        when(pmdProps.ruleset()).thenReturn("/fake/ruleset.xml");
        when(pmdProps.suppressions()).thenReturn("/fake/suppress.xml");

        var mockResponse = new PmdResponse("raw", "formatted output", List.of(new PmdViolation("Rule", "desc", 1, "class")));
        when(pmdService.runPmd(any(PmdConfig.class))).thenReturn(mockResponse);

        when(aiRecommendationService.getRecommendation(any(), any()))
                .thenReturn("AI suggestion text");

        service.analyzeMergeRequest(context);

        verify(commentPublisher).publish(eq(context), contains("AI suggestion text"));
    }
}
