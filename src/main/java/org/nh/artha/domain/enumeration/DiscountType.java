package org.nh.artha.domain.enumeration;

/**
 * The DiscountType enumeration.
 */
public enum DiscountType {
    NET("NET","Net"),  GROSS("GROSS","Gross");

    private String display;
    private String name;

    DiscountType(String name,String display){
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
