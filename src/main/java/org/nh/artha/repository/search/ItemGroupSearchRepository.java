package org.nh.artha.repository.search;
import org.nh.artha.domain.ItemGroup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ItemGroup} entity.
 */
public interface ItemGroupSearchRepository extends ElasticsearchRepository<ItemGroup, Long> {
}
