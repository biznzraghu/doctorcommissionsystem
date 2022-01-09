package org.nh.artha.repository.search;

import org.nh.artha.domain.ExceptionSponsor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ExceptionSponsor} entity.
 */
public interface ExceptionSponsorSearchRepository extends ElasticsearchRepository<ExceptionSponsor, Long> {
}
