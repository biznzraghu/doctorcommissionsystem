package org.nh.artha.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;


public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, org.nh.artha.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, org.nh.artha.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, org.nh.artha.domain.User.class.getName());
            createCache(cm, org.nh.artha.domain.Authority.class.getName());
            createCache(cm, org.nh.artha.domain.User.class.getName() + ".authorities");
            createCache(cm, org.nh.artha.domain.Privilege.class.getName());
            createCache(cm, org.nh.artha.domain.Feature.class.getName());
            createCache(cm, org.nh.artha.domain.ValueSetCodeMapping.class.getName());
            createCache(cm, org.nh.artha.domain.ValueSetCode.class.getName());
            createCache(cm, org.nh.artha.domain.ValueSet.class.getName());
            createCache(cm, org.nh.artha.domain.ConfigurationCategory.class.getName());
            createCache(cm, org.nh.artha.domain.ConfigurationDefination.class.getName());
            createCache(cm, org.nh.artha.domain.Configurations.class.getName());
            createCache(cm, org.nh.artha.domain.UserMaster.class.getName());
            createCache(cm, org.nh.artha.domain.Department.class.getName());
            createCache(cm, org.nh.artha.domain.PayoutDetails.class.getName());
            createCache(cm, org.nh.artha.domain.PayoutDetails.class.getName() + ".ids");
            createCache(cm, org.nh.artha.domain.PayoutDetails.class.getName() + ".payout_departments");
            createCache(cm, org.nh.artha.domain.DepartmentRevenue.class.getName());
            createCache(cm, org.nh.artha.domain.Role.class.getName());
            createCache(cm, org.nh.artha.domain.Role.class.getName() + ".privileges");
            createCache(cm, org.nh.artha.domain.Role.class.getName() + ".roles");
            createCache(cm, org.nh.artha.domain.PayoutDetails.class.getName() + ".departmentalRevenues");
            createCache(cm, org.nh.artha.domain.UserOrganizationRoleMapping.class.getName());
            createCache(cm, org.nh.artha.domain.Organization.class.getName());
            createCache(cm, org.nh.artha.domain.Organization.class.getName() + ".addresses");
            createCache(cm, org.nh.artha.domain.Organization.class.getName() + ".telecoms");
            createCache(cm, org.nh.artha.domain.OrganizationType.class.getName());
            createCache(cm, org.nh.artha.domain.McrStaging.class.getName());
            createCache(cm, org.nh.artha.domain.DepartmentRevenueBenefit.class.getName());
            createCache(cm, org.nh.artha.domain.LengthOfStayBenefit.class.getName());
            createCache(cm, org.nh.artha.domain.LengthOfStayBenefit.class.getName() + ".stayBenefitServices");
            createCache(cm, org.nh.artha.domain.StayBenefitService.class.getName());
            createCache(cm, org.nh.artha.domain.ServiceItemBenefit.class.getName());
            createCache(cm, org.nh.artha.domain.VariablePayout.class.getName());
            createCache(cm, org.nh.artha.domain.VariablePayout.class.getName() + ".departmentRevenueBenefits");
            createCache(cm, org.nh.artha.domain.VariablePayout.class.getName() + ".lengthOfStayBenefits");
            createCache(cm, org.nh.artha.domain.VariablePayout.class.getName() + ".serviceItemBenefits");
            createCache(cm, org.nh.artha.domain.PayoutAdjustment.class.getName());
            createCache(cm, org.nh.artha.domain.PayoutAdjustment.class.getName() + ".payoutAdjustmentDetails");
            createCache(cm, org.nh.artha.domain.PayoutAdjustment.class.getName() + ".transactionApprovalDetails");
            createCache(cm, org.nh.artha.domain.PayoutAdjustmentDetails.class.getName());
            createCache(cm, org.nh.artha.domain.TransactionApprovalDetails.class.getName());
            createCache(cm, org.nh.artha.domain.DoctorPayout.class.getName());
            createCache(cm, org.nh.artha.domain.DoctorPayout.class.getName() + ".losBenefietIds");
            createCache(cm, org.nh.artha.domain.DoctorPayout.class.getName() + ".ids");
            createCache(cm, org.nh.artha.domain.DoctorPayout.class.getName() + ".departmentRevenueBenefitIds");
            createCache(cm, org.nh.artha.domain.DoctorPayoutDepartment.class.getName());
            createCache(cm, org.nh.artha.domain.DoctorPayoutLOS.class.getName());
            createCache(cm, org.nh.artha.domain.DoctorPayout.class.getName() + ".doctorPayoutInvoices");
            createCache(cm, org.nh.artha.domain.InvoiceDoctorPayout.class.getName());
            createCache(cm, org.nh.artha.domain.DepartmentPayout.class.getName());
            createCache(cm, org.nh.artha.domain.DepartmentPayout.class.getName() + ".payoutRanges");
            createCache(cm, org.nh.artha.domain.DepartmentPayout.class.getName() + ".applicableConsultants");
            createCache(cm, org.nh.artha.domain.DepartmentPayout.class.getName() + ".departmentConsumptionMaterialReductions");
            createCache(cm, org.nh.artha.domain.DepartmentPayout.class.getName() + ".hscConsumptionMaterialReductions");
            createCache(cm, org.nh.artha.domain.ApplicableConsultant.class.getName());
            createCache(cm, org.nh.artha.domain.PayoutRange.class.getName());
            createCache(cm, org.nh.artha.domain.DepartmentConsumptionMaterialReduction.class.getName());
            createCache(cm, org.nh.artha.domain.HscConsumptionMaterialReduction.class.getName());
            createCache(cm, org.nh.artha.domain.ServiceItemBenefitTemplate.class.getName());
            createCache(cm, org.nh.artha.domain.ServiceItemBenefitTemplate.class.getName() + ".serviceItemBenefits");
            createCache(cm, org.nh.artha.domain.ServiceItemBenefitTemplate.class.getName() + ".variablePayouts");
            createCache(cm, org.nh.artha.domain.VariablePayout.class.getName() + ".serviceItemBenefitTemplates");
            createCache(cm, org.nh.artha.domain.ItemType.class.getName());
            createCache(cm, org.nh.artha.domain.ItemGroup.class.getName());
            createCache(cm, org.nh.artha.domain.Item.class.getName());
            createCache(cm, org.nh.artha.domain.ItemCategory.class.getName());
            createCache(cm, org.nh.artha.domain.ServiceType.class.getName());
            createCache(cm, org.nh.artha.domain.Group.class.getName());
            createCache(cm, org.nh.artha.domain.ServiceMaster.class.getName());
            createCache(cm, org.nh.artha.domain.ServiceMaster.class.getName() + ".profileInformations");
            createCache(cm, org.nh.artha.domain.PackageCodeMapping.class.getName());
            createCache(cm, org.nh.artha.domain.PackageComponent.class.getName());
            createCache(cm, org.nh.artha.domain.PackageMaster.class.getName());
            createCache(cm, org.nh.artha.domain.ServiceItemBenefitTemplate.class.getName());
            createCache(cm, org.nh.artha.domain.ServiceItemBenefitTemplate.class.getName() + ".serviceItemBenefits");
            createCache(cm, org.nh.artha.domain.ServiceItemBenefitTemplate.class.getName() + ".variablePayouts");
            createCache(cm, org.nh.artha.domain.VariablePayout.class.getName() + ".serviceItemBenefitTemplates");
            createCache(cm, org.nh.artha.domain.Module.class.getName());
            createCache(cm, org.nh.artha.domain.ServiceItemException.class.getName());
            createCache(cm, org.nh.artha.domain.Plan.class.getName());
            createCache(cm, org.nh.artha.domain.ExceptionSponsor.class.getName());
            createCache(cm, org.nh.artha.domain.ExceptionSponsor.class.getName() + ".plans");
            createCache(cm, org.nh.artha.domain.UserOrganizationDepartmentMapping.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache == null) {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

}
