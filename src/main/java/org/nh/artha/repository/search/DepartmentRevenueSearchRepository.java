package org.nh.artha.repository.search;
import org.nh.artha.domain.DepartmentRevenue;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DepartmentRevenue} entity.
 */
public interface DepartmentRevenueSearchRepository extends ElasticsearchRepository<DepartmentRevenue, Long> {
}
