package org.nh.artha.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ServiceMasterSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ServiceMasterSearchRepositoryMockConfiguration {

    @MockBean
    private ServiceMasterSearchRepository mockServiceMasterSearchRepository;

}
