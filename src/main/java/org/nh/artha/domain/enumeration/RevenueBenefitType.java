package org.nh.artha.domain.enumeration;

/**
 * The RevenueBenefitType enumeration.
 */
public enum RevenueBenefitType {
    FIXED("FIXED","Fixed"), CONTRIBUTION_BASED("CONTRIBUTION_BASED","Contribution Based");

    private String display;
    private String name;

    RevenueBenefitType(String name,String display){
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
