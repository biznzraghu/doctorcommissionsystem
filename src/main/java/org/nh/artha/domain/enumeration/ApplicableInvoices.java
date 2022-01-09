package org.nh.artha.domain.enumeration;

/**
 * The ApplicableInvoices enumeration.
 */
public enum ApplicableInvoices {
    ALL_INVOICES("ALL_INVOICES","All Invoices"), INVOICES_WITH_SURGERY("INVOICES_WITH_SURGERY","Invoices With Surgery"),
    INVOICES_WITH_ANESTHESIA("INVOICES_WITH_ANESTHESIA","Invoices With Anesthesia");

    private final String name;
    private final String display;

    ApplicableInvoices(String name,String display){
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
