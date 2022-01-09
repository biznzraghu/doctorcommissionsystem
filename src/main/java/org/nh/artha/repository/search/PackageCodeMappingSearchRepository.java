package org.nh.artha.repository.search;
import org.nh.artha.domain.PackageCodeMapping;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link PackageCodeMapping} entity.
 */
public interface PackageCodeMappingSearchRepository extends ElasticsearchRepository<PackageCodeMapping, Long> {
}
