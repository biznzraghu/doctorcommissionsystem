package org.nh.artha.domain.enumeration;

/**
 * The ApplicableOn enumeration.
 */
public enum ApplicableOn {
    NET("NET","Net"), GROSS("GROSS","Gross");

    private final String name;
    private final String display;

    ApplicableOn(String name,String display){
        this.name= name;
        this.display= display;
    }

    public String getName() {
        return name;
    }

    public String getDisplay() {
        return display;
    }
}
