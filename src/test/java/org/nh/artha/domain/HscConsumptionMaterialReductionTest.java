package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class HscConsumptionMaterialReductionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HscConsumptionMaterialReduction.class);
        HscConsumptionMaterialReduction hscConsumptionMaterialReduction1 = new HscConsumptionMaterialReduction();
        hscConsumptionMaterialReduction1.setId(1L);
        HscConsumptionMaterialReduction hscConsumptionMaterialReduction2 = new HscConsumptionMaterialReduction();
        hscConsumptionMaterialReduction2.setId(hscConsumptionMaterialReduction1.getId());
        assertThat(hscConsumptionMaterialReduction1).isEqualTo(hscConsumptionMaterialReduction2);
        hscConsumptionMaterialReduction2.setId(2L);
        assertThat(hscConsumptionMaterialReduction1).isNotEqualTo(hscConsumptionMaterialReduction2);
        hscConsumptionMaterialReduction1.setId(null);
        assertThat(hscConsumptionMaterialReduction1).isNotEqualTo(hscConsumptionMaterialReduction2);
    }
}
