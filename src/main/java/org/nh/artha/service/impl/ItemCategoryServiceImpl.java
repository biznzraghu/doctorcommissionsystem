package org.nh.artha.service.impl;

import org.nh.artha.service.ItemCategoryService;
import org.nh.artha.domain.ItemCategory;
import org.nh.artha.repository.ItemCategoryRepository;
import org.nh.artha.repository.search.ItemCategorySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link ItemCategory}.
 */
@Service
@Transactional
public class ItemCategoryServiceImpl implements ItemCategoryService {

    private final Logger log = LoggerFactory.getLogger(ItemCategoryServiceImpl.class);

    private final ItemCategoryRepository itemCategoryRepository;

    private final ItemCategorySearchRepository itemCategorySearchRepository;

    public ItemCategoryServiceImpl(ItemCategoryRepository itemCategoryRepository, ItemCategorySearchRepository itemCategorySearchRepository) {
        this.itemCategoryRepository = itemCategoryRepository;
        this.itemCategorySearchRepository = itemCategorySearchRepository;
    }

    /**
     * Save a itemCategory.
     *
     * @param itemCategory the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ItemCategory save(ItemCategory itemCategory) {
        log.debug("Request to save ItemCategory : {}", itemCategory);
        ItemCategory result = itemCategoryRepository.save(itemCategory);
        itemCategorySearchRepository.save(result);
        return result;
    }

    /**
     * Get all the itemCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ItemCategory> findAll(Pageable pageable) {
        log.debug("Request to get all ItemCategories");
        return itemCategoryRepository.findAll(pageable);
    }


    /**
     * Get one itemCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ItemCategory> findOne(Long id) {
        log.debug("Request to get ItemCategory : {}", id);
        return itemCategoryRepository.findById(id);
    }

    /**
     * Delete the itemCategory by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ItemCategory : {}", id);
        itemCategoryRepository.deleteById(id);
        itemCategorySearchRepository.deleteById(id);
    }

    /**
     * Search for the itemCategory corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ItemCategory> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ItemCategories for query {}", query);
        return itemCategorySearchRepository.search(queryStringQuery(query), pageable);    }
}
