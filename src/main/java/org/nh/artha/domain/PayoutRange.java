package org.nh.artha.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

/**
 * A PayoutRange.
 */
@Entity
@Table(name = "payout_range")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PayoutRange implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_amount", precision = 21, scale = 2)
    private BigDecimal fromAmount;

    @Column(name = "to_amount", precision = 21, scale = 2)
    private BigDecimal toAmount;

    @Column(name = "percentage")
    private Float percentage;


    @ManyToOne
    @JsonIgnoreProperties("payoutRanges")
    @JsonIgnore
    @ReadOnlyProperty
    private DepartmentPayout departmentPayout;

    public DepartmentPayout getDepartmentPayout() {
        return departmentPayout;
    }
    public PayoutRange departmentPayout(DepartmentPayout departmentPayout) {
        this.departmentPayout = departmentPayout;
        return this;
    }

    public void setDepartmentPayout(DepartmentPayout departmentPayout) {
        this.departmentPayout = departmentPayout;
    }


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(BigDecimal fromAmount) {
        this.fromAmount = fromAmount;
    }

    public BigDecimal getToAmount() {
        return toAmount;
    }

    public void setToAmount(BigDecimal toAmount) {
        this.toAmount = toAmount;
    }

    public Float getPercentage() {
        return percentage;
    }

    public void setPercentage(Float percentage) {
        this.percentage = percentage;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PayoutRange)) {
            return false;
        }
        return id != null && id.equals(((PayoutRange) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PayoutRange{" +
            "id=" + getId() +
            ", fromAmount=" + getFromAmount() +
            ", toAmount=" + getToAmount() +
            ", percentage=" + getPercentage() +
            "}";
    }
}
