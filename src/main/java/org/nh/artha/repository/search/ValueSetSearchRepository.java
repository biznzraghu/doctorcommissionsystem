package org.nh.artha.repository.search;
import org.nh.artha.domain.ValueSet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ValueSet} entity.
 */
public interface ValueSetSearchRepository extends ElasticsearchRepository<ValueSet, Long> {
}
