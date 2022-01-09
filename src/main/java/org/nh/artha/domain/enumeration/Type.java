package org.nh.artha.domain.enumeration;

/**
 * The Type enumeration.
 */
public enum Type {
    ALL_SERVICES("ALL_SERVICES","All Services",1),
    SERVICE_TYPE("SERVICE_TYPE","Service Type",2),
    SERVICE_GROUP("SERVICE_GROUP","Service Group",3),
    SERVICE_NAME("SERVICE_NAME","Service Name",4),
    SERVICE_INSIDE_PACKAGE("SERVICE_INSIDE_PACKAGE","Service Inside Package",9),
    ALL_PACKAGES("ALL_PACKAGES","All Packages",5),
    PACKAGE_CATEGORY("PACKAGE_CATEGORY","Package Category",6),
    PACKAGE_GROUP("PACKAGE_GROUP","Package Group",7),
    PACKAGE_NAME("PACKAGE_NAME","Package Name",8),
    PACKAGE_MINUS_MATERIAL_COST("PACKAGE_MINUS_MATERIAL_COST","Package Minus Material Cost",10),
    ALL_ITEMS("ALL_ITEMS","All Items",11),
    ITEM_CATEGORY("ITEM_CATEGORY","Item Category",12) ,
    ITEM_GROUP("ITEM_GROUP","Item Group",13),
    ITEM_NAME("ITEM_NAME","Item Name",14),
    INVOICE("INVOICE","Invoice",15),
    Invoice_With_Surgery("Invoice_With_Surgery","Invoice with Surgery",16),
    Invoice_With_Anaesthesia("Invoice_With_Anaesthesia","Invoice with Anesthesia",17);


    private int order;
    private String display;
    private String name;

    Type(String name, String display,int order){
        this.name=name;
        this.display=display;
        this.order=order;
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
