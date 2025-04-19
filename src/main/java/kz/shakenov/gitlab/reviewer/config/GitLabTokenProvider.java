package kz.shakenov.gitlab.reviewer.config;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Provides the GitLab private token from configuration.
 * <p>
 * The token is injected from the configuration property {@code gitlab.auth.token}.
 * It is used for authenticating REST API requests to GitLab using the {@code PRIVATE-TOKEN} header.
 */
@ApplicationScoped
public class GitLabTokenProvider {

    @ConfigProperty(name = "gitlab.auth.token")
    String token;

    /**
     * Returns the GitLab private token used for authentication.
     *
     * @return the token value from configuration
     */
    public String getToken() {
        return token;
    }
}
