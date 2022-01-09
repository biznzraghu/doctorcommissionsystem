package org.nh.artha.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public interface DashBoardService {

    Map<String,Object> getDoctorWiseRevenueTrend(String unitCode, String format, Date fromDate, Date toDate) throws IOException, ParseException;
    Map<String,Object> getDepartmentWiseRevenueTrend(String unitCode, String format, Date fromDate, Date toDate) throws IOException, ParseException;
}
