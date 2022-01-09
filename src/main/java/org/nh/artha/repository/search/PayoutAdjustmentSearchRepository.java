package org.nh.artha.repository.search;

import org.nh.artha.domain.PayoutAdjustment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link PayoutAdjustment} entity.
 */
public interface PayoutAdjustmentSearchRepository extends ElasticsearchRepository<PayoutAdjustment, Long> {
}
