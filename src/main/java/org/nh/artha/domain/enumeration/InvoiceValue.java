package org.nh.artha.domain.enumeration;

public enum InvoiceValue {

    MINUS_ALL_MATERIALS("MINUS_ALL_MATERIALS","Minus All Materials",1),
    MINUS_IMPLANT("MINUS_IMPLANT","Minus Implant",2),
    MINUS_DRUGS("MINUS_DRUGS","Minus Drugs",3),
    MINUS_CONSUMABLES("MINUS_CONSUMABLES","Minus Consumables",4),
    MINUS_OTHERS("MINUS_OTHERS","Minus Others",5);

    InvoiceValue(String name, String display,int order){
        this.name=name;
        this.display=display;
        this.order=order;
    }

    private int order;
    private String display;
    private String name;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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
