package org.nh.artha.domain.enumeration;

/**
 * The Beneficiary enumeration.
 */
public enum Beneficiary {
    ORDERING("ORDERING","Ordering"), RENDERING("RENDERING","Rendering"),
    ADMITTING("ADMITTING","Admitting"), CONSULTANT("CONSULTANT","Consultant");

    private final String name;
    private final String display;

    Beneficiary(String name, String display) {
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
