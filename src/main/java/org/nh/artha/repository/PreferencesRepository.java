package org.nh.artha.repository;

import org.nh.artha.domain.Preferences;
import org.nh.artha.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

/**
 * Spring Data JPA repository for the Preferences entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PreferencesRepository extends JpaRepository<Preferences,Long> {

    @Query(value = "select * from preferences preference where preference.user_id = ?1",nativeQuery = true)
    Preferences findOneByUser(Long userid);
}
