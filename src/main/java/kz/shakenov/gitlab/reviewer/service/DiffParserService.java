package kz.shakenov.gitlab.reviewer.service;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for extracting modified file paths from GitLab merge request changes JSON.
 */
@ApplicationScoped
public class DiffParserService {

    /**
     * Extracts modified file paths from the JSON returned by GitLab MR changes API.
     *
     * @param mrChangesJson the deserialized JSON from GitLab API as Map
     * @return list of modified file paths (new paths)
     */
    public List<String> extractModifiedPaths(Map<String, Object> mrChangesJson) {
        Object changesObj = mrChangesJson.get("changes");
        if (!(changesObj instanceof List<?> changesList)) {
            throw new IllegalStateException("Invalid or missing 'changes' field in GitLab response");
        }

        return changesList.stream()
                .filter(Map.class::isInstance)
                .map(change -> ((Map<?, ?>) change).get("new_path"))
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .distinct()
                .collect(Collectors.toList());
    }
}
