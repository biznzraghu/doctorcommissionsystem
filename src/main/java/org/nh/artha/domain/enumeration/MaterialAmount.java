package org.nh.artha.domain.enumeration;

/**
 * The MaterialAmount enumeration.
 */
public enum MaterialAmount {
    COST("COST","Cost"), SALE("SALE","Sale"), MARGIN("MARGIN","Margin");

    private final String name;
    private final String display;

    MaterialAmount(String name,String display){
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
