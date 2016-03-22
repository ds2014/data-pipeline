package org.araport.jcvi.stock.application;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySources(value = {@PropertySource("classpath:/batch.properties"), @PropertySource("classpath:/source.db.properties"), @PropertySource("classpath:/target.db.properties")})
public class DataSourceInfrastructureConfiguration implements InfrastructureConfiguration{

	@Autowired
	Environment environment;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	
	@Bean(name = "targetDataSource", destroyMethod = "close")
	public DataSource dataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(environment
				.getProperty("target.batch.jdbc.driver"));
		dataSource.setUrl(environment.getProperty("target.batch.jdbc.url"));
		dataSource.setUsername(environment.getProperty("target.batch.jdbc.user"));
		dataSource.setPassword(environment.getProperty("target.batch.jdbc.password"));
		return dataSource;
	}
	
}
