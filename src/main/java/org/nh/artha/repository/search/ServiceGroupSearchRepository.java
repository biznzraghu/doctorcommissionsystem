package org.nh.artha.repository.search;
import org.nh.artha.domain.ServiceGroup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ServiceGroup} entity.
 */
public interface ServiceGroupSearchRepository extends ElasticsearchRepository<ServiceGroup, Long> {
}
