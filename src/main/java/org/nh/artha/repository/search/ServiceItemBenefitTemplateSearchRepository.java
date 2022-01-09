package org.nh.artha.repository.search;
import org.nh.artha.domain.ServiceItemBenefitTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ServiceItemBenefitTemplate} entity.
 */
public interface ServiceItemBenefitTemplateSearchRepository extends ElasticsearchRepository<ServiceItemBenefitTemplate, Long> {
}
