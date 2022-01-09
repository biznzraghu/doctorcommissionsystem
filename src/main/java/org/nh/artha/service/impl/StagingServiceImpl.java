package org.nh.artha.service.impl;

import liquibase.util.csv.CSVReader;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.McrStaging;
import org.nh.artha.domain.ServiceAnalysisStaging;
import org.nh.artha.repository.McrStagingRepository;
import org.nh.artha.repository.ServiceAnalysisRepository;
import org.nh.artha.service.DoctorPayoutService;
import org.nh.artha.service.StagingService;
import org.nh.artha.service.VariablePayoutServiceAnalysisLoading;
import org.nh.artha.util.KeyGenerator;
import org.nh.artha.web.rest.StagingResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StagingServiceImpl implements StagingService {

    private final Logger log = LoggerFactory.getLogger(StagingResource.class);

    private final ApplicationProperties applicationProperties;

    private final McrStagingRepository mcrStagingRepository;

    private final ServiceAnalysisRepository serviceAnalysisRepository;

    private final VariablePayoutServiceAnalysisLoading variablePayoutServiceAnalysisLoading;



    public StagingServiceImpl(ApplicationProperties applicationProperties, McrStagingRepository mcrStagingRepository, ServiceAnalysisRepository serviceAnalysisRepository,VariablePayoutServiceAnalysisLoading variablePayoutServiceAnalysisLoading) {
        this.applicationProperties = applicationProperties;
        this.mcrStagingRepository = mcrStagingRepository;
        this.serviceAnalysisRepository = serviceAnalysisRepository;
        this.variablePayoutServiceAnalysisLoading = variablePayoutServiceAnalysisLoading;


    }

    @Override
    public String uploadFile(MultipartFile multipartFile, String type) throws IOException {
        long startTime = System.currentTimeMillis();
        long totalMemory = Runtime.getRuntime().totalMemory();
        log.debug("Uploading {} file", multipartFile.getName());
        String filePath = null;
        if (type.equals("MCR_STAGING")) {
            filePath = applicationProperties.getStaging().getMcr().getFilelocation();
        } else if (type.equals("SERVICE_ANALYSIS_STAGING")) {
            filePath = applicationProperties.getStaging().getServiceAnalysis().getFilelocation();
        }
        if (!Files.exists(Paths.get(filePath))) {
            Files.createDirectories(Paths.get(filePath));
        } else {
            File file1 = new File(filePath + "userName_" + System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename());
            System.out.println(file1.getCanonicalPath());

        }
        byte[] bytes = multipartFile.getBytes();
        Path path = Paths.get(filePath + multipartFile.getOriginalFilename());
        Files.write(path, bytes);
        int totalRecord =insertDataInStagingTable(path.toString(), type);
        long totalTime = System.currentTimeMillis() - startTime;
        return "Imported Successfully " + totalRecord+" Rows" + " And Total Time Taken = " + totalTime + " in milli second And Total Memory Used= " + (totalMemory - Runtime.getRuntime().freeMemory());

    }

    private int insertDataInStagingTable(String str, String type) throws IOException{
        FileReader reader = new FileReader(str);
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> readAll = csvReader.readAll();
        readAll.remove(0);
        if(type.equalsIgnoreCase("MCR_STAGING")) {
            List<McrStaging> collect = readAll.parallelStream().map(createMcrStagingObject).collect(Collectors.toList());
            mcrStagingRepository.saveAll(collect);
            return collect.size();
        }else if(type.equalsIgnoreCase("SERVICE_ANALYSIS_STAGING")){
            List<ServiceAnalysisStaging> collect = readAll.parallelStream().map(createServiceAnalysis).collect(Collectors.toList());
            serviceAnalysisRepository.saveAll(collect);
            return collect.size();
        }
        return 0;
    }

    private Function<String[] , McrStaging> createMcrStagingObject = (String [] csvRowArray) -> {
        McrStaging mcrStaging = null;
        try {

            String mcrVisitTypeEmployeeIdDepartmentKey = KeyGenerator.createMcrVisitTypeEmployeeIdDepartmentKey(mcrStaging.getVisitType(), mcrStaging.getEmployeeId());
            String mcrVisitTypeDepartmentAmountMaterialKey = KeyGenerator.createMcrVisitTypeDepartmentAmountMaterialKey(mcrStaging.getVisitType(), mcrStaging.getDepartmentCode(), "");
            mcrStaging.setVisitTypeDeptAmtMaterial(mcrVisitTypeEmployeeIdDepartmentKey);
        } catch (Exception e) {
            System.out.println(Arrays.toString(csvRowArray));
            throw new RuntimeException(e);
        }
        return mcrStaging;
    };

    private Function<String[] , ServiceAnalysisStaging> createServiceAnalysis = (String [] csvRowArray) -> {
        ServiceAnalysisStaging serviceAnalysisStaging = null;
        try {
            serviceAnalysisStaging = new ServiceAnalysisStaging(csvRowArray[0],csvRowArray[1],csvRowArray[2],csvRowArray[3],
                csvRowArray[4],csvRowArray[5],csvRowArray[6],csvRowArray[7],csvRowArray[8],csvRowArray[9],csvRowArray[10],
                csvRowArray[11],csvRowArray[12], csvRowArray[13],csvRowArray[14],csvRowArray[15],csvRowArray[16],csvRowArray[17],
                csvRowArray[18],csvRowArray[19],csvRowArray[20], csvRowArray[21],csvRowArray[22],csvRowArray[23],csvRowArray[24],csvRowArray[25],
                csvRowArray[26],csvRowArray[27],csvRowArray[28],
                csvRowArray[29],csvRowArray[30],csvRowArray[31],csvRowArray[32],csvRowArray[33],csvRowArray[34],csvRowArray[35],csvRowArray[36],
                csvRowArray[37],csvRowArray[38],csvRowArray[39],csvRowArray[40],csvRowArray[41],csvRowArray[42],csvRowArray[43],csvRowArray[44],
                csvRowArray[45],csvRowArray[46],csvRowArray[47],csvRowArray[48],csvRowArray[49],csvRowArray[50],csvRowArray[51],csvRowArray[52],csvRowArray[53],
                csvRowArray[54],csvRowArray[55],csvRowArray[56],csvRowArray[57],csvRowArray[58],csvRowArray[59],csvRowArray[60],csvRowArray[61]);
            KeyGenerator keyGenerator = new KeyGenerator();
            String groupkey = keyGenerator.createKey(serviceAnalysisStaging.getItemGroup());
            String typekey = keyGenerator.createKey(serviceAnalysisStaging.getItemType());
            String visitTypeTariffClassKey = keyGenerator.createVisitTypeTariffClassKey(serviceAnalysisStaging.getVisitType(), serviceAnalysisStaging.getTariffClass());
            serviceAnalysisStaging.setGroupKey(groupkey);
            serviceAnalysisStaging.setTypeKey(typekey);
            serviceAnalysisStaging.setVisitTypeTariffClassKey(visitTypeTariffClassKey);

        } catch (Exception e) {
            System.out.println(Arrays.toString(csvRowArray));
            throw  new RuntimeException(e);
        }
        return serviceAnalysisStaging;
    };

}
