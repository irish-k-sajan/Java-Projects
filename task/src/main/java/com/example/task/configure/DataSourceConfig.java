package com.example.task.configure;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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
    @Primary
    public SqlSessionFactory sqlSessionFactory() {
        SqlSessionFactoryBean sqlsessionfactorybean = new SqlSessionFactoryBean();
        sqlsessionfactorybean.setDataSource(getDataSource());
        try {
			return sqlsessionfactorybean.getObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }

}