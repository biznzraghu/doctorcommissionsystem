package org.nh.artha.web.rest;

import org.nh.artha.domain.ItemCategory;
import org.nh.artha.service.ItemCategoryService;
import org.nh.artha.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link org.nh.artha.domain.ItemCategory}.
 */
@RestController
@RequestMapping("/api")
public class ItemCategoryResource {

    private final Logger log = LoggerFactory.getLogger(ItemCategoryResource.class);

    private static final String ENTITY_NAME = "itemCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemCategoryService itemCategoryService;

    public ItemCategoryResource(ItemCategoryService itemCategoryService) {
        this.itemCategoryService = itemCategoryService;
    }

    /**
     * {@code POST  /item-categories} : Create a new itemCategory.
     *
     * @param itemCategory the itemCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemCategory, or with status {@code 400 (Bad Request)} if the itemCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/item-categories")
    public ResponseEntity<ItemCategory> createItemCategory(@Valid @RequestBody ItemCategory itemCategory) throws URISyntaxException {
        log.debug("REST request to save ItemCategory : {}", itemCategory);
        if (itemCategory.getId() != null) {
            throw new BadRequestAlertException("A new itemCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemCategory result = itemCategoryService.save(itemCategory);
        return ResponseEntity.created(new URI("/api/item-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /item-categories} : Updates an existing itemCategory.
     *
     * @param itemCategory the itemCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemCategory,
     * or with status {@code 400 (Bad Request)} if the itemCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/item-categories")
    public ResponseEntity<ItemCategory> updateItemCategory(@Valid @RequestBody ItemCategory itemCategory) throws URISyntaxException {
        log.debug("REST request to update ItemCategory : {}", itemCategory);
        if (itemCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ItemCategory result = itemCategoryService.save(itemCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemCategory.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /item-categories} : get all the itemCategories.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemCategories in body.
     */
    @GetMapping("/item-categories")
    public ResponseEntity<List<ItemCategory>> getAllItemCategories(Pageable pageable) {
        log.debug("REST request to get a page of ItemCategories");
        Page<ItemCategory> page = itemCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /item-categories/:id} : get the "id" itemCategory.
     *
     * @param id the id of the itemCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/item-categories/{id}")
    public ResponseEntity<ItemCategory> getItemCategory(@PathVariable Long id) {
        log.debug("REST request to get ItemCategory : {}", id);
        Optional<ItemCategory> itemCategory = itemCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemCategory);
    }

    /**
     * {@code DELETE  /item-categories/:id} : delete the "id" itemCategory.
     *
     * @param id the id of the itemCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/item-categories/{id}")
    public ResponseEntity<Void> deleteItemCategory(@PathVariable Long id) {
        log.debug("REST request to delete ItemCategory : {}", id);
        itemCategoryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/item-categories?query=:query} : search for the itemCategory corresponding
     * to the query.
     *
     * @param query the query of the itemCategory search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/item-categories")
    public ResponseEntity<List<ItemCategory>> searchItemCategories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ItemCategories for query {}", query);
        Page<ItemCategory> page = itemCategoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
