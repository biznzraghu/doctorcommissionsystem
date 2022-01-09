package org.nh.artha.repository.search;

import org.nh.artha.domain.UserDashboardWidget;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserDashboardWidget entity.
 */
public interface UserDashboardWidgetSearchRepository extends ElasticsearchRepository<UserDashboardWidget, Long> {
}
