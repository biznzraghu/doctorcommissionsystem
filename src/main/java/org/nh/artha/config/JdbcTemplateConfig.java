package org.nh.artha.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;


@Configuration
public class JdbcTemplateConfig {

    private final Logger log = LoggerFactory.getLogger(JdbcTemplateConfig.class);

    @Bean(name = "mdmDbDataSource")
    @ConfigurationProperties("mdm.datasource")
    public DataSource mdmDbDataSource() {
        return  DataSourceBuilder.create().build();
    }


    @Bean("mdmDbTemplate") //used Qualifier because it was not picking the correct data source.It was picking the default data source
    public JdbcTemplate mdmDbTemplate(@Qualifier("mdmDbDataSource") DataSource mdmDbDataSource){
        return new JdbcTemplate(mdmDbDataSource);
    }

    @Bean("mdmDbNamedParameterTemplate")
    public NamedParameterJdbcTemplate mdmDbNamedParameterTemplate(@Qualifier("mdmDbDataSource") DataSource mdmDbDataSource){
        return  new NamedParameterJdbcTemplate(mdmDbDataSource);
    }

}
