package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class ItemCategoryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemCategory.class);
        ItemCategory itemCategory1 = new ItemCategory();
        itemCategory1.setId(1L);
        ItemCategory itemCategory2 = new ItemCategory();
        itemCategory2.setId(itemCategory1.getId());
        assertThat(itemCategory1).isEqualTo(itemCategory2);
        itemCategory2.setId(2L);
        assertThat(itemCategory1).isNotEqualTo(itemCategory2);
        itemCategory1.setId(null);
        assertThat(itemCategory1).isNotEqualTo(itemCategory2);
    }
}
