package kz.shakenov.gitlab.reviewer.client;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.Map;

/**
 * REST client interface for interacting with the GitLab API.
 * <p>
 * This interface allows fetching merge request changes, retrieving file content, and commenting on MRs.
 * It is configured via {@code gitlab-api} settings in application configuration.
 */
@Path("/api/v4")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RegisterRestClient(configKey = "gitlab-api")
public interface GitLabApiClient {

    /**
     * Fetches the list of changes (modified files) in a merge request.
     *
     * @param projectId the GitLab project ID
     * @param mrIid     the internal ID of the merge request (not the global ID)
     * @return a map containing the list of file changes and related metadata
     */
    @GET
    @Path("/projects/{projectId}/merge_requests/{mrIid}/changes")
    Map<String, Object> getMergeRequestChanges(@PathParam("projectId") int projectId,
                                               @PathParam("mrIid") int mrIid);

    /**
     * Retrieves the content of a specific file from the repository at a given ref (branch or commit).
     *
     * @param projectId the GitLab project ID
     * @param filePath  the path to the file (must be URL-encoded)
     * @param ref       the branch name or commit SHA
     * @return a map containing the file content (base64-encoded) and metadata
     */
    @GET
    @Path("/projects/{projectId}/repository/files/{filePath}")
    Map<String, Object> getFileContent(@PathParam("projectId") int projectId,
                                       @PathParam("filePath") @Encoded String filePath,
                                       @QueryParam("ref") String ref);

    /**
     * Posts a comment to a specific merge request.
     *
     * @param projectId        the GitLab project ID
     * @param mergeRequestIid  the internal ID of the merge request
     * @param body             the comment body, typically a map with a "body" key
     */
    @POST
    @Path("/projects/{projectId}/merge_requests/{mergeRequestIid}/notes")
    @Consumes(MediaType.APPLICATION_JSON)
    void commentOnMergeRequest(@PathParam("projectId") int projectId,
                               @PathParam("mergeRequestIid") int mergeRequestIid,
                               Map<String, String> body);
}
