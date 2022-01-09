package org.nh.artha.repository.search;

import org.nh.artha.domain.Module;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Module} entity.
 */
public interface ModuleSearchRepository extends ElasticsearchRepository<Module, Long> {
}
