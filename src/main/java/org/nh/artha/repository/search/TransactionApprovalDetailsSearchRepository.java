package org.nh.artha.repository.search;

import org.nh.artha.domain.TransactionApprovalDetails;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link TransactionApprovalDetails} entity.
 */
public interface TransactionApprovalDetailsSearchRepository extends ElasticsearchRepository<TransactionApprovalDetails, Long> {
}
