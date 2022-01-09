package org.nh.artha.repository.search;

import org.nh.artha.domain.LengthOfStayBenefit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link LengthOfStayBenefit} entity.
 */
public interface LengthOfStayBenefitSearchRepository extends ElasticsearchRepository<LengthOfStayBenefit, Long> {
}
