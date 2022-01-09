package org.nh.artha.repository.search;
import org.nh.artha.domain.ItemType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ItemType} entity.
 */
public interface ItemTypeSearchRepository extends ElasticsearchRepository<ItemType, Long> {
}
