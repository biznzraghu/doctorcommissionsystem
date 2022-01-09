package org.nh.artha.repository.search;
import org.nh.artha.domain.ConfigurationCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ConfigurationCategory} entity.
 */
public interface ConfigurationCategorySearchRepository extends ElasticsearchRepository<ConfigurationCategory, Long> {
}
