package org.nh.artha.service.impl;



import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.nh.artha.security.UserPreferencesUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Primary
public class ArthaSequenceGeneratorService   {
//    private final Logger log = LoggerFactory.getLogger(ArthaSequenceGeneratorService.class);
//    private static final Long TOTALRECORDS = 50L;
//    private final SequenceFormatRepository seqFormatRepository;
//    private final SequenceStateRepository seqStateRepository;
//    private final SequenceExpressionRepository seqExpressionRepository;
//    private final SequenceNumberRepository sequenceNumberRepository;
//    public static ThreadLocal<Preferences> preferencesThreadLocal = new ThreadLocal();
//
//    public ArthaSequenceGeneratorService(SequenceFormatRepository seqFormatRepository, SequenceStateRepository seqStateRepository, SequenceExpressionRepository seqExpressionRepository, SequenceNumberRepository sequenceNumberRepository) {
//        this.seqFormatRepository = seqFormatRepository;
//        this.seqStateRepository = seqStateRepository;
//        this.seqExpressionRepository = seqExpressionRepository;
//        this.sequenceNumberRepository = sequenceNumberRepository;
//    }
//
//    @Transactional
//    public String generateSequence(String documentType, String organizationCode, Object source) throws SequenceGenerateException {
//        this.log.debug("Request to generate sequence");
//        return this.sequenceGenerater(documentType, organizationCode, source);
//    }
//
//    @Transactional(
//        propagation = Propagation.REQUIRES_NEW
//    )
//    public String generateNumber(String documentType, String organizationCode, Object source) throws SequenceGenerateException {
//        this.log.debug("Request to generate number");
//        return this.sequenceGenerater(documentType, organizationCode, source);
//    }
//
//    @Transactional(
//        propagation = Propagation.REQUIRES_NEW
//    )
//    private SequenceState createSequenceState(SequenceState sequenceState) {
//        this.log.debug("createSequenceState method starts");
//        return (SequenceState)this.seqStateRepository.save(sequenceState);
//    }
//
//    @Transactional(
//        propagation = Propagation.REQUIRES_NEW
//    )
//    public String getSequenceNumber(String documentType, String organizationCode, Object source) {
//        this.log.debug("Request to generate number");
//        return this.getSequenceGenerator(documentType, organizationCode, source);
//    }
//
//    private String getSequenceGenerator(String documentType, String organizationCode, Object source) throws SequenceGenerateException {
//        this.log.debug("Request to generate sequence number for documentType : {}, organizationCode : {}", documentType, organizationCode);
//        new StringBuilder();
//        SequenceFormat sequenceFormat = this.seqFormatRepository.findSequenceFormat(organizationCode, documentType);
//        this.log.debug("SequenceFormat : {}", sequenceFormat);
//        String resetExpression = sequenceFormat.getResetExpression();
//        String delimiter = sequenceFormat.getDelimiter();
//        String resetValue = this.getExpressionValue(resetExpression, delimiter, source);
//        SequenceState sequenceState = this.seqStateRepository.findSequenceState(sequenceFormat.getId(), resetValue);
//        this.log.debug("sequenceState : {}", sequenceState);
//        Long sequence = 1L;
//        if (sequenceState == null) {
//            synchronized(this) {
//                sequenceState = this.seqStateRepository.findSequenceState(sequenceFormat.getId(), resetValue);
//                if (sequenceState == null) {
//                    SequenceState seqState = new SequenceState();
//                    seqState.formatId(sequenceFormat.getId()).documentType(documentType).resetValue(resetValue).organizationCode(organizationCode).nextValue(sequence);
//                    String prefix = sequenceFormat.getPrefix();
//                    if (prefix != null && !prefix.isEmpty()) {
//                        seqState.setPrefix(this.getExpressionValue(prefix, delimiter, source));
//                    }
//
//                    String suffix = sequenceFormat.getSuffix();
//                    if (suffix != null && !suffix.isEmpty()) {
//                        seqState.setSuffix(this.getTagValue(suffix, source));
//                    }
//
//                    this.createSequenceState(seqState);
//                    sequenceState = this.seqStateRepository.findSequenceState(sequenceFormat.getId(), resetValue);
//                }
//            }
//        }
//
//        this.generateSequenceNumbers(sequenceState, sequenceFormat.getPadding());
//        List<SequenceNumber> sequenceNumberList = this.sequenceNumberRepository.findSequenceNumber(sequenceState.getId(), false, PageRequest.of(0, 1)).getContent();
//        if (CollectionUtils.isNotEmpty(sequenceNumberList)) {
//            SequenceNumber seqNumber = (SequenceNumber)sequenceNumberList.get(0);
//            seqNumber.setLocked(true);
//            seqNumber.setLockedDateTime(LocalDateTime.now());
//            this.sequenceNumberRepository.save(seqNumber);
//            this.log.debug("seqNumber value : {}", seqNumber.getSequenceNumber());
//            SeqNumberHandlerAspect.add(seqNumber.getSequenceNumber());
//            return seqNumber.getSequenceNumber();
//        } else {
//            return null;
//        }
//    }
//
//    private void generateSequenceNumbers(SequenceState sequenceState, Integer padding) {
//        this.log.debug("sequenceState Id : {}, padding value : {}", sequenceState.getId(), padding);
//        Long nextValue = sequenceState.getNextValue();
//        List<SequenceNumber> sequenceNumberList = new ArrayList();
//        Long totalRecordsInDB = this.sequenceNumberRepository.getTotalRecords(sequenceState.getId(), false);
//        long recordsNeedToInsert = TOTALRECORDS - totalRecordsInDB;
//
//        for(int i = 1; (long)i <= recordsNeedToInsert; ++i) {
//            StringBuilder seqNumber = new StringBuilder();
//            String paddedSequence = String.format("%0" + padding + "d", nextValue);
//            SequenceNumber sequenceNumber = new SequenceNumber();
//            if (sequenceState.getPrefix() != null && !sequenceState.getPrefix().isEmpty()) {
//                seqNumber.append(sequenceState.getPrefix());
//            }
//
//            seqNumber.append(paddedSequence);
//            if (sequenceState.getSuffix() != null && !sequenceState.getSuffix().isEmpty()) {
//                seqNumber.append(sequenceState.getSuffix());
//            }
//
//            sequenceNumber.setSequenceNumber(seqNumber.toString());
//            sequenceNumber.setLocked(false);
//            sequenceNumber.setCreatedDateTime(LocalDateTime.now());
//            sequenceNumber.setSequenceState(sequenceState);
//            sequenceNumberList.add(sequenceNumber);
//            nextValue = nextValue + 1L;
//        }
//
//        this.seqStateRepository.save(sequenceState.nextValue(nextValue));
//        this.sequenceNumberRepository.saveAll(sequenceNumberList);
//    }
//
//    public void updateProcessedDateTime(String seqNumber) {
//        SequenceNumber bySeqNumber = this.sequenceNumberRepository.findBySeqNumber(seqNumber);
//        if (bySeqNumber != null) {
//            bySeqNumber.setProcessedDateTime(LocalDateTime.now());
//            this.sequenceNumberRepository.save(bySeqNumber);
//        }
//
//    }
//
//    public void releaseSeqNumberLock(String seqNumber) {
//        SequenceNumber sequenceNumber = this.sequenceNumberRepository.findBySeqNumber(seqNumber);
//        if (sequenceNumber != null) {
//            sequenceNumber.setLocked(false);
//            this.sequenceNumberRepository.save(sequenceNumber);
//        }
//
//    }
//
//    private String sequenceGenerater(String documentType, String organizationCode, Object source) throws SequenceGenerateException {
//        this.log.debug("Request to generate sequence number for documentType : {}, organizationCode : {}", documentType, organizationCode);
//        StringBuilder sequenceNumber = new StringBuilder();
//        SequenceFormat sequenceFormat = this.seqFormatRepository.findSequenceFormat(organizationCode, documentType);
//        this.log.debug("SequenceFormat : {}", sequenceFormat);
//        String resetExpression = sequenceFormat.getResetExpression();
//        String delimiter = sequenceFormat.getDelimiter();
//        String resetValue = this.getExpressionValue(resetExpression, delimiter, source);
//        SequenceState sequenceState = this.seqStateRepository.findSequenceState(sequenceFormat.getId(), resetValue);
//        this.log.debug("sequenceState : {}", sequenceState);
//        Long sequence = 1L;
//        if (sequenceState == null) {
//            synchronized(this) {
//                sequenceState = this.seqStateRepository.findSequenceState(sequenceFormat.getId(), resetValue);
//                if (sequenceState == null) {
//                    SequenceState seqState = new SequenceState();
//                    seqState.formatId(sequenceFormat.getId()).documentType(documentType).resetValue(resetValue).organizationCode(organizationCode).nextValue(sequence);
//                    this.createSequenceState(seqState);
//                    sequenceState = this.seqStateRepository.findSequenceState(sequenceFormat.getId(), resetValue);
//                }
//            }
//        } else {
//            sequence = sequenceState.getNextValue();
//        }
//
//        String prefix = sequenceFormat.getPrefix();
//        if (prefix != null && !prefix.isEmpty()) {
//            sequenceNumber.append(this.getExpressionValue(prefix, delimiter, source));
//        }
//
//        String paddedSequence = String.format("%0" + sequenceFormat.getPadding() + "d", sequence);
//        sequenceNumber.append(paddedSequence);
//        String suffix = sequenceFormat.getSuffix();
//        if (suffix != null && !suffix.isEmpty()) {
//            sequenceNumber.append(this.getTagValue(suffix, source));
//        }
//
//        sequenceState.nextValue(sequence + 1L);
//        this.seqStateRepository.save(sequenceState);
//        return sequenceNumber.toString();
//    }
//
//    private String getExpressionValue(String resetExpression, String delimiter, Object source) throws SequenceGenerateException {
//        this.log.debug("Evaluating resetExpression : {} delimiter: {}", resetExpression, delimiter);
//        String[] resetExpTokens = resetExpression.split("\\" + delimiter.trim());
//        StringBuilder expressionValue = new StringBuilder();
//        String[] var6 = resetExpTokens;
//        int var7 = resetExpTokens.length;
//
//        for(int var8 = 0; var8 < var7; ++var8) {
//            String tag = var6[var8];
//            expressionValue.append(this.getTagValue(tag, source));
//        }
//
//        return expressionValue.toString();
//    }
//
//    private String getTagValue(String tag, Object source) throws SequenceGenerateException {
//        this.log.debug("Evaluating expression for tag : {}", tag);
//        if (!tag.startsWith("<") && !tag.endsWith(">")) {
//            return tag;
//        } else {
//            tag = tag.trim().substring(1, tag.length() - 1);
//            SequenceExpression seqExpression = this.seqExpressionRepository.findSequenceExpressionByTag(tag);
//            if (seqExpression == null) {
//                throw new SequenceGenerateException("Expression value is not found, for given tag " + tag);
//            } else {
//                return this.evaluateTagExpression(seqExpression.getExpression(), seqExpression.getType(), source);
//            }
//        }
//    }
//
//    private String evaluateTagExpression(String tagExpression, String sourceType, Object source) throws SequenceGenerateException {
//        try {
//            byte var5 = -1;
//            switch(sourceType.hashCode()) {
//                case -479705388:
//                    if (sourceType.equals("CURRENT_DATE")) {
//                        var5 = 2;
//                    }
//                    break;
//                case -152145192:
//                    if (sourceType.equals("PREFERENCES")) {
//                        var5 = 1;
//                    }
//                    break;
//                case 2050021347:
//                    if (sourceType.equals("ENTITY")) {
//                        var5 = 0;
//                    }
//            }
//
//            switch(var5) {
//                case 0:
//                    return BeanUtils.getProperty(source, tagExpression);
//                case 1:
//                    org.nh.artha.security.dto.Preferences preferences = UserPreferencesUtils.getCurrentUserPreferences();
//                    if (null == preferences && null != preferencesThreadLocal.get()) {
//                        Preferences threadLocalPreferences = (Preferences)preferencesThreadLocal.get();
//                        return BeanUtils.getProperty(threadLocalPreferences, tagExpression);
//                    }
//
//                    return BeanUtils.getProperty(preferences, tagExpression);
//                case 2:
//                    if ("DD".equalsIgnoreCase(tagExpression)) {
//                        return DateUtil.getFormattedValueForCurrentDate(DateUtil.SDF_DD);
//                    } else if ("MM".equalsIgnoreCase(tagExpression)) {
//                        return DateUtil.getFormattedValueForCurrentDate(DateUtil.SDF_MM);
//                    } else if ("YY".equalsIgnoreCase(tagExpression)) {
//                        return DateUtil.getFormattedValueForCurrentDate(DateUtil.SDF_YY);
//                    } else {
//                        if ("YYYY".equalsIgnoreCase(tagExpression)) {
//                            return DateUtil.getFormattedValueForCurrentDate(DateUtil.SDF_YYYY);
//                        }
//
//                        throw new SequenceGenerateException(sourceType + " Type is not defined for Tag expression " + tagExpression);
//                    }
//                default:
//                    throw new SequenceGenerateException(sourceType + " Type is not defined for Tag expression " + tagExpression);
//            }
//        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException var8) {
//            throw new SequenceGenerateException("Exception occur while evaluating Tag expression.");
//        }
//    }
//
//    public boolean validateSequence(String resetExpression, String suffix, String prefix, String delimiter) {
//        boolean isValidSequence = true;
//        if (resetExpression != null && !resetExpression.isEmpty()) {
//            String[] resetExpTokens = resetExpression.split("\\" + delimiter.trim());
//            String[] var7 = resetExpTokens;
//            int var8 = resetExpTokens.length;
//
//            for(int var9 = 0; var9 < var8; ++var9) {
//                String tag = var7[var9];
//                if (!this.isValidTag(tag)) {
//                    isValidSequence = false;
//                    break;
//                }
//            }
//        }
//
//        if (isValidSequence && prefix != null && !prefix.isEmpty()) {
//            isValidSequence = this.isValidTag(prefix);
//        }
//
//        if (isValidSequence && suffix != null && !suffix.isEmpty()) {
//            isValidSequence = this.isValidTag(suffix);
//        }
//
//        return isValidSequence;
//    }
//
//    private boolean isValidTag(String tag) {
//        this.log.debug("validating tag : {}", tag);
//        if (!tag.startsWith("<") && !tag.endsWith(">")) {
//            return true;
//        } else {
//            tag = tag.trim().substring(1, tag.length() - 1);
//            SequenceExpression sequenceExpression = this.seqExpressionRepository.findSequenceExpressionByTag(tag);
//            this.log.debug("sequenceExpression for tag : {}", sequenceExpression);
//            return sequenceExpression != null;
//        }
//    }
}
