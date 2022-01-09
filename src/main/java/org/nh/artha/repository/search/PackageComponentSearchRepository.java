package org.nh.artha.repository.search;
import org.nh.artha.domain.PackageComponent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link PackageComponent} entity.
 */
public interface PackageComponentSearchRepository extends ElasticsearchRepository<PackageComponent, Long> {
}
