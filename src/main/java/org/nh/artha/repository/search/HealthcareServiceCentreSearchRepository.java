package org.nh.artha.repository.search;

import org.nh.artha.domain.HealthcareServiceCenter;
import org.nh.artha.domain.Plan;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link HealthcareServiceCenter} entity.
 */
public interface HealthcareServiceCentreSearchRepository extends ElasticsearchRepository<HealthcareServiceCenter, Long> {
}
