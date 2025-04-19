package kz.shakenov.gitlab.reviewer.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import kz.shakenov.gitlab.reviewer.model.GitLabMRContext;
import kz.shakenov.gitlab.reviewer.service.GitLabService;
import org.jboss.logging.Logger;

/**
 * REST controller that handles incoming webhook requests from GitLab.
 * <p>
 * This endpoint expects a JSON payload containing merge request context
 * information and triggers static code analysis using PMD.
 */
@Path("/api/webhook")
public class WebhookResource {

    private static final Logger log = Logger.getLogger(WebhookResource.class);

    @Inject
    GitLabService gitLabService;

    /**
     * Endpoint for processing webhook events from GitLab.
     * <p>
     * This method is triggered when a POST request with MR context arrives,
     * and initiates code review analysis via {@link GitLabService}.
     *
     * @param context the merge request context extracted from webhook payload
     * @return 200 OK if processed successfully, 500 Internal Server Error otherwise
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response handleWebhook(GitLabMRContext context) {
        log.infof("Received webhook: projectId=%d, mrIid=%d, ref=%s",
                context.getProjectId(), context.getMrIid(), context.getRef());

        gitLabService.analyzeMergeRequest(context);
        return Response.ok().build();
    }
}
