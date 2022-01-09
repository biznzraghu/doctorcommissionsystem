package org.nh.artha.repository.search;

import org.nh.artha.domain.HscConsumptionMaterialReduction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link HscConsumptionMaterialReduction} entity.
 */
public interface HscConsumptionMaterialReductionSearchRepository extends ElasticsearchRepository<HscConsumptionMaterialReduction, Long> {
}
