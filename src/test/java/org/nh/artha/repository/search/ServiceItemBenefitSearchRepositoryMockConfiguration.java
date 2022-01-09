package org.nh.artha.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ServiceItemBenefitSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ServiceItemBenefitSearchRepositoryMockConfiguration {

    @MockBean
    private ServiceItemBenefitSearchRepository mockServiceItemBenefitSearchRepository;

}
