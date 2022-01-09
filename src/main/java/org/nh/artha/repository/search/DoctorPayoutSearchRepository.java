package org.nh.artha.repository.search;
import org.nh.artha.domain.DoctorPayout;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DoctorPayout} entity.
 */
public interface DoctorPayoutSearchRepository extends ElasticsearchRepository<DoctorPayout, Long> {
}
