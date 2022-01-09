package org.nh.artha.util;

import static com.hazelcast.util.CollectionUtil.isNotEmpty;
import static java.util.Objects.nonNull;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.existsQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.nh.artha.web.rest.errors.ErrorConstants.GROUP_NOT_FOUND;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.nh.artha.domain.dto.Configuration;
import org.nh.artha.domain.dto.GroupDTO;
import org.nh.artha.domain.dto.OrganizationDTO;
import org.nh.artha.domain.enumeration.Context;
import org.nh.artha.web.rest.errors.CustomParameterizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;

/**
 * A ConfigurationUtil.
 */
public class ConfigUtil {

    private static final Logger log = LoggerFactory.getLogger(ConfigUtil.class);

    /**
     * Get configuration value using input parameters
     *
     * @param key    configuration key
     * @param hscId  health care center id
     * @param unitId unit id
     * @return value configuration value
     * @Param elasticsearchTemplate search template
     */
    public static String getConfiguration(String key, Long hscId, Long unitId, Long unitPartOfId, ElasticsearchOperations elasticsearchTemplate) {

        StringBuilder builder = new StringBuilder().append("(").append("key:").append(key).append(" AND (((applicableType:System OR applicableType:system) AND applicableTo:0)");
        if (nonNull(hscId)) {
            builder.append(" OR ((applicableType:Local OR applicableType:local) AND applicableTo:").append(hscId).append(")");
        }
        if (nonNull(unitId)) {
            builder.append(" OR ((applicableType:Unit OR applicableType:unit) AND applicableTo:").append(unitId).append(")");
        }
        if (nonNull(unitPartOfId)) {
            builder.append(" OR ((applicableType:Global OR applicableType:global) AND applicableTo:").append(unitPartOfId).append(")");
        } else {
            if (nonNull(unitId)) {
                Query searchQuery = new NativeSearchQueryBuilder()
                    //.withIndices("organization")
                    .withQuery(termQuery("id", unitId))
                    .build();
                List<OrganizationDTO> organizationList = elasticsearchTemplate.search(searchQuery, OrganizationDTO.class,IndexCoordinates.of("organization")).get().map(org.springframework.data.elasticsearch.core.SearchHit::getContent).collect(Collectors.toList());
                if (isNotEmpty(organizationList)) {
                	OrganizationDTO partOf = organizationList.get(0).getPartOf();
                    if (nonNull(partOf))
                        builder.append(" OR ((applicableType:Global OR applicableType:global) AND applicableTo:").append(partOf.getId()).append(")");
                }
            }
        }
        builder.append("))");
        log.debug("configuration builder final query {}",builder.toString());
        Query searchQuery = new NativeSearchQueryBuilder()
            //.withIndices("configuration")
            .withQuery(queryStringQuery(builder.toString()))
            .withPageable(PageRequest.of(0, 1))
            .withSort(SortBuilders.fieldSort("level").order(SortOrder.DESC))
            .build();
        List<Configuration> configurationList = elasticsearchTemplate.search(searchQuery, Configuration.class,IndexCoordinates.of("configuration")).get().map(org.springframework.data.elasticsearch.core.SearchHit::getContent).collect(Collectors.toList());

        if (isNotEmpty(configurationList)) {
            return configurationList.get(0).getValue();
        } else {
            throw new IllegalStateException("No configuration found for the key : " + key);
        }
    }

    /**
     * Get comma separated group codes for workflow
     *
     * @param context context to be searched
     * @param unitId  unit id
     * @return value configuration value
     * @Param elasticsearchTemplate search template
     */
    public static String getCommaSeparatedGroupCodes(Context context, Long unitId, ElasticsearchOperations elasticsearchOperations) {

        QueryBuilder queryBuilder = boolQuery()
            .must(matchQuery("context", context.name()))
            .must(termQuery("active", true))
            .filter(boolQuery().should(termQuery("partOf.id", unitId))
                .should(boolQuery().mustNot(existsQuery("partOf"))));

        Query searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();

        List<GroupDTO> groupList = elasticsearchOperations.search(searchQuery, GroupDTO.class,IndexCoordinates.of("group")).get().map(org.springframework.data.elasticsearch.core.SearchHit::getContent).collect(Collectors.toList());

        if (isNotEmpty(groupList)) {
            List<String> unitGroupCodeList = new ArrayList<>();
            List<String> globalGroupCodeList = new ArrayList<>();
            for (GroupDTO group : groupList) {
                if (nonNull(group.getPartOf())) {
                    unitGroupCodeList.add(group.getCode());
                } else {
                    globalGroupCodeList.add(group.getCode());
                }
            }
            if (isNotEmpty(unitGroupCodeList)) {
                return unitGroupCodeList.stream().map(String::trim).collect(Collectors.joining(","));
            } else {
                return globalGroupCodeList.stream().map(String::trim).collect(Collectors.joining(","));
            }
        } else {
            throw new CustomParameterizedException(GROUP_NOT_FOUND, new HashMap<String, Object>() {{
                put("context", context.getDisplay());
            }});
        }
    }

    public static boolean isGroupExist(Long unitId , Context approvalComittee, ElasticsearchOperations elasticsearchOperations) {
        String group = ConfigUtil.getCommaSeparatedGroupCodes(approvalComittee, unitId, elasticsearchOperations);
        if (null != group && !group.isEmpty())
            return true;
        return false;
    }

    public static void throwExceptionIfGroupNotExist(String action, Long unitId , Context approvalComittee, ElasticsearchOperations elasticsearchOperations) throws Exception {
        if("SENDFORAPPROVAL".equals(action) && !isGroupExist(unitId, approvalComittee, elasticsearchOperations)){
            throw  new CustomParameterizedException("10114","Approval Group doesn't exist for logged in unit");
        }
    }
}
