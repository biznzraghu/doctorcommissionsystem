package org.nh.artha.repository.search;
import org.nh.artha.domain.Feature;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Feature} entity.
 */
public interface FeatureSearchRepository extends ElasticsearchRepository<Feature, Long> {
}
