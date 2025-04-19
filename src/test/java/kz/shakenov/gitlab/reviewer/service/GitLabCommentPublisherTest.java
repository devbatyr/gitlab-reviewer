package kz.shakenov.gitlab.reviewer.service;

import kz.shakenov.gitlab.reviewer.client.GitLabApiClient;
import kz.shakenov.gitlab.reviewer.model.GitLabMRContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.*;

class GitLabCommentPublisherTest {

    private GitLabApiClient apiClient;
    private GitLabCommentPublisher publisher;

    @BeforeEach
    void setUp() {
        apiClient = mock(GitLabApiClient.class);
        publisher = new GitLabCommentPublisher();
        publisher.apiClient = apiClient;
    }

    @Test
    void publish_shouldCallApiClientWithCorrectArguments() {
        GitLabMRContext context = new GitLabMRContext(123, 456, "main");
        String message = "Test comment";

        publisher.publish(context, message);

        verify(apiClient).commentOnMergeRequest(
                eq(123),
                eq(456),
                eq(Map.of("body", message))
        );
    }

    @Test
    void publish_shouldHandleExceptionsGracefully() {
        GitLabMRContext context = new GitLabMRContext(789, 987, "dev");

        doThrow(new RuntimeException("Failed")).when(apiClient).commentOnMergeRequest(anyInt(), anyInt(), any());

        // Should not throw
        publisher.publish(context, "Will fail but caught");
    }
}
