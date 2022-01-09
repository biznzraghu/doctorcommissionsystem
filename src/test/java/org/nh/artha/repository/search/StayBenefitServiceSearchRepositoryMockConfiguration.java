package org.nh.artha.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link StayBenefitServiceSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class StayBenefitServiceSearchRepositoryMockConfiguration {

    @MockBean
    private StayBenefitServiceSearchRepository mockStayBenefitServiceSearchRepository;

}
