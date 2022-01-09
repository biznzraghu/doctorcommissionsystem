package org.nh.artha.repository.search;
import org.nh.artha.domain.Configurations;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Configurations} entity.
 */
public interface ConfigurationsSearchRepository extends ElasticsearchRepository<Configurations, Long> {
}
