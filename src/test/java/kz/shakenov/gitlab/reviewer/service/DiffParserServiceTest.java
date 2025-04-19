package kz.shakenov.gitlab.reviewer.service;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DiffParserServiceTest {

    private final DiffParserService service = new DiffParserService();

    @Test
    void extractModifiedPaths_validChanges_returnsPaths() {
        Map<String, Object> changesJson = Map.of(
                "changes", List.of(
                        Map.of("old_path", "src/Old.java", "new_path", "src/New.java"),
                        Map.of("old_path", "src/Old2.java", "new_path", "src/New2.java")
                )
        );

        List<String> result = service.extractModifiedPaths(changesJson);

        assertEquals(2, result.size());
        assertTrue(result.contains("src/New.java"));
        assertTrue(result.contains("src/New2.java"));
    }

    @Test
    void extractModifiedPaths_withDuplicates_returnsDistinctPaths() {
        Map<String, Object> changesJson = Map.of(
                "changes", List.of(
                        Map.of("new_path", "src/Main.java"),
                        Map.of("new_path", "src/Main.java")
                )
        );

        List<String> result = service.extractModifiedPaths(changesJson);

        assertEquals(1, result.size());
        assertEquals("src/Main.java", result.get(0));
    }

    @Test
    void extractModifiedPaths_invalidChangesField_throwsException() {
        Map<String, Object> changesJson = Map.of(
                "changes", "not_a_list"
        );

        assertThrows(IllegalStateException.class, () -> service.extractModifiedPaths(changesJson));
    }

    @Test
    void extractModifiedPaths_missingChangesField_throwsException() {
        Map<String, Object> changesJson = Map.of();

        assertThrows(IllegalStateException.class, () -> service.extractModifiedPaths(changesJson));
    }
}
