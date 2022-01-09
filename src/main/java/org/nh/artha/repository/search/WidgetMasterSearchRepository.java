package org.nh.artha.repository.search;


import org.nh.artha.domain.WidgetMaster;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the WidgetMaster entity.
 */
public interface WidgetMasterSearchRepository extends ElasticsearchRepository<WidgetMaster, Long> {
}
