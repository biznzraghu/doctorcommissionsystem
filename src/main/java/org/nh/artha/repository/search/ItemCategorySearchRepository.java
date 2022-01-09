package org.nh.artha.repository.search;
import org.nh.artha.domain.ItemCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ItemCategory} entity.
 */
public interface ItemCategorySearchRepository extends ElasticsearchRepository<ItemCategory, Long> {
}
