package org.nh.artha.repository.search;


import org.nh.artha.domain.UserDashboard;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserDashboard entity.
 */
public interface UserDashboardSearchRepository extends ElasticsearchRepository<UserDashboard, Long> {
}
