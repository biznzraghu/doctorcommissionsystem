package org.nh.artha.repository.search;

import org.nh.artha.domain.PayoutAdjustmentDetails;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link PayoutAdjustmentDetails} entity.
 */
public interface PayoutAdjustmentDetailsSearchRepository extends ElasticsearchRepository<PayoutAdjustmentDetails, Long> {
}
