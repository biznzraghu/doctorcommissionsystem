package org.nh.artha.service;

import org.nh.artha.domain.Item;

import org.nh.artha.domain.dto.GenericItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Item}.
 */
public interface ItemService {

    /**
     * Save a item.
     *
     * @param item the entity to save.
     * @return the persisted entity.
     */
    Item save(Item item);

    /**
     * Get all the items.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Item> findAll(Pageable pageable);


    /**
     * Get the "id" item.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Item> findOne(Long id);

    /**
     * Delete the "id" item.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the item corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Item> search(String query, Pageable pageable);

    void importItems(MultipartFile file) throws  Exception;

    List<Item> saveAll(List<Item> packageMasterList);

    Page<Item> searchItemWithBrand(String query, Pageable pageable);

    List<GenericItemDTO> searchDistinctGanericName(String query, Long groupId);
}
