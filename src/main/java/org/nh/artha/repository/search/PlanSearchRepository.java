package org.nh.artha.repository.search;

import org.nh.artha.domain.Plan;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Plan} entity.
 */
public interface PlanSearchRepository extends ElasticsearchRepository<Plan, Long> {
}
