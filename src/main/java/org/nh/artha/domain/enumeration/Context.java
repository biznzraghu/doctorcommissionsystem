package org.nh.artha.domain.enumeration;

/**
 * The Context enumeration.
 */
public enum Context {
    Service_Group("Service_Group","Service_Group"),
    Billing_Group("Billing_Group","Billing_Group"),
    Financial_Group("Financial_Group","Financial_Group");

    private String display;
    private String name;
    Context(String name,String display) {
        this.name=name;
        this.display = display;
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
