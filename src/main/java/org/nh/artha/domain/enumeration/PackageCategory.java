package org.nh.artha.domain.enumeration;

/**
 * The packageCategory enumeration.
 */
public enum PackageCategory {
    SURGICAL("SURGICAL","Surgical"),HEALTH("HEALTH","Health"),
    DIAGNOSTIC("DIAGNOSTIC","Diagnostic");

    private String display;
    private String name;

    PackageCategory(String name,String display){
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
