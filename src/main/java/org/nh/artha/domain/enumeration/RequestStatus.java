package org.nh.artha.domain.enumeration;

/**
 * The RequestStatus enumeration.
 */
public enum RequestStatus {
    DRAFT("DRAFT","Draft"), PENDING_APPROVAL("PENDING_APPROVAL","Pending Approval"),
    APPROVED("APPROVED","Approved"), REJECTED("REJECTED","Rejected");

    private String display;
    private String name;

    RequestStatus(String name,String display){
        this.name=name;
        this.display=display;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
