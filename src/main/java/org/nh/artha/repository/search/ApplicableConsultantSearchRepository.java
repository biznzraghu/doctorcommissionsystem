package org.nh.artha.repository.search;

import org.nh.artha.domain.ApplicableConsultant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ApplicableConsultant} entity.
 */
public interface ApplicableConsultantSearchRepository extends ElasticsearchRepository<ApplicableConsultant, Long> {
}
