package org.nh.artha.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

import org.nh.artha.domain.enumeration.PackageComponentCategory;
import org.springframework.data.elasticsearch.annotations.Setting;

/**
 * A PackageComponent.
 */
@Entity
@Table(name = "package_component")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_packagecomponent")
@Setting(settingPath = "/es/settings.json")
public class PackageComponent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "package_component_category")
    private PackageComponentCategory packageComponentCategory;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "auto_order")
    private Boolean autoOrder;

    @Column(name = "exclude")
    private Boolean exclude;

    @Column(name = "quantity_limit")
    private Integer quantityLimit;

    @Column(name = "amount_limit")
    private Float amountLimit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("packageComponents")
    @JsonIgnore
    private PackageMaster packageMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("packageComponents")
    @JsonIgnore
    private Group serviceGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("packageComponents")
    @JsonIgnore
    private ItemCategory itemCategory;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PackageComponentCategory getPackageComponentCategory() {
        return packageComponentCategory;
    }

    public PackageComponent packageComponentCategory(PackageComponentCategory packageComponentCategory) {
        this.packageComponentCategory = packageComponentCategory;
        return this;
    }

    public void setPackageComponentCategory(PackageComponentCategory packageComponentCategory) {
        this.packageComponentCategory = packageComponentCategory;
    }

    public Boolean isActive() {
        return active;
    }

    public PackageComponent active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean isAutoOrder() {
        return autoOrder;
    }

    public PackageComponent autoOrder(Boolean autoOrder) {
        this.autoOrder = autoOrder;
        return this;
    }

    public void setAutoOrder(Boolean autoOrder) {
        this.autoOrder = autoOrder;
    }

    public Boolean isExclude() {
        return exclude;
    }

    public PackageComponent exclude(Boolean exclude) {
        this.exclude = exclude;
        return this;
    }

    public void setExclude(Boolean exclude) {
        this.exclude = exclude;
    }

    public Integer getQuantityLimit() {
        return quantityLimit;
    }

    public PackageComponent quantityLimit(Integer quantityLimit) {
        this.quantityLimit = quantityLimit;
        return this;
    }

    public void setQuantityLimit(Integer quantityLimit) {
        this.quantityLimit = quantityLimit;
    }

    public Float getAmountLimit() {
        return amountLimit;
    }

    public PackageComponent amountLimit(Float amountLimit) {
        this.amountLimit = amountLimit;
        return this;
    }

    public void setAmountLimit(Float amountLimit) {
        this.amountLimit = amountLimit;
    }

    public PackageMaster getPackageMaster() {
        return packageMaster;
    }

    public PackageComponent packageMaster(PackageMaster PackageMaster) {
        this.packageMaster = PackageMaster;
        return this;
    }

    public void setPackageMaster(PackageMaster PackageMaster) {
        this.packageMaster = PackageMaster;
    }

    public Group getServiceGroup() {
        return serviceGroup;
    }

    public PackageComponent serviceGroup(Group group) {
        this.serviceGroup = group;
        return this;
    }

    public void setServiceGroup(Group group) {
        this.serviceGroup = group;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public PackageComponent itemCategory(ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
        return this;
    }

    public void setItemCategory(ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PackageComponent)) {
            return false;
        }
        return id != null && id.equals(((PackageComponent) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PackageComponent{" +
            "id=" + getId() +
            ", packageComponentCategory='" + getPackageComponentCategory() + "'" +
            ", active='" + isActive() + "'" +
            ", autoOrder='" + isAutoOrder() + "'" +
            ", exclude='" + isExclude() + "'" +
            ", quantityLimit=" + getQuantityLimit() +
            ", amountLimit=" + getAmountLimit() +
            "}";
    }
}
