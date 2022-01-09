package org.nh.artha.domain.enumeration;

/**
 * The PatientCategory enumeration.
 */
public enum PatientCategory {
    CASH("CASH","Cash"), CREDIT("CREDIT","Credit"), CASH_CREDIT("CASH_CREDIT","Cash Credit");

    private final String name;
    private final String display;

    PatientCategory(String name,String display){
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
