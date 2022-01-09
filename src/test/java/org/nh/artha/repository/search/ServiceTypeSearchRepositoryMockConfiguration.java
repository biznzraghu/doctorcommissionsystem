package org.nh.artha.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ServiceTypeSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ServiceTypeSearchRepositoryMockConfiguration {

    @MockBean
    private ServiceTypeSearchRepository mockServiceTypeSearchRepository;

}
