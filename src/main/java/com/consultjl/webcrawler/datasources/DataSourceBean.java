package com.consultjl.webcrawler.datasources;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceBean {

    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:mysql://localhost:3306/test");
        dataSourceBuilder.username("root");
        dataSourceBuilder.password("toor");
        return dataSourceBuilder.build();
    }
}
