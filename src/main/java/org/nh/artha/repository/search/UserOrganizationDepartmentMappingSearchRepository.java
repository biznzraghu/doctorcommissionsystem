package org.nh.artha.repository.search;

import org.nh.artha.domain.UserOrganizationDepartmentMapping;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link UserOrganizationDepartmentMapping} entity.
 */
public interface UserOrganizationDepartmentMappingSearchRepository extends ElasticsearchRepository<UserOrganizationDepartmentMapping, Long> {
}
