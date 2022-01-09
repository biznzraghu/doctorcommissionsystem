package org.nh.artha.domain.enumeration;

/**
 * The PaymentMode enumeration.
 */
public enum PaymentMode {
    PERCENTAGE("PERCENTAGE","Percentage"), AMOUNT("AMOUNT","Amount");

    private String display;
    private String name;

    PaymentMode(String name,String display){
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
