package org.nh.artha.util;

import org.nh.artha.domain.Group;
import org.nh.artha.domain.ItemGroup;

public class KeyGenerator {

    public String createKey(String group){
        StringBuilder key=new StringBuilder();
        if(null!= group) {
            key.append(group);
        }else{
            key.append("-X");
        }
        return key.toString();
    }

    public String createVisitTypeTariffClassKey(String visitType,String tariffClass){
        StringBuilder key=new StringBuilder();
        if(null!= visitType) {
            key.append(visitType+"-");
        }else{
            key.append("-X");
        }
        if(null!=tariffClass ) {
            key.append(tariffClass);
        }else{
            key.append("-X");
        }
        return key.toString();
    }

    public static String createMcrVisitTypeEmployeeIdDepartmentKey(String visitType,Long employeeId){
        StringBuffer key = new StringBuffer();
        if(null!=visitType){
            key.append(visitType+"-");
        }else{
            key.append("-X");
        }if(employeeId!=null){
            key.append(employeeId+"-");
        }else {
            key.append("-X");
        }
        return key.toString();
    }


    public static String createMcrVisitTypeDepartmentAmountMaterialKey(String visitType,String departmentCode,String amountMaterial){
        StringBuffer key = new StringBuffer();
        if(null!=visitType){
            key.append(visitType+"-");
        }else{
            key.append("-X");
        }if(departmentCode!=null){
            key.append(departmentCode+"-");
        }else {
            key.append("-X");
        }
        return key.toString();
    }


}
