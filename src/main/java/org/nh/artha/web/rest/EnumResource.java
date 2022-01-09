package org.nh.artha.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.enumeration.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class EnumResource {

    private final Logger log = LoggerFactory.getLogger(DoctorPayoutResource.class);

    private static final String ENTITY_NAME = "EnumResource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApplicationProperties applicationProperties;

    public EnumResource(ApplicationProperties applicationProperties){
        this.applicationProperties=applicationProperties;
    }

    @GetMapping("/applicable-invoices-type")
    public  ResponseEntity<Map<String,String>> getApplicableInvoices() throws URISyntaxException {
        List<ApplicableInvoices> applicableInvoices = Arrays.asList(ApplicableInvoices.values());
        Map<String,String> applicableInvoiceType= new HashMap<>();
        for(int i=0;i<applicableInvoices.size();i++){
            applicableInvoiceType.put(applicableInvoices.get(i).getName(),applicableInvoices.get(i).getDisplay());
        }
        return ResponseEntity.ok()
            .body(applicableInvoiceType);
    }

    @GetMapping("/applicable-on")
    public  ResponseEntity<Map<String,String>> getApplicableOnTypes() throws URISyntaxException {
        List<ApplicableOn> applicableOnList = Arrays.asList(ApplicableOn.values());
        Map<String,String> applicableOnMap= new HashMap<>();

        for(int i=0;i<applicableOnList.size();i++){
            applicableOnMap.put(applicableOnList.get(i).getName(),applicableOnList.get(i).getDisplay());
        }
        return ResponseEntity.ok()
            .body(applicableOnMap);
    }

    @GetMapping("/beneficiary-type")
    public  ResponseEntity<Map<String,String>> getBeneficiaryTypes() throws URISyntaxException {
        List<Beneficiary> beneficiaryList = Arrays.asList(Beneficiary.values());
        Map<String,String> beneficiaryMap= new LinkedHashMap<>();

        for(int i=0;i<beneficiaryList.size();i++){
            beneficiaryMap.put(beneficiaryList.get(i).getName(),beneficiaryList.get(i).getDisplay());
        }
        return ResponseEntity.ok()
            .body(beneficiaryMap);
    }

    @GetMapping("/change-request-status")
    public  ResponseEntity<Map<String,String>> getChangeRequestStatus() throws URISyntaxException {
        List<ChangeRequestStatus> changeRequestStatusList = Arrays.asList(ChangeRequestStatus.values());
        Map<String,String> changeRequestStatusMap= new HashMap<>();

        for(int i=0;i<changeRequestStatusList.size();i++){
            changeRequestStatusMap.put(changeRequestStatusList.get(i).getName(),changeRequestStatusList.get(i).getDisplay());
        }
        return ResponseEntity.ok()
            .body(changeRequestStatusMap);
    }

    @GetMapping("/configuration-input-type")
    public  ResponseEntity<Map<String,String>> getConfigurationInputType() throws URISyntaxException {
        List<ConfigurationInputType> configurationInputTypeList = Arrays.asList(ConfigurationInputType.values());

        Map<String,String> configurationInputTypeMap= new HashMap<>();
        for(int i=0;i<configurationInputTypeList.size();i++){
            configurationInputTypeMap.put(configurationInputTypeList.get(i).getName(),configurationInputTypeList.get(i).getDisplay());
        }
        return ResponseEntity.ok()
            .body(configurationInputTypeMap);
    }

    @GetMapping("/configuration-level")
    public  ResponseEntity<Map<String,String>> getConfigurationLevel() throws URISyntaxException {
        List<ConfigurationLevel> configurationLevelList = Arrays.asList(ConfigurationLevel.values());
        Map<String,String> configurationLevelMap= new HashMap<>();

        for(int i=0;i<configurationLevelList.size();i++){
            configurationLevelMap.put(configurationLevelList.get(i).getName(),configurationLevelList.get(i).getDisplay());
        }
        return ResponseEntity.ok()
            .body(configurationLevelMap);
    }

    @GetMapping("/consultant-type")
    public  ResponseEntity<Map<String,String>> getConsultantType() throws URISyntaxException {
        List<ConsultantType> consultantTypeList = Arrays.asList(ConsultantType.values());
        Map<String,String> consultantTypeMap= new HashMap<>();

        for(int i=0;i<consultantTypeList.size();i++){
            consultantTypeMap.put(consultantTypeList.get(i).getName(),consultantTypeList.get(i).getDisplay());
        }

        return ResponseEntity.ok()
            .body(consultantTypeMap);
    }

    @GetMapping("/context")
    public  ResponseEntity<Map<String,String>> getContext() throws URISyntaxException {
        List<Context> contextList = Arrays.asList(Context.values());

        Map<String,String> contextMap= new HashMap<>();
        for(int i=0;i<contextList.size();i++){
            contextMap.put(contextList.get(i).getName(),contextList.get(i).getDisplay());
        }
        return ResponseEntity.ok()
            .body(contextMap);
    }

    @GetMapping("/discount-type")
    public  ResponseEntity<List<DiscountType>> getDiscountType() throws URISyntaxException {
        List<DiscountType> discountTypeList = Arrays.asList(DiscountType.values());

        Map<String,String> discountTypeMap= new HashMap<>();
        for(int i=0;i<discountTypeList.size();i++){
            discountTypeMap.put(discountTypeList.get(i).getName(),discountTypeList.get(i).getDisplay());
        }
        return ResponseEntity.ok()
            .body(discountTypeList);
    }

    @GetMapping("/document-type")
    public  ResponseEntity<Map<String,String>> getDocumentType() throws URISyntaxException {
        List<DocumentType> documentTypeList = Arrays.asList(DocumentType.values());

        Map<String,String> documentTypeMap= new HashMap<>();
        for(int i=0;i<documentTypeList.size();i++){
            documentTypeMap.put(documentTypeList.get(i).getName(),documentTypeList.get(i).getDisplay());
        }
        return ResponseEntity.created(new URI("/api/document-type/"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,documentTypeList.toString()))
            .body(documentTypeMap);
    }

    @GetMapping("/feature-type")
    public  ResponseEntity<Map<String,String>> getFeatureType() throws URISyntaxException {
        List<FeatureType> featureTypeList = Arrays.asList(FeatureType.values());
        Map<String,String> featureTypeMap= new HashMap<>();

        for(int i=0;i<featureTypeList.size();i++){
            featureTypeMap.put(featureTypeList.get(i).getName(),featureTypeList.get(i).getDisplay());
        }
        return ResponseEntity.ok()
            .body(featureTypeMap);
    }

    @GetMapping("/package-category-type")
    public  ResponseEntity<Map<String,String>> getPackageCategoryType() throws URISyntaxException {
        List<PackageCategory> packageCategoryList = Arrays.asList(PackageCategory.values());
        Map<String,String> packageCategoryMap= new HashMap<>();

        for(int i=0;i<packageCategoryList.size();i++){
            packageCategoryMap.put(packageCategoryList.get(i).getName(),packageCategoryList.get(i).getDisplay());
        }
        return ResponseEntity.ok()
            .body(packageCategoryMap);
    }

    @GetMapping("/patient-gender")
    public  ResponseEntity<List<PatientGender>> getPatientGender() throws URISyntaxException {
        List<PatientGender> patientGenderList = Arrays.asList(PatientGender.values());

        return ResponseEntity.ok()
            .body(patientGenderList);
    }

    @GetMapping("/payment-mode-type")
    public  ResponseEntity<Map<String,String>> getPaymentModeType() throws URISyntaxException {
        List<PaymentMode> paymentModeList = Arrays.asList(PaymentMode.values());

        Map<String,String> paymentModeMap= new HashMap<>();
        for(int i=0;i<paymentModeList.size();i++){
            paymentModeMap.put(paymentModeList.get(i).getName(),paymentModeList.get(i).getDisplay());
        }
        return ResponseEntity.ok()
            .body(paymentModeMap);
    }


    @GetMapping("/request-status-type")
    public  ResponseEntity<Map<String,String>> getRequestStatusType() throws URISyntaxException {
        List<RequestStatus> requestStatusList = Arrays.asList(RequestStatus.values());

        Map<String,String> requestStatusMap= new HashMap<>();
        for(int i=0;i<requestStatusList.size();i++){
            requestStatusMap.put(requestStatusList.get(i).getName(),requestStatusList.get(i).getDisplay());
        }
        return ResponseEntity.ok()
            .body(requestStatusMap);
    }

    @GetMapping("/revenue-benefit-type")
    public  ResponseEntity<Map<String,String>> getRevenueBenefitType() throws URISyntaxException {
        List<RevenueBenefitType> revenueBenefitTypeList = Arrays.asList(RevenueBenefitType.values());
        Map<String,String> revenueBenefitTypeMap= new HashMap<>();
        for(int i=0;i<revenueBenefitTypeList.size();i++){
            revenueBenefitTypeMap.put(revenueBenefitTypeList.get(i).getName(),revenueBenefitTypeList.get(i).getDisplay());
        }
        return ResponseEntity.ok()
            .body(revenueBenefitTypeMap);
    }

    @GetMapping("/transaction-type")
    public  ResponseEntity<Map<String,String>> getTransactionType() throws URISyntaxException {
        List<TransactionType> transactionTypeList = Arrays.asList(TransactionType.values());
        Map<String,String> transactionTypeMap= new HashMap<>();

        for(int i=0;i<transactionTypeList.size();i++){
            transactionTypeMap.put(transactionTypeList.get(i).getName(),transactionTypeList.get(i).getDisplay());
        }
        return ResponseEntity.ok()
            .body(transactionTypeMap);
    }

    @GetMapping("/type")
    public  ResponseEntity<Map<String,String>> getType() throws URISyntaxException {
        List<Type> typeList = Arrays.asList(Type.values());
        typeList.sort(Comparator.comparing(Type::getOrder));
        Map<String,String> typeMap= new LinkedHashMap<>();
        for(int i=0;i<typeList.size();i++){
            typeMap.put(typeList.get(i).getName(),typeList.get(i).getDisplay());
        }
        return ResponseEntity.ok()
            .body(typeMap);
    }

    @GetMapping("/patient-category")
    public  ResponseEntity<Map<String,String>> getPatientCategory() throws URISyntaxException {
        List<PatientCategory> patientCategories = Arrays.asList(PatientCategory.values());

        Map<String,String> patientCategoriesMap= new HashMap<>();
        for(int i=0;i<patientCategories.size();i++){
            patientCategoriesMap.put(patientCategories.get(i).getName(),patientCategories.get(i).getDisplay());
        }
        return ResponseEntity.ok()
            .body(patientCategoriesMap);
    }

    @GetMapping("/material-amount")
    public  ResponseEntity<Map<String,String>> getMaterialAmount() throws URISyntaxException {
        List<MaterialAmount> materialAmountList = Arrays.asList(MaterialAmount.values());

        Map<String,String> materialAmountMap= new HashMap<>();
        for(int i=0;i<materialAmountList.size();i++){
            materialAmountMap.put(materialAmountList.get(i).getName(),materialAmountList.get(i).getDisplay());
        }
        return ResponseEntity.ok()
            .body(materialAmountMap);
    }

    @GetMapping("/visit-type")
    public  ResponseEntity<Map<String,String>> getVisitType() throws URISyntaxException {
        List<VisitType> visitTypeList = Arrays.asList(VisitType.values());

        Map<String,String> visitTypeMap= new HashMap<>();
        for(int i=0;i<visitTypeList.size();i++){
            visitTypeMap.put(visitTypeList.get(i).getName(),visitTypeList.get(i).getDisplay());
        }
        return ResponseEntity.ok()
            .body(visitTypeMap);
    }


    @GetMapping("/exception-type")
    public  ResponseEntity<Map<String,String>> getExceptionType() throws URISyntaxException {
        List<ExceptionType> exceptionTypeList = Arrays.asList(ExceptionType.values());

        Map<String,String> exceptionTypeMap= new HashMap<>();
        for(int i=0;i<exceptionTypeList.size();i++){
            exceptionTypeMap.put(exceptionTypeList.get(i).getName(),exceptionTypeList.get(i).getDisplay());
        }
        return ResponseEntity.ok()
            .body(exceptionTypeMap);
    }

    @GetMapping("/invoice-value-type")
    public  ResponseEntity<Map<String,String>> getInvoiceValueType() throws URISyntaxException {
        List<InvoiceValue> InvoiceValueTypeList = Arrays.asList(InvoiceValue.values());

        Map<String,String> InvoiceValueTypeMap= new HashMap<>();
        for(int i=0;i<InvoiceValueTypeList.size();i++){
            InvoiceValueTypeMap.put(InvoiceValueTypeList.get(i).getName(),InvoiceValueTypeList.get(i).getDisplay());
        }
        return ResponseEntity.ok()
            .body(InvoiceValueTypeMap);
    }

}
