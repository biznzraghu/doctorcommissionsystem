package org.nh.artha.repository.search;
import org.nh.artha.domain.PackageMaster;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link PackageMaster} entity.
 */
public interface PackageMasterSearchRepository extends ElasticsearchRepository<PackageMaster, Long> {
}
