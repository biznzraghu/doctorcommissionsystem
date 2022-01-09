package org.nh.artha.domain.enumeration;

/**
 * The ExceptionType enumeration.
 */
public enum ExceptionType {
    SponsorType("SponsorType","Sponsor Type"), Plan("Plan","Plan"), Service("Service","Service"),ServiceGroup("ServiceGroup","Service Group"),ItemWithBrand("ItemWithBrand","Item With Brand"),ItemWithGeneric("ItemWithGeneric","Item With Generic"),Sponsor("Sponsor","Sponsor");

    private final String name;
    private final String display;

    ExceptionType(String name,String display){
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
