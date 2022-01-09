package org.nh.artha.repository.search;
import org.nh.artha.domain.UserOrganizationRoleMapping;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link UserOrganizationRoleMapping} entity.
 */
public interface UserOrganizationRoleMappingSearchRepository extends ElasticsearchRepository<UserOrganizationRoleMapping, Long> {
}
