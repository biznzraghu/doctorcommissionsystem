package org.nh.artha.repository.search;

import org.nh.artha.domain.DepartmentRevenueBenefit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DepartmentRevenueBenefit} entity.
 */
public interface DepartmentRevenueBenefitSearchRepository extends ElasticsearchRepository<DepartmentRevenueBenefit, Long> {
}
