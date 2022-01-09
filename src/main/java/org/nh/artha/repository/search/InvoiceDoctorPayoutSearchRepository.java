package org.nh.artha.repository.search;
import org.nh.artha.domain.InvoiceDoctorPayout;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link InvoiceDoctorPayout} entity.
 */
public interface InvoiceDoctorPayoutSearchRepository extends ElasticsearchRepository<InvoiceDoctorPayout, Long> {
}
