package org.nh.artha.repository.search;

import org.nh.artha.domain.StayBenefitService;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link StayBenefitService} entity.
 */
public interface StayBenefitServiceSearchRepository extends ElasticsearchRepository<StayBenefitService, Long> {
}
