package org.nh.artha.repository.search;

import org.nh.artha.domain.ServiceItemBenefit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ServiceItemBenefit} entity.
 */
public interface ServiceItemBenefitSearchRepository extends ElasticsearchRepository<ServiceItemBenefit, Long> {
}
