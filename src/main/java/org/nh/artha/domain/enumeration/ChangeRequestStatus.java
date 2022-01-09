package org.nh.artha.domain.enumeration;

/**
 * The ChangeRequestStatus enumeration.
 */
public enum ChangeRequestStatus {
    DRAFT("DRAFT","Draft"), PENDING_APPROVAL("PENDING_APPROVAL","Pending Approval"),
    APPROVED("APPROVED","Approved"), REJECTED("REJECTED","Rejected");

    private final String name;
    private final String display;

    ChangeRequestStatus(String name,String display){
        this.name=name;
        this.display=display;
    }

    public String getName() {
        return name;
    }

    public String getDisplay() {
        return display;
    }
}
