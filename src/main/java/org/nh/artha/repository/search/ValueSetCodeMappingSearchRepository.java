package org.nh.artha.repository.search;
import org.nh.artha.domain.ValueSetCodeMapping;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ValueSetCodeMapping} entity.
 */
public interface ValueSetCodeMappingSearchRepository extends ElasticsearchRepository<ValueSetCodeMapping, Long> {
}
