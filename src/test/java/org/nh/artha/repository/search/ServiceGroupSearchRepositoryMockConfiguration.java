package org.nh.artha.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ServiceGroupSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ServiceGroupSearchRepositoryMockConfiguration {

    @MockBean
    private ServiceGroupSearchRepository mockServiceGroupSearchRepository;

}
