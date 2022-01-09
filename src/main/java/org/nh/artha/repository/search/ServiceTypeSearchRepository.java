package org.nh.artha.repository.search;
import org.nh.artha.domain.ServiceType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ServiceType} entity.
 */
public interface ServiceTypeSearchRepository extends ElasticsearchRepository<ServiceType, Long> {
}
