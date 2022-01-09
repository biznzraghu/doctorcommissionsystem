package org.nh.artha.repository.search;

import org.nh.artha.domain.VariablePayout;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link VariablePayout} entity.
 */
public interface VariablePayoutSearchRepository extends ElasticsearchRepository<VariablePayout, Long> {
}
