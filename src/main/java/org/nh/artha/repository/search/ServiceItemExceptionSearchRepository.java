package org.nh.artha.repository.search;

import org.nh.artha.domain.ServiceItemException;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ServiceItemException} entity.
 */
public interface ServiceItemExceptionSearchRepository extends ElasticsearchRepository<ServiceItemException, Long> {
}
