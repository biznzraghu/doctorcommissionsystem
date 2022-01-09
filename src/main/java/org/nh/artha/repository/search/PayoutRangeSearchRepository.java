package org.nh.artha.repository.search;

import org.nh.artha.domain.PayoutRange;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link PayoutRange} entity.
 */
public interface PayoutRangeSearchRepository extends ElasticsearchRepository<PayoutRange, Long> {
}
