package org.nh.artha.repository;
import org.nh.artha.domain.ItemGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ItemGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemGroupRepository extends JpaRepository<ItemGroup, Long> {

}
