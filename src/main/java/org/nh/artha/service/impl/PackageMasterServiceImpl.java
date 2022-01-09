package org.nh.artha.service.impl;

import liquibase.util.csv.CSVReader;
import org.nh.artha.domain.PackageMaster;
import org.nh.artha.domain.enumeration.PackageCategory;
import org.nh.artha.domain.enumeration.VisitType;
import org.nh.artha.domain.enumeration.durationUnit;
import org.nh.artha.repository.PackageMasterRepository;
import org.nh.artha.repository.search.PackageMasterSearchRepository;
import org.nh.artha.service.PackageMasterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing {@link PackageMaster}.
 */
@Service
@Transactional
public class PackageMasterServiceImpl implements PackageMasterService {

    private final Logger log = LoggerFactory.getLogger(PackageMasterServiceImpl.class);

    private final PackageMasterRepository packageMasterRepository;

    private final PackageMasterSearchRepository packageMasterSearchRepository;

    public PackageMasterServiceImpl(PackageMasterRepository packageMasterRepository, PackageMasterSearchRepository packageMasterSearchRepository) {
        this.packageMasterRepository = packageMasterRepository;
        this.packageMasterSearchRepository = packageMasterSearchRepository;
    }

    /**
     * Save a packageMaster.
     *
     * @param packageMaster the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PackageMaster save(PackageMaster packageMaster) {
        log.debug("Request to save PackageMaster : {}", packageMaster);
        PackageMaster result = packageMasterRepository.save(packageMaster);
        packageMasterSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the packageMasters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PackageMaster> findAll(Pageable pageable) {
        log.debug("Request to get all PackageMasters");
        return packageMasterRepository.findAll(pageable);
    }


    /**
     * Get one packageMaster by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PackageMaster> findOne(Long id) {
        log.debug("Request to get PackageMaster : {}", id);
        return packageMasterRepository.findById(id);
    }

    /**
     * Delete the packageMaster by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PackageMaster : {}", id);
        packageMasterRepository.deleteById(id);
        packageMasterSearchRepository.deleteById(id);
    }

    /**
     * Search for the packageMaster corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PackageMaster> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PackageMasters for query {}", query);
        return packageMasterSearchRepository.search(queryStringQuery(query).field("name").field("code"), pageable);
    }

    @Override
    public void importPackage(MultipartFile file)throws  Exception{
        Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        CSVReader csvReader=new CSVReader(reader);
        List<String[]> packageMasterArray = csvReader.readAll();
        packageMasterArray.remove(0);
        List<PackageMaster> all = packageMasterArray.stream().map(createPackageMasterFromCsv).distinct().collect(Collectors.toList());
        saveAll(all);
    }

    @Override
    public List<PackageMaster> saveAll(List<PackageMaster> packageMasterList) {
        List<PackageMaster> packageMasters = packageMasterRepository.saveAll(packageMasterList);
        packageMasterSearchRepository.saveAll(packageMasters);
        return packageMasters;
    }

    private Function<String[] , PackageMaster> createPackageMasterFromCsv = (String [] csvRowArray) -> {
        PackageMaster packageMaster=new PackageMaster();
        try {
            packageMaster.setCode(csvRowArray[0]);
            packageMaster.setName(csvRowArray[1]);
            packageMaster.setPackageCategory(csvRowArray[2].equalsIgnoreCase(PackageCategory.DIAGNOSTIC.name())?PackageCategory.DIAGNOSTIC:PackageCategory.HEALTH);
       //     packageMaster.setVisitType(VisitType.OP);
            packageMaster.setStartDate(LocalDate.now());
            packageMaster.setEndDate(LocalDate.now().plusYears(1));
            packageMaster.setTemplate(csvRowArray[7].equalsIgnoreCase("Active")?true:false);
            packageMaster.setActive(csvRowArray[8].equalsIgnoreCase("Active")?true:false);
          //  packageMaster.setDurationUnit(durationUnit.DAYS);
        } catch (Exception e) {
            System.out.println(Arrays.toString(csvRowArray));
            throw new RuntimeException(e);
        }
        return packageMaster;
    };

}
