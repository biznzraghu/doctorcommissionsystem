package org.nh.artha.domain.enumeration;

/**
 * The FeatureType enumeration.
 */
public enum FeatureType {
    MASTER("MASTER","Master"), TRANSACTION("TRANSACTION","Transaction"), REPORT("REPORT","Report"),
    CONFIGURATION("CONFIGURATION","Configuration"), DASHBOARD("DASHBOARD","Dashboard");

    private String display;
    private String name;

    FeatureType(String name,String display){
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
