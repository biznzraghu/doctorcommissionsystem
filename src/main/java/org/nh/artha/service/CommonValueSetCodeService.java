package org.nh.artha.service;
import org.nh.artha.domain.AbstractValueSetCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface CommonValueSetCodeService {
    /**
     *  Get all the valueSetCodes.
     *
     *  @param tClass the pagination information
     *  @return the list of entities
     */
    <T> Page<T> findAll(Class<T> tClass);

    /**
     *  Get the "id" valueSetCode.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    <T> T findOne(Class<T> tClass, Long id);

    /**
     * Search for the valueSetCode corresponding to the query.
     *
     *  @param query the query of the search
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    <T extends AbstractValueSetCode> Page<? extends AbstractValueSetCode> search(String query, Pageable pageable);

}
