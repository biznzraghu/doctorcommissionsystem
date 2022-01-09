package org.nh.artha.domain.enumeration;

/**
 * The DocumentType enumeration.
 */
public enum DocumentType {
    VARIABLE_PAYOUT("VARIABLE_PAYOUT","Variable Payout"), DEPARTMENT_PAYOUT("DEPARTMENT_PAYOUT","Department Payout"),
    PAYOUT_ADJUSTMENT("PAYOUT_ADJUSTMENT","Payout Adjustment");

    private String display;
    private String name;

    DocumentType(String name,String display){
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
