package org.nh.artha.repository.search;
import org.nh.artha.domain.PayoutDetails;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link PayoutDetails} entity.
 */
public interface PayoutDetailsSearchRepository extends ElasticsearchRepository<PayoutDetails, Long> {
}
