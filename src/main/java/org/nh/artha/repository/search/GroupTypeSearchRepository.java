package org.nh.artha.repository.search;
import org.nh.artha.domain.GroupType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link GroupType} entity.
 */
public interface GroupTypeSearchRepository extends ElasticsearchRepository<GroupType, Long> {
}
