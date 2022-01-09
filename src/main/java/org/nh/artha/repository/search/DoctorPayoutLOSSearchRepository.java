package org.nh.artha.repository.search;
import org.nh.artha.domain.DoctorPayoutLOS;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DoctorPayoutLOS} entity.
 */
public interface DoctorPayoutLOSSearchRepository extends ElasticsearchRepository<DoctorPayoutLOS, Long> {
}
