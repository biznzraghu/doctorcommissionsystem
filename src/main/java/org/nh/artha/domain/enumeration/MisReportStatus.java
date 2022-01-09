package org.nh.artha.domain.enumeration;

/**
 * The MisReportStatus enumeration.
 */
public enum MisReportStatus {
    IN_PROGRESS("IN_PROGRESS","In Progress"), COMPLETED("COMPLETED","Completed"), PENDING("PENDING","Pending"), ON_HOLD("ON_HOLD","On Hold"), FAILED("FAILED","Failed");

    private final String name;
    private final String display;

    MisReportStatus(String name,String display){
        this.name = name;
        this.display = display;
    }

    public String getName() {
        return name;
    }

    public String getDisplay() {
        return display;
    }
}
