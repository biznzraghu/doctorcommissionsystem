package org.nh.artha.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A ExceptionSponsor.
 */
@Entity
@Table(name = "exception_sponsor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_exceptionsponsor")
@Setting(settingPath = "/es/settings.json")
public class ExceptionSponsor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "applicable")
    private Boolean applicable;

    @Type(type = "jsonb")
    @Column(name = "sponsor_type")
    private List<SponsorType> sponsorType;

    @Type(type = "jsonb")
    @Column(name = "plans",columnDefinition = "jsonb")
    private List<Plan> plans;

    public List<Plan> getPlans() {
        return plans;
    }

    public void setPlans(List<Plan> plans) {
        this.plans = plans;
    }
/*@OneToMany(mappedBy = "exceptionSponsor")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Plan> plans = new HashSet<>();*/

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isApplicable() {
        return applicable;
    }

    public ExceptionSponsor applicable(Boolean applicable) {
        this.applicable = applicable;
        return this;
    }

    public void setApplicable(Boolean applicable) {
        this.applicable = applicable;
    }



  /*  public Set<Plan> getPlans() {
        return plans;
    }

    public ExceptionSponsor plans(Set<Plan> Plans) {
        this.plans = Plans;
        return this;
    }*/

   /* public ExceptionSponsor addPlan(Plan Plan) {
        this.plans.add(Plan);
        Plan.setExceptionSponsor(this);
        return this;
    }

    public ExceptionSponsor removePlan(Plan Plan) {
        this.plans.remove(Plan);
        Plan.setExceptionSponsor(null);
        return this;
    }*/

    /*public void setPlans(Set<Plan> Plans) {
        this.plans = Plans;
    }*/
    public List<SponsorType> getSponsorType() {
        return sponsorType;
    }

    public void setSponsorType(List<SponsorType> sponsorType) {
        this.sponsorType = sponsorType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExceptionSponsor)) {
            return false;
        }
        return id != null && id.equals(((ExceptionSponsor) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ExceptionSponsor{" +
            "id=" + getId() +
            ", applicable='" + isApplicable() + "'" +
            ", sponsorType='" + getSponsorType() + "'" +
            "}";
    }
}
