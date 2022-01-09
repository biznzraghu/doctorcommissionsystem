package org.nh.artha.repository.search;
import org.nh.artha.domain.ValueSetCode;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ValueSetCode} entity.
 */
public interface ValueSetCodeSearchRepository extends ElasticsearchRepository<ValueSetCode, Long> {
}
