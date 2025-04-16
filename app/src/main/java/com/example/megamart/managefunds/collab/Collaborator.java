package com.example.megamart.managefunds.collab;

public class Collaborator {

    private String collaboratorName;
    private String owner;
    private String purpose;

    // Constructor
    public Collaborator(String collaboratorName, String owner, String purpose) {
        this.collaboratorName = collaboratorName;
        this.owner = owner;
        this.purpose = purpose;
    }

    // Getter and Setter methods
    public String getCollaboratorName() {
        return collaboratorName;
    }

    public void setCollaboratorName(String collaboratorName) {
        this.collaboratorName = collaboratorName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
