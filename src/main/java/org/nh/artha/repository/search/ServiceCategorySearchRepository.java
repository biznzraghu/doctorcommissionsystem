package org.nh.artha.repository.search;
import org.nh.artha.domain.ServiceCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ServiceCategory} entity.
 */
public interface ServiceCategorySearchRepository extends ElasticsearchRepository<ServiceCategory, Long> {
}
