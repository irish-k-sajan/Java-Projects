package com.example.task.configure;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class h2DataSourceConfig {
    
    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder<?> datasourcebuilder = DataSourceBuilder.create();
        datasourcebuilder.driverClassName("org.h2.Driver");
        datasourcebuilder.url("jdbc:h2:mem:testdb");
        datasourcebuilder.username("sa");
        datasourcebuilder.password("password");
        return datasourcebuilder.build();
    }

    @Bean(name = "h2sqlsession")
    public SqlSessionFactory h2sqlSessionFactory() {
    	try {
        SqlSessionFactoryBean sqlsessionfactorybean = new SqlSessionFactoryBean();
        sqlsessionfactorybean.setDataSource(getDataSource());
       // System.out.println(sqlsessionfactorybean.getObject().toString());
        return sqlsessionfactorybean.getObject();
    	}
    	
    	catch(Exception e)
    	
    	{
    		System.out.println(e.getMessage());
        	return null;
    	}

        
    }
}