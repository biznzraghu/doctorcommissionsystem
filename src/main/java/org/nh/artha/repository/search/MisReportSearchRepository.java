package org.nh.artha.repository.search;

import org.nh.artha.domain.ApplicableConsultant;
import org.nh.artha.domain.MisReport;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link MisReport} entity.
 */
public interface MisReportSearchRepository extends ElasticsearchRepository<MisReport, Long> {
}
