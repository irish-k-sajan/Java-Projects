package com.example.demo.configure;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
    
    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder<?> datasourcebuilder = DataSourceBuilder.create();
        datasourcebuilder.driverClassName("org.postgresql.Driver");
        datasourcebuilder.url("jdbc:postgresql://localhost:5432/postgres");
        datasourcebuilder.password("1234");
        datasourcebuilder.username("postgres");
        return datasourcebuilder.build();
    }

    @Bean(name = "sqlsession")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlsessionfactorybean = new SqlSessionFactoryBean();
        sqlsessionfactorybean.setDataSource(getDataSource());
        return sqlsessionfactorybean.getObject();
    }

}