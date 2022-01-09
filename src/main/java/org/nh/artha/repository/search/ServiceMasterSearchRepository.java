package org.nh.artha.repository.search;
import org.nh.artha.domain.ServiceMaster;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ServiceMaster} entity.
 */
public interface ServiceMasterSearchRepository extends ElasticsearchRepository<ServiceMaster, Long> {
}
