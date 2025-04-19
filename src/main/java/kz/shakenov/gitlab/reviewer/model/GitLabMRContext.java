package kz.shakenov.gitlab.reviewer.model;

/**
 * Represents the context of a GitLab Merge Request.
 * <p>
 * This model holds metadata necessary to identify and analyze a specific MR,
 * including the project ID, merge request IID, and the target branch or commit reference.
 */
public class GitLabMRContext {

    /** ID of the GitLab project (numeric) */
    private int projectId;

    /** Internal ID (IID) of the merge request within the project */
    private int mrIid;

    /** Target branch or commit reference (ref) for fetching file content */
    private String ref;

    /**
     * Default constructor for serialization/deserialization frameworks.
     */
    public GitLabMRContext() {}

    /**
     * Constructs a GitLabMRContext with all fields.
     *
     * @param projectId the project ID
     * @param mrIid     the merge request IID
     * @param ref       the target branch or commit reference
     */
    public GitLabMRContext(int projectId, int mrIid, String ref) {
        this.projectId = projectId;
        this.mrIid = mrIid;
        this.ref = ref;
    }

    /**
     * @return the GitLab project ID
     */
    public int getProjectId() {
        return projectId;
    }

    /**
     * @param projectId the project ID to set
     */
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    /**
     * @return the merge request IID
     */
    public int getMrIid() {
        return mrIid;
    }

    /**
     * @param mrIid the merge request IID to set
     */
    public void setMrIid(int mrIid) {
        this.mrIid = mrIid;
    }

    /**
     * @return the target branch or commit reference
     */
    public String getRef() {
        return ref;
    }

    /**
     * @param ref the target branch or commit reference to set
     */
    public void setRef(String ref) {
        this.ref = ref;
    }
}
