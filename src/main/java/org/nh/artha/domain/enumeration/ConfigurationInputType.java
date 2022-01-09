package org.nh.artha.domain.enumeration;

/**
 * The ConfigurationInputType enumeration.
 */
public enum ConfigurationInputType {
    DropDown("DropDown","DropDown");

    private final String name;
    private final String display;

    ConfigurationInputType(String name,String display){
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
