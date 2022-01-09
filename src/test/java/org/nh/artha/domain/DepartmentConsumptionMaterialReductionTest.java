package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class DepartmentConsumptionMaterialReductionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepartmentConsumptionMaterialReduction.class);
        DepartmentConsumptionMaterialReduction departmentConsumptionMaterialReduction1 = new DepartmentConsumptionMaterialReduction();
        departmentConsumptionMaterialReduction1.setId(1L);
        DepartmentConsumptionMaterialReduction departmentConsumptionMaterialReduction2 = new DepartmentConsumptionMaterialReduction();
        departmentConsumptionMaterialReduction2.setId(departmentConsumptionMaterialReduction1.getId());
        assertThat(departmentConsumptionMaterialReduction1).isEqualTo(departmentConsumptionMaterialReduction2);
        departmentConsumptionMaterialReduction2.setId(2L);
        assertThat(departmentConsumptionMaterialReduction1).isNotEqualTo(departmentConsumptionMaterialReduction2);
        departmentConsumptionMaterialReduction1.setId(null);
        assertThat(departmentConsumptionMaterialReduction1).isNotEqualTo(departmentConsumptionMaterialReduction2);
    }
}
