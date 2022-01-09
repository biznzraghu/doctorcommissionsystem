package org.nh.artha.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.RestHighLevelClient;
import org.nh.artha.elastic.converter.ElasticSearchCustomConverter;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

@Configuration
@EnableConfigurationProperties(ElasticsearchProperties.class)
public class ElasticsearchConfiguration {


    private ObjectMapper mapper;

    public ElasticsearchConfiguration(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Bean
    public String moduleName(){
        return "artha";
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate(RestHighLevelClient client) {
        ElasticsearchRestTemplate elasticsearchRestTemplate = new ElasticsearchRestTemplate(client);
        ElasticSearchCustomConverter.addElasticConverters(elasticsearchRestTemplate);
        return elasticsearchRestTemplate;
    }

}
