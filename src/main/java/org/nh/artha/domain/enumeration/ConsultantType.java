package org.nh.artha.domain.enumeration;

/**
 * The ConsultantType enumeration.
 */
public enum ConsultantType {
    ADMITTING_CONSULTANT("ADMITTING_CONSULTANT","Admitting Consultant"), RENDERING_CONSULTANT("RENDERING_CONSULTANT","Rendering Consultant");

    private final String name;
    private final String display;

    ConsultantType(String name,String display){
        this.name=name;
        this.display=display;
    }

    public String getName() {
        return name;
    }

    public String getDisplay() {
        return display;
    }
}
