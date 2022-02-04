package org.nh.artha.config;

/**
 * Created by sajithchandran on 1/30/17.
 */


import liquibase.integration.spring.SpringLiquibase;
import org.nh.artha.domain.HealthcareServiceCenter;
import org.nh.artha.domain.ServiceGroup;
import org.nh.artha.job.*;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;


@Configuration
public class SchedulerConfiguration {

    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext,
                                 // injecting SpringLiquibase to ensure liquibase is already initialized and created the quartz tables:
                                 SpringLiquibase springLiquibase) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean(name = "payoutJobDetails")
    public JobDetail payoutJobDetails() {
        return createJobDetail(PayoutJob.class, "SAR_JOB_DETAIL");
    }

    @Bean (name = "payoutJobTriggers")
    public CronTriggerFactoryBean payoutJobTriggers(@Qualifier("payoutJobDetails") JobDetail jobDetail) {
        return createCronTrigger(jobDetail, "0 0 12 ? DEC *", "SAR_JOB_TRIGGER");
    }
    @Bean(name = "departmentRevenueJobDetails")
    public JobDetail departmentRevenueJobDetails() {
        return createJobDetail(DepartmentRevenueJob.class, "DEPARTMENT_REVENUE_JOB_DETAIL");
    }

    @Bean (name = "departmentRevenueJobTriggers")
    public CronTriggerFactoryBean departmentRevenueJobTriggers(@Qualifier("departmentRevenueJobDetails") JobDetail jobDetail) {
        return createCronTrigger(jobDetail, "0 0 12 ? DEC *", "DEPARTMENT_REVENUE_JOB_TRIGGER");
    }


    private JobDetail createJobDetail(Class jobClass, String jobName) {
        return JobBuilder.newJob(jobClass).withIdentity(jobName).storeDurably().build();
    }
    private CronTriggerFactoryBean createCronTrigger(JobDetail jobDetail, String cronExpression, String triggerName) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setCronExpression(cronExpression);
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        factoryBean.setName(triggerName);
        return factoryBean;
    }

    @Bean
    public SchedulerFactoryBean createSchedulerFactoryBean(DataSource dataSource, JobFactory jobFactory,
                                                           List<Trigger> triggers,
                                                           PlatformTransactionManager transactionManager) throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        // this allows to update triggers in DB when updating settings in config file:
        factory.setSchedulerName("PAYOUT_SAR_SCHEDULER");
        factory.setOverwriteExistingJobs(false);
        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager);
        factory.setJobFactory(jobFactory);
        factory.setConfigLocation(new ClassPathResource("scheduler.properties"));
        factory.setTriggers(triggers.toArray(new Trigger[triggers.size()]));
        return factory;
    }

    @Bean(name = "serviceNameJobSync")
    public JobDetail serviceNameJobSync() {
        return createJobDetail(ServiceNameSyncJob.class, "SERVICE_NAME_SYNC_JOB");
    }

    @Bean (name = "serviceNameTriggers")
    public CronTriggerFactoryBean serviceNameTriggers(@Qualifier("serviceNameJobSync") JobDetail jobDetail) {
        return createCronTrigger(jobDetail, "0 0 12 ? DEC *", "SERVICE_NAME_SYNC_TRIGGER");
    }

    @Bean(name = "serviceGroupJobSync")
    public JobDetail serviceGroupJobSync() {
        return createJobDetail(ServiceGroupSyncJob.class, "SERVICE_GROUP_SYNC_JOB");
    }

    @Bean (name = "serviceGroupTriggers")
    public CronTriggerFactoryBean serviceGroupTriggers(@Qualifier("serviceGroupJobSync") JobDetail jobDetail) {
        return createCronTrigger(jobDetail, "0 0 12 ? DEC *", "SERVICE_GROUP_SYNC_TRIGGER");
    }

    @Bean(name = "planJobSync")
    public JobDetail planJobSync() {
        return createJobDetail(PlanSyncJob.class, "PLAN_SYNC_JOB");
    }

    @Bean (name = "planTriggers")
    public CronTriggerFactoryBean planTriggers(@Qualifier("planJobSync") JobDetail jobDetail) {
        return createCronTrigger(jobDetail, "0 0 12 ? DEC *", "PLAN_SYNC_TRIGGER");
    }

    @Bean(name = "packageJobSync")
    public JobDetail packageJobSync() {
        return createJobDetail(PackageNameSyncJob.class, "PACKAGE_SYNC_JOB");
    }

    @Bean (name = "packageJobTrigger")
    public CronTriggerFactoryBean packageJobTrigger(@Qualifier("packageJobSync") JobDetail jobDetail) {
        return createCronTrigger(jobDetail, "0 0 12 ? DEC *", "PACKAGE_SYNC_TRIGGER");
    }

    @Bean(name = "userJobSync")
    public JobDetail userJobSync() {
        return createJobDetail(UserSyncJob.class, "USER_SYNC_JOB");
    }

    @Bean (name = "userJobTrigger")
    public CronTriggerFactoryBean userJobTrigger(@Qualifier("userJobSync") JobDetail jobDetail) {
        return createCronTrigger(jobDetail, "0 0 12 ? DEC *", "USER_SYNC_TRIGGER");
    }

    @Bean(name = "itemJobSync")
    public JobDetail itemJobSync() {
        return createJobDetail(ItemNameSyncJob.class, "ITEM_SYNC_JOB");
    }

    @Bean (name = "itemJobTrigger")
    public CronTriggerFactoryBean itemJobTrigger(@Qualifier("itemJobSync") JobDetail jobDetail) {
        return createCronTrigger(jobDetail, "0 0 12 ? DEC *", "ITEM_SYNC_TRIGGER");
    }

    @Bean(name = "organizationJobSync")
    public JobDetail organizationJobSync() {
        return createJobDetail(OrganizationJobSync.class, "ORGANIZATION_SYNC_JOB");
    }

    @Bean (name = "organizationJobTrigger")
    public CronTriggerFactoryBean organizationJobTrigger(@Qualifier("organizationJobSync") JobDetail jobDetail) {
        return createCronTrigger(jobDetail, "0 0 12 ? DEC *", "ORGANIZATION_SYNC_TRIGGER");
    }

    @Bean(name = "userToUaaSyncJob")
    public JobDetail userToUaaSyncJob() {
        return createJobDetail(UserMasterToUaaUserSyncJob.class, "UAA_SYNC_JOB");
    }

    @Bean (name = "userToUaaJobTrigger")
    public CronTriggerFactoryBean usetToUaaJobTrigger(@Qualifier("userToUaaSyncJob") JobDetail jobDetail) {
        return createCronTrigger(jobDetail, "0 0 12 ? DEC *", "UAA_SYNC_TRIGGER");
    }

    @Bean(name = "UserorganizationJobSync")
    public JobDetail userOrganizationJobSync() {
        return createJobDetail(UserOrganizationDepartmentJobSync.class, "USERORGANIZATION_SYNC_JOB");
    }

    @Bean (name = "userorganizationJobTrigger")
    public CronTriggerFactoryBean userorganizationJobTrigger(@Qualifier("UserorganizationJobSync") JobDetail jobDetail) {
        return createCronTrigger(jobDetail, "0 0 12 ? DEC *", "USERORGANIZATION_SYNC_TRIGGER");
    }

    @Bean(name = "departmentJobSync")
    public JobDetail departmentJobSync() {
        return createJobDetail(DepartmentJobSync.class, "DEPARTMENT_SYNC_JOB");
    }

    @Bean (name = "departmentJobTrigger")
    public CronTriggerFactoryBean departmentJobTrigger(@Qualifier("departmentJobSync") JobDetail jobDetail) {
        return createCronTrigger(jobDetail, "0 0 12 ? DEC *", "DEPARTMENT_SYNC_TRIGGER");
    }

    @Bean(name = "tariffClassJobSync")
    public JobDetail tariffClassJobSync() {
        return createJobDetail(TariffClassJobSync.class, "TARIFF_CLASS_SYNC_JOB");
    }

    @Bean (name = "tariffClassJobTrigger")
    public CronTriggerFactoryBean tariffClassJobTrigger(@Qualifier("tariffClassJobSync") JobDetail jobDetail) {
        return createCronTrigger(jobDetail, "0 0 12 ? DEC *", "TARIFF_CLASS_SYNC_TRIGGER");
    }

    @Bean(name = "itemCategoryJobSync")
    public JobDetail itemCategoryJobSync() {
        return createJobDetail(ItemCategoryJobSync.class, "ITEM_CATEGORY_SYNC_JOB");
    }

    @Bean (name = "itemCategoryJobTrigger")
    public CronTriggerFactoryBean itemCategoryJobTrigger(@Qualifier("itemCategoryJobSync") JobDetail jobDetail) {
        return createCronTrigger(jobDetail, "0 0 12 ? DEC *", "ITEM_CATEGORY_SYNC_TRIGGER");
    }

    @Bean(name = "itemGroupJobSync")
    public JobDetail itemGroupJobSync() {
        return createJobDetail(ItemGroupJobSync.class, "ITEM_GROUP_SYNC_JOB");
    }

    @Bean (name = "itemGroupJobTrigger")
    public CronTriggerFactoryBean itemGroupJobTrigger(@Qualifier("itemGroupJobSync") JobDetail jobDetail) {
        return createCronTrigger(jobDetail, "0 0 12 ? DEC *", "ITEM_GROUP_SYNC_TRIGGER");
    }
    @Bean(name = "healthcareServiceCenterJobSync")
    public JobDetail healthcareServiceCenterJobSync() {
        return createJobDetail(HealthcareServiceCentreSyncJob.class, "HEALTHCARE_SERVICE_CENTER_SYNC_JOB");
    }

    @Bean (name = "healthcareServiceCenterTriggers")
    public CronTriggerFactoryBean healthcareServiceCenterTriggers(@Qualifier("healthcareServiceCenterJobSync") JobDetail jobDetail) {
        return createCronTrigger(jobDetail, "0 0 12 ? DEC *", "HEALTHCARE_SERVICE_CENTER_SYNC_TRIGGER");
    }

    @Bean(name = "serviceTypeJobSync")
    public JobDetail serviceTypeJobSync() {
        return createJobDetail(ServiceTypeJobSync.class, "SERVICE_TYPE_SYNC_JOB");
    }

    @Bean (name = "serviceTypeJobTrigger")
    public CronTriggerFactoryBean serviceTypeJobTrigger(@Qualifier("serviceTypeJobSync") JobDetail jobDetail) {
        return createCronTrigger(jobDetail, "0 0 12 ? DEC *", "SERVICE_TYPE_SYNC_TRIGGER");
    }
    @Bean(name = "sponsorTypeJobSync")
    public JobDetail sponsorTypeJobSync() {
        return createJobDetail(SponsorTypeJobSync.class, "SPONSOR_TYPE_SYNC_JOB");
    }

    @Bean (name = "sponsorTypeJobTrigger")
    public CronTriggerFactoryBean sponsorTypeJobTrigger(@Qualifier("sponsorTypeJobSync") JobDetail jobDetail) {
        return createCronTrigger(jobDetail, "0 0 12 ? DEC *", "SPONSOR_TYPE_SYNC_TRIGGER");
    }

}
