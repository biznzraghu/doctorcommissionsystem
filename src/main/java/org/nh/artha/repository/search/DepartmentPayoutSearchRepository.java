package org.nh.artha.repository.search;

import org.nh.artha.domain.DepartmentPayout;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DepartmentPayout} entity.
 */
public interface DepartmentPayoutSearchRepository extends ElasticsearchRepository<DepartmentPayout, Long> {
}
