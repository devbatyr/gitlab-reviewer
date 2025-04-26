package kz.shakenov.gitlab.reviewer.service;

import kz.shakenov.gitlab.reviewer.client.OllamaClient;
import kz.shakenov.gitlab.reviewer.config.OllamaProperties;
import kz.shakenov.gitlab.reviewer.model.OllamaRequest;
import kz.shakenov.gitlab.reviewer.model.OllamaResponse;
import com.shakenov.pmdcore.model.PmdViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AiRecommendationServiceTest {

    private AiRecommendationService service;
    private OllamaClient ollamaClient;
    private OllamaProperties ollamaProperties;

    @BeforeEach
    void setUp() throws Exception {
        service = new AiRecommendationService();
        ollamaClient = mock(OllamaClient.class);
        ollamaProperties = mock(OllamaProperties.class);

        Field clientField = AiRecommendationService.class.getDeclaredField("ollamaClient");
        clientField.setAccessible(true);
        clientField.set(service, ollamaClient);

        Field propertiesField = AiRecommendationService.class.getDeclaredField("ollamaProperties");
        propertiesField.setAccessible(true);
        propertiesField.set(service, ollamaProperties);
    }

    @Test
    void testGetRecommendation_successfulResponse() {
        List<PmdViolation> violations = List.of(new PmdViolation("Rule", "Description", 10, "SomeClass"));
        String codeSnippet = "public class Test {}";

        when(ollamaProperties.model()).thenReturn("test-model");
        when(ollamaProperties.temperature()).thenReturn(0.1);
        when(ollamaProperties.promptPath()).thenReturn("nonexistent.txt");

        OllamaResponse fakeResponse = new OllamaResponse("AI Recommendation");
        when(ollamaClient.analyze(any(OllamaRequest.class))).thenReturn(fakeResponse);

        String result = service.getRecommendation(violations, codeSnippet);

        assertTrue(result.contains("AI Recommendation"));
        verify(ollamaClient).analyze(any(OllamaRequest.class));
    }

    @Test
    void testGetRecommendation_timeoutError() {
        List<PmdViolation> violations = List.of(new PmdViolation("Rule", "Description", 10, "SomeClass"));
        String codeSnippet = "public class Test {}";

        when(ollamaProperties.model()).thenReturn("test-model");
        when(ollamaProperties.temperature()).thenReturn(0.1);
        when(ollamaProperties.promptPath()).thenReturn("nonexistent.txt");

        when(ollamaClient.analyze(any(OllamaRequest.class))).thenThrow(new jakarta.ws.rs.ProcessingException(new java.net.SocketTimeoutException("timeout")));

        String result = service.getRecommendation(violations, codeSnippet);

        assertTrue(result.contains("⚠️ Could not retrieve AI recommendation due to timeout."));
        verify(ollamaClient).analyze(any(OllamaRequest.class));
    }
}
