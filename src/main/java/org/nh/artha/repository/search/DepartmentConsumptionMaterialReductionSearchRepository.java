package org.nh.artha.repository.search;

import org.nh.artha.domain.DepartmentConsumptionMaterialReduction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DepartmentConsumptionMaterialReduction} entity.
 */
public interface DepartmentConsumptionMaterialReductionSearchRepository extends ElasticsearchRepository<DepartmentConsumptionMaterialReduction, Long> {
}
