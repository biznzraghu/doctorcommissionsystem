package org.nh.artha.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

import org.nh.artha.domain.enumeration.FSNType;

import org.nh.artha.domain.enumeration.VEDCategory;
import org.springframework.data.elasticsearch.annotations.Setting;

/**
 * A Item.
 */
@Entity
@Table(name = "item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_item")
@Setting(settingPath = "/es/settings.json")
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private String category;


    @Column(name = "batch_tracked")
    private Boolean batchTracked;


    @Column(name = "expiry_date_required")
    private Boolean expiryDateRequired;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "remarks")
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "fsn_type")
    private FSNType fsnType;

    @Enumerated(EnumType.STRING)
    @Column(name = "ved_category")
    private VEDCategory vedCategory;


    @Column(name = "generic")
    private String generic;

   /* @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JsonIgnoreProperties("items")
    private ItemType type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JsonIgnoreProperties("items")
    private ItemGroup group;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JsonIgnoreProperties("items")
    private ItemCategory category;*/

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Item code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public Item name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Item description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isBatchTracked() {
        return batchTracked;
    }

    public Item batchTracked(Boolean batchTracked) {
        this.batchTracked = batchTracked;
        return this;
    }

    public void setBatchTracked(Boolean batchTracked) {
        this.batchTracked = batchTracked;
    }

    public Boolean isExpiryDateRequired() {
        return expiryDateRequired;
    }

    public Item expiryDateRequired(Boolean expiryDateRequired) {
        this.expiryDateRequired = expiryDateRequired;
        return this;
    }

    public void setExpiryDateRequired(Boolean expiryDateRequired) {
        this.expiryDateRequired = expiryDateRequired;
    }

    public Boolean isActive() {
        return active;
    }

    public Item active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getRemarks() {
        return remarks;
    }

    public Item remarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public FSNType getFsnType() {
        return fsnType;
    }

    public Item fsnType(FSNType fsnType) {
        this.fsnType = fsnType;
        return this;
    }

    public void setFsnType(FSNType fsnType) {
        this.fsnType = fsnType;
    }

    public VEDCategory getVedCategory() {
        return vedCategory;
    }

    public Item vedCategory(VEDCategory vedCategory) {
        this.vedCategory = vedCategory;
        return this;
    }

    public void setVedCategory(VEDCategory vedCategory) {
        this.vedCategory = vedCategory;
    }

    public String getGeneric() {
        return generic;
    }

    public void setGeneric(String generic) {
        this.generic = generic;
    }

    /* public ItemType getType() {
            return type;
        }

        public Item type(ItemType ItemType) {
            this.type = ItemType;
            return this;
        }

        public void setType(ItemType ItemType) {
            this.type = ItemType;
        }

        public ItemGroup getGroup() {
            return group;
        }

        public Item group(ItemGroup ItemGroup) {
            this.group = ItemGroup;
            return this;
        }

        public void setGroup(ItemGroup ItemGroup) {
            this.group = ItemGroup;
        }

        public ItemCategory getCategory() {
            return category;
        }

        public Item category(ItemCategory ItemCategory) {
            this.category = ItemCategory;
            return this;
        }

        public void setCategory(ItemCategory ItemCategory) {
            this.category = ItemCategory;
        }
        // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove
    */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        return id != null && id.equals(((Item) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Item{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", batchTracked='" + isBatchTracked() + "'" +
            ", expiryDateRequired='" + isExpiryDateRequired() + "'" +
            ", active='" + isActive() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", fsnType='" + getFsnType() + "'" +
            ", vedCategory='" + getVedCategory() + "'" +
            "}";
    }
}
