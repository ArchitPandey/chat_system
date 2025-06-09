/*
package com.homemade.chat_service.config.postgres;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@Slf4j
@MapperScan("com.homemade.chat_service.service.postgres.mappers")
public class DatasourceConfig {

    @Bean
    public DataSource dataSource() {
        HikariDataSource hikari = new HikariDataSource();
        hikari.setJdbcUrl("jdbc:postgresql://localhost:30100/postgres");
        hikari.setUsername("chat_admin");
        hikari.setPassword("chat123");
        hikari.setDriverClassName("org.postgresql.Driver");
        hikari.setMinimumIdle(1);
        hikari.setMaximumPoolSize(1);
        return hikari;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        */
/*Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources("classpath:/mappers/*.xml");
        sessionFactory.setMapperLocations(resources);*//*

        return sessionFactory.getObject();
    }
}
*/
