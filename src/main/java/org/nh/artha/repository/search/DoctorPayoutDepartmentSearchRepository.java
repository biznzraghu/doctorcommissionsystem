package org.nh.artha.repository.search;
import org.nh.artha.domain.DoctorPayoutDepartment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DoctorPayoutDepartment} entity.
 */
public interface DoctorPayoutDepartmentSearchRepository extends ElasticsearchRepository<DoctorPayoutDepartment, Long> {
}
