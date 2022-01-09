package org.nh.artha.repository.search;
import org.nh.artha.domain.OrganizationType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link OrganizationType} entity.
 */
public interface OrganizationTypeSearchRepository extends ElasticsearchRepository<OrganizationType, Long> {
}
