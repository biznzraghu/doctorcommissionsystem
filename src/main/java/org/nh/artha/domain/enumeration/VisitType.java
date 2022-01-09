package org.nh.artha.domain.enumeration;

/**
 * The VisitType enumeration.
 */
public enum VisitType {
    OP("OP","OP"),  IP("IP","IP"),  DC("DC","Day Care"),  ER("ER","ER");

    private String display;
    private String name;

    VisitType(String name,String display){
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
