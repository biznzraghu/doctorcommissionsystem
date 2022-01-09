package org.nh.artha.service.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;
import org.elasticsearch.index.query.Operator;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.*;
import org.nh.artha.domain.enumeration.TransactionType;
import org.nh.artha.domain.enumeration.Type;
import org.nh.artha.repository.*;
import org.nh.artha.repository.search.DoctorPayoutSearchRepository;
import org.nh.artha.security.SecurityUtils;
import org.nh.artha.security.UserPreferencesUtils;
import org.nh.artha.service.*;
import org.nh.artha.domain.dto.DepartmentMasterDTO;
import org.nh.artha.util.ExportUtil;
import org.nh.artha.web.rest.errors.CustomParameterizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing {@link DoctorPayout}.
 */
@Service
public class DoctorPayoutServiceImpl implements DoctorPayoutService {

    private final Logger log = LoggerFactory.getLogger(DoctorPayoutServiceImpl.class);

    private final DoctorPayoutRepository doctorPayoutRepository;

    private final DoctorPayoutSearchRepository doctorPayoutSearchRepository;

    private final UserMasterRepository userMasterRepository;

    private final OrganizationRepository organizationRepository;

    private final ApplicationProperties applicationProperties;

    private final UserOrganizationDepartmentMappingService userDepartmentMappingService;

    private final UserOrganizationDepartmentMappingRepository userDepartmentMappingRepository;

    private final ServiceMasterRepository serviceMasterRepository;

    private final McrStagingRepository mcrStagingRepository;

    private final ServiceAnalysisRepository serviceAnalysisRepository;

    private final PayoutAdjustmentRepository payoutAdjustmentRepository;

    private final VariablePayoutRepository variablePayoutRepository;

    private final VariablePayoutService variablePayoutService;

    private final PayoutAdjustmentService payoutAdjustmentService;

    @Autowired
    @Lazy
    private VariablePayoutServiceAnalysisLoading variablePayoutServiceAnalysisLoading;

    private final DepartmentPayoutRepository departmentPayoutRepository;

    private final DoctorPayoutDepartmentRepository doctorPayoutDepartmentRepository;

    private final PayoutAdjustmentDetailsRepository payoutAdjustmentDetailsRepository;

    private final DepartmentRepository departmentRepository;

    private final InvoiceDoctorPayoutRepository invoiceDoctorPayoutRepository;

    @Autowired
    @Lazy
    private DoctorPayoutService doctorPayoutService;

    @Autowired
    private LengthOfStayBenefitService lengthOfStayBenefitService;

    @Autowired
    private DoctorPayoutLOSService doctorPayoutLOSService;

    private final LengthOfStayBenefitRepository lengthOfStayBenefitRepository;

    private final DoctorPayoutLOSRepository doctorPayoutLOSRepository;

    public DoctorPayoutServiceImpl(DoctorPayoutRepository doctorPayoutRepository, DoctorPayoutSearchRepository doctorPayoutSearchRepository,
                                   UserMasterRepository userMasterRepository, OrganizationRepository organizationRepository, ApplicationProperties applicationProperties,
                                   UserOrganizationDepartmentMappingService userDepartmentMappingService, UserOrganizationDepartmentMappingRepository userDepartmentMappingRepository,
                                   ServiceMasterRepository serviceMasterRepository, McrStagingRepository mcrStagingRepository, ServiceAnalysisRepository serviceAnalysisRepository,
                                   PayoutAdjustmentRepository payoutAdjustmentRepository, VariablePayoutRepository variablePayoutRepository,
                                   VariablePayoutService variablePayoutService, PayoutAdjustmentService payoutAdjustmentService,
                                   DepartmentPayoutRepository departmentPayoutRepository, DoctorPayoutDepartmentRepository doctorPayoutDepartmentRepository,
                                   PayoutAdjustmentDetailsRepository payoutAdjustmentDetailsRepository,
                                   DepartmentRepository departmentRepository, InvoiceDoctorPayoutRepository invoiceDoctorPayoutRepository, LengthOfStayBenefitRepository lengthOfStayBenefitRepository,
                                   DoctorPayoutLOSRepository doctorPayoutLOSRepository) {
        this.doctorPayoutRepository = doctorPayoutRepository;
        this.doctorPayoutSearchRepository = doctorPayoutSearchRepository;
        this.userMasterRepository = userMasterRepository;
        this.organizationRepository = organizationRepository;
        this.applicationProperties = applicationProperties;
        this.userDepartmentMappingService = userDepartmentMappingService;
        this.userDepartmentMappingRepository = userDepartmentMappingRepository;
        this.serviceMasterRepository = serviceMasterRepository;
        this.mcrStagingRepository = mcrStagingRepository;
        this.serviceAnalysisRepository = serviceAnalysisRepository;
        this.payoutAdjustmentRepository = payoutAdjustmentRepository;
        this.variablePayoutRepository = variablePayoutRepository;
        this.variablePayoutService = variablePayoutService;
        this.payoutAdjustmentService = payoutAdjustmentService;
        this.departmentPayoutRepository = departmentPayoutRepository;
        this.doctorPayoutDepartmentRepository = doctorPayoutDepartmentRepository;
        this.payoutAdjustmentDetailsRepository = payoutAdjustmentDetailsRepository;
        this.departmentRepository = departmentRepository;
        this.invoiceDoctorPayoutRepository = invoiceDoctorPayoutRepository;
        this.lengthOfStayBenefitRepository = lengthOfStayBenefitRepository;
        this.doctorPayoutLOSRepository = doctorPayoutLOSRepository;
    }

    /**
     * Save a doctorPayout.
     *
     * @param doctorPayout the entity to save.
     * @return the persisted entity.
     */
    @Override
    @Transactional
    public DoctorPayout save(DoctorPayout doctorPayout) {
        log.debug("Request to save DoctorPayout : {}", doctorPayout);
        DoctorPayout result = doctorPayoutRepository.save(doctorPayout);
        doctorPayoutSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the doctorPayouts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DoctorPayout> findAll(Pageable pageable) {
        log.debug("Request to get all DoctorPayouts");
        return doctorPayoutRepository.findAll(pageable);
    }


    /**
     * Get one doctorPayout by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DoctorPayout> findOne(Long id) {
        log.debug("Request to get DoctorPayout : {}", id);
        return doctorPayoutRepository.findById(id);
    }

    /**
     * Delete the doctorPayout by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DoctorPayout : {}", id);
        doctorPayoutRepository.deleteById(id);
        doctorPayoutSearchRepository.deleteById(id);
    }

    /**
     * Search for the doctorPayout corresponding to the query.
     *
     * @param query    the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DoctorPayout> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DoctorPayouts for query {}", query);
        return doctorPayoutSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on Doctor Payout");
        List<DoctorPayout> data = doctorPayoutRepository.findByDateRangeSortById(fromDate, toDate, PageRequest.of(pageNo, pageSize));
        if (!data.isEmpty()) {
            data.forEach(doctorPayout -> {
                doctorPayoutSearchRepository.save(doctorPayout);
                doctorPayoutSearchRepository.indexWithoutRefresh(doctorPayout);
            });
        }
    }

    @Override
    public Map<String, Object> exportDoctorPayoutBreakUpSummary(String query, Pageable pageable) throws Exception {
        Map<String, Object> returnObject = new HashMap<>();
        Iterable<DoctorPayout> search = doctorPayoutSearchRepository.search(queryStringQuery(query));
        List<DoctorPayout> collect = StreamSupport.stream(search.spliterator(), false)
            .collect(Collectors.toList());
        String[] split = query.split("AND");
        Map<String, String> queryMap = new HashMap<>();
        for (int i = 0; i < split.length; i++) {
            queryMap.put(split[i].split(":")[0].trim(), split[i].split(":")[1].trim());
        }
        String unitCode = queryMap.get("unitCode");
        Integer year = Integer.parseInt(queryMap.get("year"));
        Integer month = Integer.parseInt(queryMap.get("month"));
        Long employeeId = Long.valueOf(queryMap.get("employeeId"));
        Long latestApprovedVariablePayoutId = variablePayoutRepository.getLatestApprovedVariablePayoutId(queryMap.get("unitCode"), Long.valueOf(queryMap.get("employeeId")));
        if (((PageImpl) search).getContent().size() <= 0) {
            if (latestApprovedVariablePayoutId == null) {
                throw new CustomParameterizedException("10395", "Doctor Payout Breakup Summary Is Not Available For The Selected Employee");
            } else {
                collect.add(saveDoctorPayout(year, month, employeeId, unitCode,null));
            }
        }
        File file = getXLSXExportFile("Doctor Payout Breakup Summary", 1l, applicationProperties.getAthmaBucket().getTempExport());
        DoctorPayout doctorPayout = collect.get(0);
        Boolean modifiedRule = false;
        if (doctorPayout.getVariablePayoutId() != null && !doctorPayout.getVariablePayoutId().equals(latestApprovedVariablePayoutId)) {
            modifiedRule = true;
        }
        if (doctorPayout.getVariablePayoutId() == null && latestApprovedVariablePayoutId != null) {
            modifiedRule = true;
        }
        VariablePayout variablePayout = variablePayoutRepository.findById(latestApprovedVariablePayoutId).get();
        LocalDateTime minDate = LocalDateTime.of(year, month, 1,0,0,0).with(TemporalAdjusters.firstDayOfMonth());
        LocalDateTime maxDate = LocalDateTime.of(minDate.with(TemporalAdjusters.lastDayOfMonth()).toLocalDate(), LocalTime.MIDNIGHT);
        Boolean flag=validateCommencementAndContractEndDate(variablePayout,minDate,maxDate);
        if(!flag){
            throw new CustomParameterizedException("100020","CommencementDate is greater than Matching with selected month");
        }
        if (modifiedRule) {
            doctorPayout = variablePayoutServiceAnalysisLoading.executeVariablePayoutByYearMonthAndID(unitCode, year, month, employeeId, latestApprovedVariablePayoutId, doctorPayout);
            deleteOldLosValue(year, month, employeeId, unitCode, doctorPayout);
            Map<LengthOfStayBenefit, List<ServiceAnalysisStaging>> patientBasedOnLosBenefit = lengthOfStayBenefitService.findPatientBasedOnLosBenefit(variablePayout);
            List<DoctorPayoutLOS> doctorPayoutLOSList = doctorPayoutLOSService.calculateDoctorPayoutLos(patientBasedOnLosBenefit, doctorPayout);
            if (doctorPayoutLOSList != null && !doctorPayoutLOSList.isEmpty())
                doctorPayout.setLosBenefietIds(doctorPayoutLOSList.stream().collect(Collectors.toSet()));
            doctorPayout.setVariablePayoutId(latestApprovedVariablePayoutId);
            log.debug("Doctor Payout ::",doctorPayout);
            saveDoctorPayout(year,month,employeeId,unitCode,doctorPayout);
        }
        doctorPayout = doctorPayoutRepository.findById(doctorPayout.getId()).orElse(null);
        XSSFWorkbook workbook = geDataFromFile("Doctor Payout Breakup Summary.xlsx", applicationProperties.getAthmaBucket().getTemplate());
        XSSFSheet sheet = workbook.getSheetAt(0);

        short format = workbook.getCreationHelper().createDataFormat().getFormat("dd/mm/yyyy hh:mm:ss");
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(format);

        LocalDate fromDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate toDate = fromDate.with(TemporalAdjusters.lastDayOfMonth());
        Organization organizationByUnitCode = organizationRepository.findOrganizationByUnitCode(doctorPayout.getUnitCode());
        UserMaster byEmployeeNo = userMasterRepository.findById(doctorPayout.getEmployeeId()).get();
        List<DepartmentMasterDTO> departmentList = userDepartmentMappingService.search("organization.id:" + organizationByUnitCode.getId() + " AND  userMaster.id:" + doctorPayout.getEmployeeId(), PageRequest.of(0, 1)).getContent().get(0).getDepartment();
        List<String> departMentList = departmentList.stream().map(department -> department.getName()).collect(Collectors.toList());
        Double totalFinalSum = 0d;
        if (doctorPayout != null && doctorPayout.getVariablePayoutId() != null) {
            variablePayout = variablePayoutRepository.findById(doctorPayout.getVariablePayoutId()).orElse(null);
        }
        LocalDateTime fromDateTime = fromDate.atTime(00, 00, 00);
        LocalDateTime toDateTime = toDate.atTime(23, 59, 59);

        sheet.getRow(1).getCell(2).setCellStyle(cellStyle);
        sheet.getRow(1).getCell(2).setCellValue(Date.from(fromDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        sheet.getRow(1).getCell(6).setCellValue(byEmployeeNo.getDisplayName());

        sheet.getRow(2).getCell(2).setCellStyle(cellStyle);
        sheet.getRow(2).getCell(2).setCellValue(Date.from(toDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        sheet.getRow(2).getCell(6).setCellValue(departMentList.toString().subSequence(1, departMentList.toString().length() - 1).toString());

        sheet.getRow(3).getCell(2).setCellValue(organizationByUnitCode.getName());
        sheet.getRow(3).getCell(6).setCellStyle(cellStyle);
        if (variablePayout != null && variablePayout.getMinAssuredValidityDate() != null) {
            sheet.getRow(3).getCell(6).setCellValue(Date.from(variablePayout.getMinAssuredValidityDate().atZone(ZoneId.systemDefault()).toInstant()));
        }

        Map<String, Double> serviceItemBenefietRow = createServiceItemBenefietRow(workbook, sheet, doctorPayout);
        totalFinalSum += serviceItemBenefietRow.getOrDefault("sum", 0d);
        int serviceItemBenefitRow = serviceItemBenefietRow.get("index").intValue();
        int count = serviceItemBenefitRow + 4;
        Map<String, Double> departmentRowSumIndex = createDepartmentRow(workbook, sheet, doctorPayout, count);
        totalFinalSum += departmentRowSumIndex.getOrDefault("sum", 0d);
        int departmentRow = departmentRowSumIndex.get("index").intValue();
        Map<String, Double> losRowBreakupIndexSumMap = createLosRowBreakup(workbook, sheet, doctorPayout, departmentRow + 4);
        totalFinalSum += losRowBreakupIndexSumMap.getOrDefault("sum", 0d);
        int losRowBreakup = losRowBreakupIndexSumMap.get("index").intValue();
        int payoutRow = losRowBreakup + 5;
        if (variablePayout != null && variablePayout.getMinAssuredAmount() != null) {
            if (sheet.getRow(losRowBreakup + 2) != null) {
                sheet.getRow(losRowBreakup + 2).getCell(6).setCellValue(variablePayout.getMinAssuredAmount().doubleValue());
            }
        }
        Map<String, Double> serviceItemRowForPayoutAdjustment = createServiceItemRowForPayoutAdjustment(workbook, sheet, doctorPayout, payoutRow);
        totalFinalSum += serviceItemRowForPayoutAdjustment.getOrDefault("sum", 0d);
        totalFinalSum+=variablePayout.getMinAssuredAmount().doubleValue();
        int serviceItemRowForPayoutAdjustmentRowNum = serviceItemRowForPayoutAdjustment.get("index").intValue();

        if (sheet.getRow(serviceItemRowForPayoutAdjustmentRowNum) != null && sheet.getRow(serviceItemRowForPayoutAdjustmentRowNum).getCell(5) != null && sheet.getRow(serviceItemRowForPayoutAdjustmentRowNum).getCell(5).toString().equalsIgnoreCase("Total")) {
            sheet.getRow(serviceItemRowForPayoutAdjustmentRowNum + 2).getCell(6).setCellValue(totalFinalSum);
        }
        if (variablePayout != null && variablePayout.getMaxPayoutAmount() != null) {
            if (sheet.getRow(serviceItemRowForPayoutAdjustmentRowNum + 4) != null && sheet.getRow(serviceItemRowForPayoutAdjustmentRowNum + 4).getCell(1) != null) {
                sheet.getRow(serviceItemRowForPayoutAdjustmentRowNum + 4).getCell(6).setCellValue(variablePayout.getMaxPayoutAmount().doubleValue());
            } else if (sheet.getRow(serviceItemRowForPayoutAdjustmentRowNum + 5) != null && sheet.getRow(serviceItemRowForPayoutAdjustmentRowNum + 5).getCell(1) != null) {
                sheet.getRow(serviceItemRowForPayoutAdjustmentRowNum + 5).getCell(6).setCellValue(variablePayout.getMaxPayoutAmount().doubleValue());
            }
        }
        if (sheet.getRow(serviceItemRowForPayoutAdjustmentRowNum + 6) != null) {
            if (totalFinalSum < variablePayout.getMaxPayoutAmount().doubleValue()) {
                sheet.getRow(serviceItemRowForPayoutAdjustmentRowNum + 6).getCell(5).setCellValue(totalFinalSum);
            } else {
                sheet.getRow(serviceItemRowForPayoutAdjustmentRowNum + 6).getCell(5).setCellValue(variablePayout.getMaxPayoutAmount()!=null?variablePayout.getMaxPayoutAmount().doubleValue():0);
            }
        }
        int amountPayableIndex = serviceItemRowForPayoutAdjustmentRowNum + 6;
        if (sheet.getRow(amountPayableIndex + 5) != null) {
            String displayName = userMasterRepository.findByEmployeeNo(UserPreferencesUtils.getCurrentUserPreferences().getUser().getLogin()).getDisplayName();
            sheet.getRow(amountPayableIndex + 5).getCell(2).setCellValue(displayName);
            sheet.getRow(amountPayableIndex + 5).getCell(6).setCellValue(LocalDate.now().toString());
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        returnObject.put("path", file.getCanonicalPath());
        returnObject.put("name", file.getName());
        returnObject.put("contentType", Files.probeContentType(file.toPath()));
        System.out.println("Content Type :: " + Files.probeContentType(file.toPath()));
        return returnObject;
    }


    public static XSSFWorkbook geDataFromFile(String name, String fileLocation) {
        XSSFWorkbook workBook = null;
        if (null != fileLocation) {
            String filePath = fileLocation + "/" + name;
            try {
                System.out.println("FILE PATH LOCATION:::" + filePath);
                InputStream ExcelFileToRead = new FileInputStream(filePath);
                workBook = new XSSFWorkbook(ExcelFileToRead);
                return workBook;
            } catch (Exception fileNotFound) {
                fileNotFound.printStackTrace();
            }

        } else {
            return workBook;
        }
        return workBook;
    }

    public static File getXLSXExportFile(String featureName, Long code, String parentPath) {
        StringBuilder fileName = new StringBuilder(featureName)
            .append("_")
            .append(SecurityUtils.getCurrentUserLogin())
            .append("_")
            .append(code)
            .append(".xlsx");
        File file = new File(parentPath + fileName);
        System.out.println("FILE PATH LOCATION :::" + file.getAbsolutePath());
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    private void CopyRow(XSSFWorkbook workbook, XSSFSheet worksheet, int sourceRowNum, int destinationRowNum, int numberOfRowToCreate) {
        // Get the source / new row
        XSSFRow newRow = worksheet.getRow(destinationRowNum);
        XSSFRow sourceRow = worksheet.getRow(sourceRowNum);

        // If the row exist in destination, push down all rows by 1 else create a new row
        if (newRow != null) {
            worksheet.shiftRows(destinationRowNum, 100000, numberOfRowToCreate);

        }
        // Loop through source columns to add to new row
        int rowCreateionNumber = destinationRowNum;
        while (numberOfRowToCreate > 0) {
            newRow = worksheet.getRow(rowCreateionNumber);
            if (newRow == null) {
                newRow = worksheet.createRow(rowCreateionNumber);
            }
            for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
                // Grab a copy of the old/new cell
                XSSFCell oldCell = sourceRow.getCell(i);
                XSSFCell newCell = newRow.createCell(i);

                // If the old cell is null jump to next cell
                if (oldCell == null) {
                    newCell = null;
                    continue;
                }
                // Copy style from old cell and apply to new cell
                XSSFCellStyle newCellStyle = workbook.createCellStyle();
                newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
                newCell.setCellStyle(newCellStyle);
                // If there is a cell comment, copy
                if (oldCell.getCellComment() != null) {
                    newCell.setCellComment(oldCell.getCellComment());
                }

                // If there is a cell hyperlink, copy
                if (oldCell.getHyperlink() != null) {
                    newCell.setHyperlink(oldCell.getHyperlink());
                }
                // Set the cell data type
                newCell.setCellType(oldCell.getCellType());
                // Set the cell data value
                switch (oldCell.getCellType()) {
                    case Cell.CELL_TYPE_BLANK:
                        newCell.setCellValue(oldCell.getStringCellValue());
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        newCell.setCellValue(oldCell.getBooleanCellValue());
                        break;
                    case Cell.CELL_TYPE_ERROR:
                        newCell.setCellErrorValue(oldCell.getErrorCellValue());
                        break;
                    case Cell.CELL_TYPE_FORMULA:
                        newCell.setCellFormula(oldCell.getCellFormula());
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        newCell.setCellValue(oldCell.getNumericCellValue());
                        break;
                    case Cell.CELL_TYPE_STRING:
                        newCell.setCellValue(oldCell.getRichStringCellValue());
                        break;
                }
            }
            numberOfRowToCreate--;
            rowCreateionNumber++;
        }


    }

    private Map<String, Double> createServiceItemBenefietRow(XSSFWorkbook workbook, XSSFSheet sheet, DoctorPayout doctorPayout) {
        Map<String, List<InvoiceDoctorPayout>> collect = doctorPayout.getDoctorPayoutInvoices().stream().
            collect(Collectors.groupingBy(invoiceDoctorPayout -> invoiceDoctorPayout.getDepartment() != null ? invoiceDoctorPayout.getDepartment().getName() : ""));
        Map<String, Double> calculatedSumAndIndexMap = new HashMap<>();
        Double totalSum = 0d;
        int count = 7;
        if (collect.size() > 1)
            CopyRow(workbook, sheet, count, count, collect.size() - 1);
        Set<Map.Entry<String, List<InvoiceDoctorPayout>>> entries = collect.entrySet();
        Iterator<Map.Entry<String, List<InvoiceDoctorPayout>>> iterator = entries.iterator();
        StringBuffer serviceItmeSumFormula = new StringBuffer();
        serviceItmeSumFormula.append("SUM(");
        while (iterator.hasNext()) {
            Map.Entry<String, List<InvoiceDoctorPayout>> next = iterator.next();
            List<InvoiceDoctorPayout> value = next.getValue();
            double sum = value.stream().mapToDouble(invoiceDoctorPayout -> invoiceDoctorPayout.getServiceBenefietAmount()).sum();
            totalSum += sum;
            if (sheet.getRow(count).getCell(1) != null) {
                if (departmentRepository.findDepartmentByCode(next.getKey().trim()) != null) {
                    sheet.getRow(count).getCell(1).setCellValue(departmentRepository.findDepartmentByCode(next.getKey()).getName());
                } else {
                    sheet.getRow(count).getCell(1).setCellValue(next.getKey());
                }
            } else {
                sheet.getRow(count).createCell(1).setCellValue(next.getKey());
            }
            if (sheet.getRow(count).getCell(6) != null) {
                sheet.getRow(count).getCell(6).setCellValue(sum);
            } else {
                sheet.getRow(count).createCell(6).setCellValue(sum);

            }
            serviceItmeSumFormula.append("G" + (count + 1));
            serviceItmeSumFormula.append(":");
            count++;

        }
        String forumala = serviceItmeSumFormula.subSequence(0, serviceItmeSumFormula.toString().length() - 1).toString() + ")";
        if (!serviceItmeSumFormula.toString().equalsIgnoreCase("SUM(")) {
            sheet.getRow(count).getCell(6).setCellFormula(forumala);
            sheet.getRow(count).getCell(6).setCellValue(totalSum);
        } else {
            count++;
        }
        calculatedSumAndIndexMap.put("sum", totalSum);
        calculatedSumAndIndexMap.put("index", Double.valueOf(count));
        return calculatedSumAndIndexMap;
    }

    private Map<String, Double> createDepartmentRow(XSSFWorkbook workbook, XSSFSheet sheet, DoctorPayout doctorPayout, int count) {
        String departmentRevenueTotalFormula = "";
        Map<String, Double> departmentRevenueSumAndIndexMap = new HashMap<>();
        Double tolalSum = 0d;
        List<DoctorPayoutDepartment> departmentRevenueList = doctorPayout.getDepartmentRevenueBenefitIds().stream().collect(Collectors.toList());
        if (departmentRevenueList != null && !departmentRevenueList.isEmpty()) {
            StringBuffer departmentSumFormula = new StringBuffer();
            if (departmentRevenueList.size() > 1)
                CopyRow(workbook, sheet, count, count, departmentRevenueList.size() - 1);
            departmentSumFormula.append("SUM(");
            for (int i = 0; i < departmentRevenueList.size(); i++) {
                DoctorPayoutDepartment doctorPayoutDepartment = departmentRevenueList.get(i);
                tolalSum += doctorPayoutDepartment.getAmount();
                if (sheet.getRow(count).getCell(1) != null) {
                    sheet.getRow(count).getCell(1).setCellValue(doctorPayoutDepartment.getDepartment().getName());
                } else {
                    sheet.getRow(count).createCell(1).setCellValue(doctorPayoutDepartment.getDepartment().getName());
                }
                if (sheet.getRow(count).getCell(6) != null) {
                    sheet.getRow(count).getCell(6).setCellValue(doctorPayoutDepartment.getAmount());
                } else {
                    sheet.getRow(count).createCell(6).setCellValue(doctorPayoutDepartment.getAmount());

                }
                departmentSumFormula.append("G" + (count + 1));
                departmentSumFormula.append(":");
                count++;
            }
            departmentRevenueTotalFormula = departmentSumFormula.subSequence(0, departmentSumFormula.toString().length() - 1).toString() + ")";
            if (!departmentSumFormula.toString().equalsIgnoreCase("SUM(")) {
                sheet.getRow(count).getCell(6).setCellFormula(departmentRevenueTotalFormula);
                sheet.getRow(count).getCell(6).setCellValue(tolalSum);
            }
        } else {
            count++;
        }
        departmentRevenueSumAndIndexMap.put("sum", tolalSum);
        departmentRevenueSumAndIndexMap.put("index", Double.valueOf(count));
        return departmentRevenueSumAndIndexMap;
    }

    @Override
    public Map<String, Object> exportDoctorPayoutBreakup(String query, Pageable pageable) throws Exception {
        Map<String, Object> returnObject = new HashMap<>();
        Iterable<DoctorPayout> search = doctorPayoutSearchRepository.search(queryStringQuery(query));
        List<DoctorPayout> collect = StreamSupport.stream(search.spliterator(), false)
            .collect(Collectors.toList());
        String[] split = query.split("AND");
        Map<String, String> queryMap = new HashMap<>();
        for (int i = 0; i < split.length; i++) {
            queryMap.put(split[i].split(":")[0].trim(), split[i].split(":")[1].trim());
        }
        String unitCode = queryMap.get("unitCode");
        Integer year = Integer.parseInt(queryMap.get("year"));
        Integer month = Integer.parseInt(queryMap.get("month"));
        Long employeeId = Long.valueOf(queryMap.get("employeeId"));
        Long latestApprovedVariablePayoutId = variablePayoutRepository.getLatestApprovedVariablePayoutId(queryMap.get("unitCode"), Long.valueOf(queryMap.get("employeeId")));
        if (((PageImpl) search).getContent().size() <= 0) {
            if (latestApprovedVariablePayoutId == null) {
                throw new CustomParameterizedException("10395", "Doctor Payout Breakup Summary Is Not Available For The Selected Employee");
            } else {
                collect.add(saveDoctorPayout(year, month, employeeId, unitCode,null));
            }
        }
        VariablePayout variablePayout = variablePayoutRepository.findById(latestApprovedVariablePayoutId).get();
        LocalDateTime minDate = LocalDateTime.of(year, month, 1,0,0,0).with(TemporalAdjusters.firstDayOfMonth());
        LocalDateTime maxDate = LocalDateTime.of(minDate.with(TemporalAdjusters.lastDayOfMonth()).toLocalDate(), LocalTime.MIDNIGHT);
        Boolean flag=validateCommencementAndContractEndDate(variablePayout,minDate,maxDate);
        if(!flag){
            throw new CustomParameterizedException("100020","CommencementDate is greater than Matching with selected month");
        }
        File file = getXLSXExportFile("Doctor Payout Breakup", System.currentTimeMillis(), applicationProperties.getAthmaBucket().getTempExport());
        collect.forEach(doctorPayout ->
        {
            Boolean modifiedRule = false;
            if (doctorPayout.getVariablePayoutId() != null && !doctorPayout.getVariablePayoutId().equals(latestApprovedVariablePayoutId)) {
                modifiedRule = true;
            }
            if (doctorPayout.getVariablePayoutId() == null && latestApprovedVariablePayoutId != null) {
                modifiedRule = true;
            }
            if (modifiedRule) {
                doctorPayout = variablePayoutServiceAnalysisLoading.executeVariablePayoutByYearMonthAndID(unitCode, year, month, employeeId, latestApprovedVariablePayoutId, doctorPayout);
                deleteOldLosValue(year, month, employeeId, unitCode, doctorPayout);
                Map<LengthOfStayBenefit, List<ServiceAnalysisStaging>> patientBasedOnLosBenefit = lengthOfStayBenefitService.findPatientBasedOnLosBenefit(variablePayout);
                List<DoctorPayoutLOS> doctorPayoutLOSList = doctorPayoutLOSService.calculateDoctorPayoutLos(patientBasedOnLosBenefit, doctorPayout);
                if (doctorPayoutLOSList != null && !doctorPayoutLOSList.isEmpty())
                    doctorPayout.setLosBenefietIds(doctorPayoutLOSList.stream().collect(Collectors.toSet()));
                doctorPayout.setVariablePayoutId(latestApprovedVariablePayoutId);
                log.debug("Doctor Payout :: {} ",doctorPayout);
                saveDoctorPayout(year,month,employeeId,unitCode,doctorPayout);
            }
            doctorPayout = doctorPayoutRepository.findById(doctorPayout.getId()).orElse(null);
            XSSFWorkbook workbook = geDataFromFile("Doctor Payout Breakup.xlsx", applicationProperties.getAthmaBucket().getTemplate());
            XSSFSheet sheet = workbook.getSheetAt(0);

            short format = workbook.getCreationHelper().createDataFormat().getFormat("dd/mm/yyyy hh:mm:ss");
            XSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setDataFormat(format);

            LocalDate fromDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.firstDayOfMonth());
            LocalDate toDate = fromDate.with(TemporalAdjusters.lastDayOfMonth());
            Organization organizationByUnitCode = organizationRepository.findOrganizationByUnitCode(doctorPayout.getUnitCode());
            UserMaster byEmployeeNo = userMasterRepository.findById(doctorPayout.getEmployeeId()).get();
            List<DepartmentMasterDTO> departmentList = userDepartmentMappingService.search("organization.id:" + organizationByUnitCode.getId() + " AND  userMaster.id:" + doctorPayout.getEmployeeId(), PageRequest.of(0, 1)).getContent().get(0).getDepartment();
            List<String> departmentNameList = new ArrayList<>();
            for (DepartmentMasterDTO dept : departmentList) {
                departmentNameList.add(dept.getName());
            }
            Double totalfinalSum = 0d;
            LocalDateTime fromDateTime = fromDate.atTime(00, 00, 00);
            LocalDateTime toDateTime = toDate.atTime(23, 59, 59);
            sheet.getRow(1).getCell(2).setCellStyle(cellStyle);
            sheet.getRow(1).getCell(2).setCellValue(Date.from(fromDateTime.atZone(ZoneId.systemDefault()).toInstant()));
            sheet.getRow(1).getCell(6).setCellValue(byEmployeeNo.getDisplayName());

            sheet.getRow(2).getCell(2).setCellStyle(cellStyle);
            sheet.getRow(2).getCell(2).setCellValue(Date.from(toDateTime.atZone(ZoneId.systemDefault()).toInstant()));
            sheet.getRow(2).getCell(6).setCellValue(departmentNameList.toString().subSequence(1, departmentNameList.toString().length() - 1).toString());

            sheet.getRow(3).getCell(2).setCellValue(organizationByUnitCode.getName());
            sheet.getRow(3).getCell(6).setCellStyle(cellStyle);
            if (variablePayout != null && variablePayout.getMinAssuredValidityDate() != null) {
                sheet.getRow(3).getCell(6).setCellValue(Date.from(variablePayout.getMinAssuredValidityDate().atZone(ZoneId.systemDefault()).toInstant()));
            }
            Map<String, Double> serviceItemRowForDoctorBreakupIndexSumMap = createServiceItemRowForDoctorBreakup(workbook, sheet, doctorPayout);
            totalfinalSum += serviceItemRowForDoctorBreakupIndexSumMap.getOrDefault("sum", 0d);
            int serviceItemBenefitRow = serviceItemRowForDoctorBreakupIndexSumMap.get("index").intValue();
            int count = serviceItemBenefitRow + 4;
            Map<String, Double> departmentRowBreakupIndexSumMap = createDepartmentRowBreakup(workbook, sheet, doctorPayout, count);
            totalfinalSum += departmentRowBreakupIndexSumMap.getOrDefault("sum", 0d);
            int departmentRow = departmentRowBreakupIndexSumMap.get("index").intValue();
            Map<String, Double> losRowBreakupIndexSumMap = createLosRowBreakup(workbook, sheet, doctorPayout, departmentRow + 4);
            totalfinalSum += losRowBreakupIndexSumMap.getOrDefault("sum", 0d);
            int losRowBreakup = (int) losRowBreakupIndexSumMap.get("index").intValue();
            int payoutRow = losRowBreakup + 5;
            if (variablePayout != null && variablePayout.getMinAssuredAmount() != null) {
                if (sheet.getRow(losRowBreakup + 2) != null) {
                    totalfinalSum += variablePayout.getMinAssuredAmount().doubleValue();
                    sheet.getRow(losRowBreakup + 2).getCell(6).setCellValue(variablePayout.getMinAssuredAmount().doubleValue());
                }
            }
            Map<String, Double> serviceItemRowForPayoutAdjustment = createServiceItemRowForPayoutAdjustment(workbook, sheet, doctorPayout, payoutRow);
            totalfinalSum += serviceItemRowForPayoutAdjustment.getOrDefault("sum", 0d);
            int serviceItemRowForPayoutAdjustmentRowNum = serviceItemRowForPayoutAdjustment.get("index").intValue();
            StringBuffer totalSum = new StringBuffer();
            totalSum.append("SUM(");
            //totalSum.append(":G" + (losRowBreakup+2));
            totalfinalSum+=variablePayout.getMinAssuredAmount().doubleValue();
            if (sheet.getRow(serviceItemRowForPayoutAdjustmentRowNum) != null && sheet.getRow(serviceItemRowForPayoutAdjustmentRowNum).getCell(5) != null && sheet.getRow(serviceItemRowForPayoutAdjustmentRowNum).getCell(5).toString().equalsIgnoreCase("Total")) {
                totalSum.append("G" + (serviceItemRowForPayoutAdjustmentRowNum + 1) + ")");
                sheet.getRow(serviceItemRowForPayoutAdjustmentRowNum + 2).getCell(6).setCellValue(totalfinalSum);
            }
            if (variablePayout != null && variablePayout.getMaxPayoutAmount() != null) {
                if (sheet.getRow(serviceItemRowForPayoutAdjustmentRowNum + 4) != null && sheet.getRow(serviceItemRowForPayoutAdjustmentRowNum + 4).getCell(1) != null) {
                    sheet.getRow(serviceItemRowForPayoutAdjustmentRowNum + 4).getCell(6).setCellValue(variablePayout.getMaxPayoutAmount().doubleValue());
                } else if (sheet.getRow(serviceItemRowForPayoutAdjustmentRowNum + 5) != null && sheet.getRow(serviceItemRowForPayoutAdjustmentRowNum + 5).getCell(1) != null) {
                    sheet.getRow(serviceItemRowForPayoutAdjustmentRowNum + 5).getCell(6).setCellValue(variablePayout.getMaxPayoutAmount().doubleValue());
                }
            }
            if (sheet.getRow(serviceItemRowForPayoutAdjustmentRowNum + 6) != null) {
                if (totalfinalSum != null && totalfinalSum < variablePayout.getMaxPayoutAmount().doubleValue()) {
                    sheet.getRow(serviceItemRowForPayoutAdjustmentRowNum + 6).getCell(5).setCellValue(totalfinalSum);
                } else {
                    sheet.getRow(serviceItemRowForPayoutAdjustmentRowNum + 6).getCell(5).setCellValue(variablePayout.getMaxPayoutAmount()!=null?variablePayout.getMaxPayoutAmount().doubleValue():0);
                }
            }
            int amountPayableIndex = serviceItemRowForPayoutAdjustmentRowNum + 6;
            if (sheet.getRow(amountPayableIndex + 5) != null) {
                String displayName = userMasterRepository.findByEmployeeNo(UserPreferencesUtils.getCurrentUserPreferences().getUser().getLogin()).getDisplayName();
                sheet.getRow(amountPayableIndex + 5).getCell(2).setCellValue(displayName);
                sheet.getRow(amountPayableIndex + 5).getCell(6).setCellValue(LocalDate.now().toString());
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                workbook.write(fileOutputStream);
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        returnObject.put("path", file.getCanonicalPath());
        returnObject.put("name", file.getName());
        returnObject.put("contentType", Files.probeContentType(file.toPath()));
        System.out.println("Content Type :: " + Files.probeContentType(file.toPath()));
        return returnObject;
    }

    @Override
    @Transactional
    public List<DoctorPayout> saveAll(List<DoctorPayout> doctorPayout) {
        List<DoctorPayout> doctorPayoutList = doctorPayoutRepository.saveAll(doctorPayout);
        doctorPayoutSearchRepository.saveAll(doctorPayoutList);
        return doctorPayoutList;
    }

    private Map<String, Double> createServiceItemRowForDoctorBreakup(XSSFWorkbook workbook, XSSFSheet sheet, DoctorPayout doctorPayout) {
        List<InvoiceDoctorPayout> collect = doctorPayout.getDoctorPayoutInvoices().stream().collect(Collectors.toList());
        Map<String, Double> serviceItmeSumIndexMap = new HashMap<>();
        Double totalSum = 0d;
        String serviceItembenfietFormula = "";
        StringBuffer serviceItemFormula = new StringBuffer();
        int count = 7;
        if (collect.size() > 1)
            CopyRow(workbook, sheet, count, count, collect.size() - 1);
        serviceItemFormula.append("SUM(");
        for (int i = 0; i < collect.size(); i++) {
            InvoiceDoctorPayout invoiceDoctorPayout = collect.get(i);
            totalSum += invoiceDoctorPayout.getServiceBenefietAmount();
            LocalDate toLocalDate = invoiceDoctorPayout.getIssueDateTime().atZone(ZoneId.systemDefault()).toLocalDate();
            sheet.getRow(count).getCell(1).setCellValue(toLocalDate.format(DateTimeFormatter.ISO_DATE));
            sheet.getRow(count).getCell(2).setCellValue(invoiceDoctorPayout.getDepartment() != null ? invoiceDoctorPayout.getDepartment().getName() : "");
            sheet.getRow(count).getCell(3).setCellValue(invoiceDoctorPayout.getMrn());
            sheet.getRow(count).getCell(4).setCellValue(invoiceDoctorPayout.getName());
            sheet.getRow(count).getCell(6).setCellValue(invoiceDoctorPayout.getServiceBenefietAmount());
            serviceItemFormula.append("G" + (count + 1));
            serviceItemFormula.append(":");
            count++;
        }
        serviceItembenfietFormula = serviceItemFormula.subSequence(0, serviceItemFormula.toString().length() - 1).toString() + ")";
        if (!serviceItemFormula.toString().equalsIgnoreCase("SUM(")) {
            sheet.getRow(count).getCell(6).setCellFormula(serviceItembenfietFormula);
            sheet.getRow(count).getCell(6).setCellValue(totalSum);
        } else if (serviceItemFormula.toString().equalsIgnoreCase("SUM(")) {
            count++; //added if there is no row for service but adjustment there are then format are breaking;
        }
        serviceItmeSumIndexMap.put("sum", totalSum);
        serviceItmeSumIndexMap.put("index", Double.valueOf(count));
        return serviceItmeSumIndexMap;
    }

    private Map<String, Double> createServiceItemRowForPayoutAdjustment(XSSFWorkbook workbook, XSSFSheet sheet, DoctorPayout doctorPayout, int count) {
        List<DoctorPayoutAdjustment> collect = doctorPayout.getDoctorPayoutAdjustments().stream().collect(Collectors.toList());
        Map<String, Double> payoutAdjustmentSumIndexMap = new HashMap<>();
        Double totalSum = 0d;
        StringBuffer payoutAdjustmentFormula = new StringBuffer();
        if (collect.size() > 1)
            CopyRow(workbook, sheet, count, count, collect.size() - 1);
        payoutAdjustmentFormula.append("SUM(");
        for (int i = 0; i < collect.size(); i++) {
            DoctorPayoutAdjustment doctorPayoutAdjustment = collect.get(i);
            Long adjustmentConfigurationId = doctorPayoutAdjustment.getAdjustmentConfigurationId();
            PayoutAdjustmentDetails payoutAdjustmentDetails = payoutAdjustmentDetailsRepository.findById(adjustmentConfigurationId).get();
            if (payoutAdjustmentDetails.getContraEmployeeDetail() == null) {
                sheet.getRow(count).getCell(1).setCellValue(payoutAdjustmentDetails.getDescription() + "(" + payoutAdjustmentDetails.getYear() + " , " + payoutAdjustmentDetails.getMonth() + ")");
            } else {
                sheet.getRow(count).getCell(1).setCellValue(payoutAdjustmentDetails.getDescription() + "(Contra - " + payoutAdjustmentDetails.getContraEmployeeDetail().getDisplayName() + " " + payoutAdjustmentDetails.getYear() + " , " + payoutAdjustmentDetails.getMonth() + ")");
            }
            if (payoutAdjustmentDetails.getSign().equalsIgnoreCase("positive")) {
                totalSum += payoutAdjustmentDetails.getAmount().doubleValue();
                sheet.getRow(count).getCell(6).setCellValue(payoutAdjustmentDetails.getAmount().doubleValue());

            } else if (payoutAdjustmentDetails.getSign().equalsIgnoreCase("negative")) {
                totalSum += 0 - (payoutAdjustmentDetails.getAmount().doubleValue());
                sheet.getRow(count).getCell(6).setCellValue(0 - (payoutAdjustmentDetails.getAmount().doubleValue()));
            }
            payoutAdjustmentFormula.append("G" + (count + 1));
            payoutAdjustmentFormula.append(":");
            count++;
        }
        if (!payoutAdjustmentFormula.toString().equalsIgnoreCase("SUM(")) {
            String payoutAdjustmentFinalFormula = payoutAdjustmentFormula.subSequence(0, payoutAdjustmentFormula.toString().length() - 1).toString() + ")";
            sheet.getRow(count).getCell(6).setCellFormula(payoutAdjustmentFinalFormula);
            sheet.getRow(count).getCell(6).setCellValue(totalSum);
        } else if (payoutAdjustmentFormula.toString().equalsIgnoreCase("SUM(")) {
            count++;//added if there is no row for adjustment  format are breaking;
        }
        payoutAdjustmentSumIndexMap.put("sum", totalSum);
        payoutAdjustmentSumIndexMap.put("index", Double.valueOf(count));
        return payoutAdjustmentSumIndexMap;

    }

    private Map<String, Double> createDepartmentRowBreakup(XSSFWorkbook workbook, XSSFSheet sheet, DoctorPayout doctorPayout, int count) {
        Map<String, Double> departmentRowIndexSumMap = new HashMap<>();
        Double totalSum = 0d;
        LocalDate fromDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate toDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        List<DoctorPayoutDepartment> departmentRevenueList = doctorPayout.getDepartmentRevenueBenefitIds().stream().collect(Collectors.toList());
        StringBuffer dateRange = new StringBuffer();
        dateRange.append(fromDate.getDayOfMonth() + "-" + fromDate.getMonthValue() + "-" + fromDate.getDayOfYear() + " to " + toDate.getDayOfMonth() + "-" + toDate.getMonthValue() + "-" + toDate.getDayOfYear());
        StringBuffer departmentSumFormula = new StringBuffer();
        departmentSumFormula.append("SUM(");
        if (departmentRevenueList != null && !departmentRevenueList.isEmpty()) {
            if (departmentRevenueList.size() > 1)
                CopyRow(workbook, sheet, count, count, departmentRevenueList.size() - 1);
            for (int i = 0; i < departmentRevenueList.size(); i++) {
                DoctorPayoutDepartment doctorPayoutDepartment = departmentRevenueList.get(i);
                totalSum += doctorPayoutDepartment.getAmount();
                if (sheet.getRow(count).getCell(1) != null) {
                    sheet.getRow(count).getCell(1).setCellValue(dateRange.toString());
                }
                if (sheet.getRow(count).getCell(2) != null) {
                    sheet.getRow(count).getCell(2).setCellValue(doctorPayoutDepartment.getDepartment().getName());
                } else {
                    sheet.getRow(count).createCell(2).setCellValue(doctorPayoutDepartment.getDepartment().getName());
                }
                if (sheet.getRow(count).getCell(6) != null) {
                    sheet.getRow(count).getCell(6).setCellValue(doctorPayoutDepartment.getAmount());
                } else {
                    sheet.getRow(count).createCell(6).setCellValue(doctorPayoutDepartment.getAmount());

                }
                departmentSumFormula.append("G" + (count + 1));
                departmentSumFormula.append(":");
                count++;
            }

        }
        if (!departmentSumFormula.toString().equalsIgnoreCase("SUM(")) {
            String departmentRevenueTotalFormula = departmentSumFormula.subSequence(0, departmentSumFormula.toString().length() - 1).toString() + ")";
            sheet.getRow(count).getCell(6).setCellFormula(departmentRevenueTotalFormula);
            sheet.getRow(count).getCell(6).setCellValue(totalSum);
        } else {
            count++;
        }
        departmentRowIndexSumMap.put("sum", totalSum);
        departmentRowIndexSumMap.put("index", Double.valueOf(count));
        return departmentRowIndexSumMap;
    }

    private Map<String, Double> createLosRowBreakup(XSSFWorkbook workbook, XSSFSheet sheet, DoctorPayout doctorPayout, int count) {
        String losBenefietTotalFormula = "";
        LocalDate fromDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate toDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        List<DoctorPayoutLOS> losBenefietList = doctorPayout.getLosBenefietIds().stream().collect(Collectors.toList());
        Map<String, Double> losBreakupSumIndexMap = new HashMap<>();
        Double totalSum = 0d;
        StringBuffer dateRange = new StringBuffer();
        dateRange.append(fromDate.getDayOfMonth() + "-" + fromDate.getMonthValue() + "-" + fromDate.getDayOfYear() + " to " + toDate.getDayOfMonth() + "-" + toDate.getMonthValue() + "-" + toDate.getDayOfYear());
        StringBuffer departmentSumFormula = new StringBuffer();
        if (losBenefietList != null && !losBenefietList.isEmpty()) {
            if (losBenefietList.size() > 1)
                CopyRow(workbook, sheet, count, count, losBenefietList.size() - 1);
            departmentSumFormula.append("SUM(");
            for (int i = 0; i < losBenefietList.size(); i++) {
                DoctorPayoutLOS doctorPayoutLOS = losBenefietList.get(i);
                totalSum += doctorPayoutLOS.getAmount();
                if (sheet.getRow(count).getCell(1) != null) {
                    sheet.getRow(count).getCell(1).setCellValue(Date.from(doctorPayoutLOS.getDischargeDateTime().atZone(ZoneId.systemDefault()).toInstant()));
                }
                if (sheet.getRow(count).getCell(2) != null) {
                    sheet.getRow(count).getCell(2).setCellValue(doctorPayoutLOS.getDepartment().getName());
                } else {
                    sheet.getRow(count).createCell(2).setCellValue(doctorPayoutLOS.getDepartment().getName());
                }

                if (sheet.getRow(count).getCell(3) != null) {
                    sheet.getRow(count).getCell(3).setCellValue(doctorPayoutLOS.getPatientMrn());
                } else {
                    sheet.getRow(count).createCell(3).setCellValue(doctorPayoutLOS.getPatientMrn());
                }

                if (sheet.getRow(count).getCell(4) != null) {
                    sheet.getRow(count).getCell(4).setCellValue(doctorPayoutLOS.getPayableDays());
                } else {
                    sheet.getRow(count).createCell(3).setCellValue(doctorPayoutLOS.getPayableDays());
                }

                if (sheet.getRow(count).getCell(6) != null) {
                    sheet.getRow(count).getCell(6).setCellValue(doctorPayoutLOS.getAmount());
                } else {
                    sheet.getRow(count).createCell(6).setCellValue(doctorPayoutLOS.getAmount());

                }
                departmentSumFormula.append("G" + (count + 1));
                departmentSumFormula.append(":");
                count++;
            }
        }
        if (departmentSumFormula != null && !departmentSumFormula.toString().isEmpty()) {
            losBenefietTotalFormula = departmentSumFormula.subSequence(0, departmentSumFormula.toString().length() - 1).toString() + ")";
            int sum = losBenefietList.stream().map(doctorPayoutLOS -> doctorPayoutLOS.getPayableDays()).collect(Collectors.toList()).stream().mapToInt(along -> along.intValue()).sum();
            sheet.getRow(count).getCell(4).setCellValue(sum);
            sheet.getRow(count).getCell(6).setCellFormula(losBenefietTotalFormula);
        } else {
            count++;
        }
        losBreakupSumIndexMap.put("sum", totalSum);
        losBreakupSumIndexMap.put("index", Double.valueOf(count));
        return losBreakupSumIndexMap;
    }

    @Override
    public Map<String, String> exportItemServiceWisePayoutBreakUp(String unitCode, Integer year, Integer month, MisReport misReport) throws IOException {
        File file = ExportUtil.getCSVExportFile("Item Service wise payout breakup" + "_" + misReport.getId(), applicationProperties.getAthmaBucket().getTempExport(), misReport);
        FileWriter invoiceFileWriter = new FileWriter(file);
        Map<String, String> fileDetails = new HashMap<>();
        fileDetails.put("fileName", file.getName());
        fileDetails.put("pathReference", "tempExport");
        final String[] invoiceFileHeader = {"MRN", "Patient Name", "Dept", "Service/Item Name", "Ordering Date", "Billed Service/Item Amount Gross", "Billed Service/Item Amount Net", "Total Amount", "No of Payout Doctors", "Payout Doctor (Amt)"};
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(System.lineSeparator()).withQuoteMode(QuoteMode.MINIMAL);
        try (CSVPrinter csvFilePrinter = new CSVPrinter(invoiceFileWriter, csvFileFormat)) {
            csvFilePrinter.printRecord(invoiceFileHeader);
            List<DoctorPayout> allDoctorByYearAndMonth = doctorPayoutRepository.getAllDoctorByYearAndMonth(year, month, unitCode);
            if (allDoctorByYearAndMonth == null || allDoctorByYearAndMonth.isEmpty()) {
                throw new CustomParameterizedException("1200", "No Service Breakup found for " + unitCode + "(" + year + " and " + month + " )");
            }
            List<Long> doctorPayoutIdList = allDoctorByYearAndMonth.stream().map(doctorPayout -> doctorPayout.getId()).collect(Collectors.toList());
            Map<Long, List<InvoiceDoctorPayout>> sarIdAndCalculatedInvoiceList = invoiceDoctorPayoutRepository.getAllInvoiceByDoctorPayout(doctorPayoutIdList).stream().collect(Collectors.groupingBy(InvoiceDoctorPayout::getSarId));
            sarIdAndCalculatedInvoiceList.forEach((sarId, invoiceDoctorPayoutList) -> {
                List<String> dataList = new ArrayList<>();
                Double sum = invoiceDoctorPayoutList.parallelStream().mapToDouble(InvoiceDoctorPayout::getServiceBenefietAmount).sum();
                String formattedDoctorString = invoiceDoctorPayoutList.parallelStream().map(invoiceDoctorPayout -> {
                    Long employeeId = invoiceDoctorPayout.getDoctorPayout().getEmployeeId();
                    UserMaster userMaster = userMasterRepository.findById(employeeId).orElse(null);
                    if (userMaster != null) {
                        return userMaster.getDisplayName() + " (" + (invoiceDoctorPayout.getServiceBenefietAmount()) + " )" + " , ";
                    } else {
                        return "";
                    }
                }).reduce(String::concat).get();
                invoiceDoctorPayoutList.stream().forEach(invoiceDoctorPayout -> {
                    ServiceAnalysisStaging serviceAnalysisStaging = serviceAnalysisRepository.findById(invoiceDoctorPayout.getSarId()).orElse(null);
                    dataList.add(invoiceDoctorPayout.getMrn());
                    dataList.add(serviceAnalysisStaging.getPatientName());
                    dataList.add(invoiceDoctorPayout.getDepartment() != null ? invoiceDoctorPayout.getDepartment().getName() : "");
                    dataList.add(invoiceDoctorPayout.getName());
                    dataList.add(invoiceDoctorPayout.getOrderDateTime().toString());
                    dataList.add(String.valueOf(serviceAnalysisStaging.getGrossAmount()));
                    dataList.add(String.valueOf(serviceAnalysisStaging.getNetAmount()));
                    dataList.add(String.valueOf(sum));
                    dataList.add(String.valueOf(invoiceDoctorPayoutList.size()));
                    if (formattedDoctorString != null && !formattedDoctorString.isEmpty() && formattedDoctorString.length() >= 2) {
                        dataList.add(formattedDoctorString.substring(0, formattedDoctorString.length() - 2));
                    } else {
                        dataList.add("");
                    }

                });
                try {
                    csvFilePrinter.printRecord(dataList);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return fileDetails;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public DoctorPayout saveDoctorPayout(Integer year, Integer month, Long employeeId, String unitCode,DoctorPayout doctorPayout1) {
        DoctorPayout doctorPayout = doctorPayoutRepository.getDoctorByYearAndMonth(employeeId, year, month);
        if (doctorPayout == null) {
            doctorPayout = new DoctorPayout();
            doctorPayout.setEmployeeId(employeeId);
            doctorPayout.setMonth(month);
            doctorPayout.setYear(year);
            doctorPayout.setunitCode(unitCode);
            doctorPayout = doctorPayoutService.save(doctorPayout);
        }else{
            doctorPayoutRepository.save(doctorPayout1);
            doctorPayoutSearchRepository.save(doctorPayout1);
        }
        return doctorPayout;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteOldLosValue(int year, int month, Long employeeId, String unitCode, DoctorPayout doctorPayout) {
        UserMaster userMaster = userMasterRepository.findById(employeeId).get();
        List<LengthOfStayBenefit> lengthOfStayBenefitList = lengthOfStayBenefitRepository.getLengthOfStayByVariablePayout(doctorPayout.getVariablePayoutId());
        lengthOfStayBenefitList.stream().forEach(lengthOfStayBenefit -> {
            Department department = lengthOfStayBenefit.getDepartment();
            List<String> stayBenefitServiceCode = lengthOfStayBenefit.getStayBenefitServices().stream().map(stayBenefitService -> stayBenefitService.getServiceCode()).collect(Collectors.toList());
            List<ServiceAnalysisStaging> matchMrnBasedOnStayBenefietServiceCriteria = serviceAnalysisRepository.findMrnBasedOnDepartmentAndServiceCodeExecuteOne(userMaster.getEmployeeNumber(), doctorPayout.getUnitCode(), department.getCode(), "IP", stayBenefitServiceCode).stream().collect(Collectors.toList());
            List<ServiceAnalysisStaging> updateSarForLos = matchMrnBasedOnStayBenefietServiceCriteria.stream().map(serviceAnalysisStaging -> {
                serviceAnalysisStaging.setLengthOfStayExecuted(false);
                return serviceAnalysisStaging;
            }).collect(Collectors.toList());
            serviceAnalysisRepository.saveAll(updateSarForLos);
        });
        doctorPayoutLOSRepository.getLengthOfStayByVariablePayout(doctorPayout.getId());
    }

    private boolean validateCommencementAndContractEndDate(VariablePayout variablePayout, LocalDateTime minDate, LocalDateTime maxDate){
        Boolean flag=false;
        if (variablePayout.getCommencementDate().getYear() == minDate.getYear() && variablePayout.getCommencementDate().getMonthValue() == minDate.getMonthValue()) {
            flag=true;
        } else if (variablePayout.getContractEndDate().getYear() >= minDate.getYear()) {
            if(variablePayout.getContractEndDate().getYear() == minDate.getYear() && variablePayout.getContractEndDate().getMonthValue() != minDate.getMonthValue()){
                flag=true;
            }
            else if(variablePayout.getContractEndDate().getYear() == minDate.getYear() && variablePayout.getContractEndDate().getMonthValue() != minDate.getMonthValue()){
                flag=true;
            }
            else if(variablePayout.getContractEndDate().getYear()>minDate.getYear()){
                flag=true;
            }
        }
        if(variablePayout.getContractEndDate().getYear() == minDate.getYear() && variablePayout.getContractEndDate().getMonthValue() == minDate.getMonthValue()) {
            flag=true;
        } else if (variablePayout.getContractEndDate().getYear() >= minDate.getYear()) {
            if(variablePayout.getContractEndDate().getYear()==minDate.getYear()&& variablePayout.getContractEndDate().getMonthValue() == minDate.getMonthValue()){
                flag=true;
            }else if(variablePayout.getContractEndDate().getYear()==minDate.getYear()&& variablePayout.getContractEndDate().getMonthValue() >= minDate.getMonthValue()){
                flag=true;
            }else if(variablePayout.getContractEndDate().getYear()>minDate.getYear()){
                flag=true;
            }
        }
        return flag;
    }
}
