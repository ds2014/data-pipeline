package org.jcvi.araport.stock.reader.batch;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

@Configuration

@EnableTransactionManagement
@PropertySources(value = {@PropertySource("classpath:/batch.properties"), @PropertySource("classpath:/tair.db.properties")}) 

public class DataSourceConfiguration   {

	@Autowired
	private Environment environment;
	
	@Autowired
	private ResourceLoader resourceLoader;

	@PostConstruct
	protected void initialize() {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(resourceLoader.getResource(environment
				.getProperty("batch.schema.script")));
		populator.setContinueOnError(true);
		DatabasePopulatorUtils.execute(populator, batchDataSource());
	}

	@Bean(name = "batchDataSource", destroyMethod = "close")
	@Primary
	public DataSource batchDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(environment
				.getProperty("batch.jdbc.driver"));
		dataSource.setUrl(environment.getProperty("batch.jdbc.url"));
		dataSource.setUsername(environment.getProperty("batch.jdbc.user"));
		dataSource.setPassword(environment.getProperty("batch.jdbc.password"));
		return dataSource;
	}

	@Bean(name = "tairDataSource", destroyMethod = "close")
	public DataSource tairDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(environment
				.getProperty("tair.batch.jdbc.driver"));
		dataSource.setUrl(environment.getProperty("tair.batch.jdbc.url"));
		dataSource.setUsername(environment.getProperty("tair.batch.jdbc.user"));
		dataSource.setPassword(environment.getProperty("tair.batch.jdbc.password"));
		return dataSource;
	}
	
	
	@Bean
    public JdbcTemplate batchJdbcTemplate(@Qualifier("batchDataSource")  final DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
	
	@Bean
    public JdbcTemplate tairJdbcTemplate(@Qualifier("tairDataSource")  final DataSource dataSource) {
        return new JdbcTemplate(tairDataSource());
	}

	
}
