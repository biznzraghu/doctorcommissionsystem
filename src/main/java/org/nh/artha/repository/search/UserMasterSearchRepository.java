package org.nh.artha.repository.search;
import org.nh.artha.domain.UserMaster;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link UserMaster} entity.
 */
public interface UserMasterSearchRepository extends ElasticsearchRepository<UserMaster, Long> {
}
