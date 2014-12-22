package org.jcvi.araport.stock.reader.batch;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@Import({DataSourceConfiguration.class})
public class BatchConfiguration {

	
	@Autowired private JobBuilderFactory jobs;
    @Autowired private StepBuilderFactory steps;
    @Autowired private PlatformTransactionManager transactionManager;
    
	@Autowired
	@Qualifier("jobRepository")
	private JobRepository jobRepository;

	/**
	@Bean
	public DataSource dataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl("jdbc:postgresql://localhost:5432/stockbatch");
		dataSource.setUsername("tripal2");
		dataSource.setPassword("tripal2");
		return dataSource;
	}

**/
	@Bean
	public SimpleJobLauncher jobLauncher() {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository);
		return jobLauncher;
	}


}
