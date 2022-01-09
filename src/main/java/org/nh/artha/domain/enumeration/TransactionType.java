package org.nh.artha.domain.enumeration;

/**
 * The TransactionType enumeration.
 */
public enum TransactionType {
    EMPLOYEE("EMPLOYEE","Employee"), DEPARTMENT("DEPARTMENT","Department");

    private String display;
    private String name;

    TransactionType(String name,String display){
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
