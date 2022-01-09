package org.nh.artha.repository.search;
import org.nh.artha.domain.ConfigurationDefination;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ConfigurationDefination} entity.
 */
public interface ConfigurationDefinationSearchRepository extends ElasticsearchRepository<ConfigurationDefination, Long> {
}
