package org.nh.artha.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link PayoutAdjustmentSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PayoutAdjustmentSearchRepositoryMockConfiguration {

    @MockBean
    private PayoutAdjustmentSearchRepository mockPayoutAdjustmentSearchRepository;

}
