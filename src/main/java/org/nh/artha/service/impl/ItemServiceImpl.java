package org.nh.artha.service.impl;

import liquibase.util.csv.CSVReader;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.nh.artha.domain.dto.GenericItemDTO;
import org.nh.artha.service.ItemService;
import org.nh.artha.domain.Item;
import org.nh.artha.repository.ItemRepository;
import org.nh.artha.repository.search.ItemSearchRepository;
import org.nh.artha.util.EmptyPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Item}.
 */
@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    private final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    private final ItemRepository itemRepository;

    private final ItemSearchRepository itemSearchRepository;

    private final RestHighLevelClient restHighLevelClient;

    public ItemServiceImpl(ItemRepository itemRepository, ItemSearchRepository itemSearchRepository, RestHighLevelClient restHighLevelClient) {
        this.itemRepository = itemRepository;
        this.itemSearchRepository = itemSearchRepository;
        this.restHighLevelClient = restHighLevelClient;
    }

    /**
     * Save a item.
     *
     * @param item the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Item save(Item item) {
        log.debug("Request to save Item : {}", item);
        Item result = itemRepository.save(item);
        itemSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the items.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Item> findAll(Pageable pageable) {
        log.debug("Request to get all Items");
        return itemRepository.findAll(pageable);
    }


    /**
     * Get one item by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Item> findOne(Long id) {
        log.debug("Request to get Item : {}", id);
        return itemRepository.findById(id);
    }

    /**
     * Delete the item by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Item : {}", id);
        itemRepository.deleteById(id);
        itemSearchRepository.deleteById(id);
    }

    /**
     * Search for the item corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Item> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Items for query {}", query);
        return itemSearchRepository.search(queryStringQuery(query), pageable);    }

    @Override
    public void importItems(MultipartFile file)throws  Exception{
        Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        CSVReader csvReader=new CSVReader(reader);
        List<String[]> itemArray = csvReader.readAll();
        itemArray.remove(0);
        List<Item> all = itemArray.stream().map(createItemsFromCsv).distinct().collect(Collectors.toList());
        saveAll(all);
    }

    @Override
    public List<Item> saveAll(List<Item> itemList) {
        List<Item> items = itemRepository.saveAll(itemList);
        itemSearchRepository.saveAll(items);
        return items;
    }

    private Function<String[] , Item> createItemsFromCsv = (String [] csvRowArray) -> {
        Item item=new Item();
        try {
            item.setCode(csvRowArray[0]);
            item.setName(csvRowArray[1]);
            item.setCategory(csvRowArray[2]);
            item.setActive(csvRowArray[3].equalsIgnoreCase("Active")?true:false);
        } catch (Exception e) {
            System.out.println(Arrays.toString(csvRowArray));
            throw new RuntimeException(e);
        }
        return item;
    };

    @Override
    @Transactional(readOnly = true)
    public Page<Item> searchItemWithBrand(String query, Pageable pageable) {
        log.debug("Request to search for a page of Item With Brand for query {}", query);
        return itemSearchRepository.search(queryStringQuery(query).field("name"), pageable);
    }


    @Override
    public List<GenericItemDTO> searchDistinctGanericName(String query, Long groupId) {
        GenericItemDTO genericItemDTO=new GenericItemDTO();
        Map<String, Long> ganericCount = new HashMap<>();
        if (null == query) {
            query = "";
        }
        StringBuffer queryString = null;
        if (null != groupId) {
            queryString = new StringBuffer("*").append(query).append("*").append(" AND ").append("group.id:").append(groupId);
        } else {
            queryString = new StringBuffer("*").append(query).append("*");
        }
        SearchRequest searchRequest = new SearchRequest("item");
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
            .withPageable(EmptyPage.INSTANCE).withQuery(queryStringQuery(queryString.toString()).defaultField("generic").defaultOperator(Operator.AND)).build();
        //addAggregation(AggregationBuilders.terms("generic").field("generic.keyword").size(20)).build();

        /*Aggregations aggregations = elasticsearchOperations.query(searchQuery, new ResultsExtractor<Aggregations>() {

            @Override
            public Aggregations extract(SearchResponse searchResponse) {
                return searchResponse.getAggregations();
            }
        });*/
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(searchQuery.getQuery()).aggregation(AggregationBuilders.terms("generic").field("generic.keyword").size(20));
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse=restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            Terms terms = searchResponse.getAggregations().get("generic");
            for (Terms.Bucket bucket : terms.getBuckets()) {
                ganericCount.put(bucket.getKeyAsString(), bucket.getDocCount());
            }
            Set<String> ganericName=ganericCount.keySet();

            return convertGenericItemDTO(ganericName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }


    private List<GenericItemDTO> convertGenericItemDTO(Set<String> genericNames) {
        List<GenericItemDTO> genericItemDTOS=new ArrayList<GenericItemDTO>();
        for (String genericName : genericNames) {
            GenericItemDTO genericItemDTO = new GenericItemDTO();
            genericItemDTO.setGenericName(genericName);
            genericItemDTOS.add(genericItemDTO);
        }

        return genericItemDTOS;
    }
}
