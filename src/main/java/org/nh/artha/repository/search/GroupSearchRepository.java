package org.nh.artha.repository.search;
import org.nh.artha.domain.Group;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Group} entity.
 */
public interface GroupSearchRepository extends ElasticsearchRepository<Group, Long> {
}
