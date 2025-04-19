package kz.shakenov.gitlab.reviewer.client;

import jakarta.inject.Inject;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.ext.Provider;
import kz.shakenov.gitlab.reviewer.config.GitLabTokenProvider;

import java.io.IOException;

/**
 * JAX-RS client request filter that automatically injects the PRIVATE-TOKEN header
 * into all outgoing HTTP requests to the GitLab REST API.
 *
 * <p>
 * This filter retrieves the token from {@link GitLabTokenProvider}
 * and appends it as a PRIVATE-TOKEN header.
 * </p>
 *
 * Example:
 * <pre>
 * PRIVATE-TOKEN: TOKEN
 * </pre>
 *
 * This allows centralized and reusable handling of authentication
 * across all REST client requests.
 *
 * @see GitLabTokenProvider
 */
@Provider
public class GitLabAuthFilter implements ClientRequestFilter {

    @Inject
    GitLabTokenProvider tokenProvider;

    /**
     * Adds the PRIVATE-TOKEN header to each outgoing request for GitLab authentication.
     *
     * @param requestContext the context of the HTTP request
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        requestContext.getHeaders().add("PRIVATE-TOKEN", tokenProvider.getToken());
    }
}
