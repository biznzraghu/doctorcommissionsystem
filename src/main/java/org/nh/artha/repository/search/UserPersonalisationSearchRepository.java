package org.nh.artha.repository.search;

import org.nh.artha.domain.UserPersonalisation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserPersonalisation entity.
 */
public interface UserPersonalisationSearchRepository extends ElasticsearchRepository<UserPersonalisation, Long> {
}
