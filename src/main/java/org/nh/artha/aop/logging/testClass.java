package org.nh.artha.aop.logging;

import java.util.HashSet;
import java.util.Set;

public class testClass {
        public static void main(String []args){
            Set<String> consultantNames = new HashSet<>();
            String S1="Dr.Vivek Agarwala /  Dr.Chandrakanth MV / Pradip Kumar Mondal /Dr Sudip Das";
            String S2="Dr.Vivek Agarwala /  Dr.Chandrakanth MV / Pradip Kumar Mondal /Dr Sudip Das";
            consultantNames.add(S1.trim());
            consultantNames.add(S2);
            System.out.println(String.join(",", consultantNames).trim());
        }
    }

