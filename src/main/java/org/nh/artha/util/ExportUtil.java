package org.nh.artha.util;

import org.nh.artha.domain.MisReport;
import org.nh.artha.security.SecurityUtils;

import java.io.File;
import java.util.Date;

public class ExportUtil {

    public static File getCSVExportFile(String featureName, String parentPath) {
        StringBuilder fileName = new StringBuilder(featureName)
            .append("_")
            .append(SecurityUtils.getCurrentUserLogin())
            .append("_")
            .append(DateUtil.getDateFormatForExportCSVFileName(new Date()))
            .append(".csv");
        File file = new File(parentPath+fileName);
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }
        return file;
    }

    public static File getCSVExportFile(String featureName, String parentPath, MisReport misReport) {
        StringBuilder fileName = new StringBuilder(featureName)
            .append("_")
            .append(misReport.getCreatedBy().getLogin())
            .append("_")
            .append(DateUtil.getDateFormatForExportCSVFileName(new Date()))
            .append(".csv");
        File file = new File(parentPath+fileName);
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }
        return file;
    }

    public static File getXLSXExportFile(String featureName,String docNumber, String parentPath) {
        StringBuilder fileName = new StringBuilder(featureName)
            .append("_")
            .append(SecurityUtils.getCurrentUserLogin())
            .append("_")
            .append(docNumber)
            .append(".xlsx");
        File file = new File(parentPath+fileName);
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }
        return file;
    }

    public static File getXLSXExportFile(String featureName, String parentPath) {
        StringBuilder fileName = new StringBuilder(featureName)
                .append("_")
                .append(SecurityUtils.getCurrentUserLogin())
                .append("_")
                .append(".xlsx");
        File file = new File(parentPath+fileName);
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }
        return file;
    }

    public static String formatAmount(Float amount) {
        String[] fracPart = (""+amount).split("\\.");
        if(fracPart[1].length()<2)
            return new StringBuilder("=\"").append(amount).append("0\"").toString();
        return new StringBuilder("=\"").append(amount).append("\"").toString();
    }

}
