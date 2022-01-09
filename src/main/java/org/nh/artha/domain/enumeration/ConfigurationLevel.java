package org.nh.artha.domain.enumeration;

/**
 * The ConfigurationLevel enumeration.
 */
public enum ConfigurationLevel {
    Global("Global","Global"), Unit("Unit","Unit");

    private final String name;
    private final String display;

    ConfigurationLevel(String name,String display){
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
