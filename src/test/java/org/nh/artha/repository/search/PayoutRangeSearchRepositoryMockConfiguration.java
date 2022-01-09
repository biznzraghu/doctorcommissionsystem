package org.nh.artha.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link PayoutRangeSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PayoutRangeSearchRepositoryMockConfiguration {

    @MockBean
    private PayoutRangeSearchRepository mockPayoutRangeSearchRepository;

}
