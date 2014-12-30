package org.araport.jcvi.stock.application;

import javax.sql.DataSource;

import org.araport.jcvi.stock.executors.TaskExecutorConfig;
import org.araport.stock.domain.Db;
import org.araport.stock.domain.DbXref;
import org.araport.stock.domain.SourceStockDrivingQuery;
import org.araport.stock.domain.Stock;
import org.jcvi.araport.stock.processor.DbXrefItemProcessor;
import org.jcvi.araport.stock.processor.DbXrefItemProcessor1;
import org.jcvi.araport.stock.reader.DbXrefItemReader;
import org.jcvi.araport.stock.reader.SourceStockDrivingQueryReader;
import org.jcvi.araport.stock.rowmapper.DbXrefRowMapper;
import org.jcvi.araport.stock.rowmapper.beans.RowMapperBeans;
import org.jcvi.araport.stock.tasklet.DbLookupTasklet;
import org.jcvi.araport.stock.tasklet.StockPropertiesCVTermLookupTasklet;
import org.jcvi.araport.stock.writer.DbXrefItemWriter;
import org.jcvi.araport.stock.listeners.ItemFailureLoggerListener;
import org.jcvi.araport.stock.listeners.LogProcessListener;
import org.jcvi.araport.stock.listeners.LogStepStartStopListener;
import org.jcvi.araport.stock.listeners.ProtocolListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@Import({ DataSourceInfrastructureConfiguration.class, DbXrefItemReader.class,
		SourceStockDrivingQueryReader.class, RowMapperBeans.class,
		TaskExecutorConfig.class, DbLookupTasklet.class,
		StockPropertiesCVTermLookupTasklet.class})

public class LoadStocksJobBatchConfiguration {

	public static final String STOCK_MASTER_LOADING_STEP = "stockMasterLoadingStep";
	public static final String DB_LOOKUP_LOADING_STEP = "dbLookupLoadingStep";
	public static final String DB_CVTERM_LOADING_STEP = "dbCVTermLookupLoadingStep";
	public static final String STOCK_CROSSREFERENCES_LOADING_STEP = "stockCrossReferencesLoadingStep";
	public static final String STOCK_PROPERTIES_LOADING_STEP = "stockPropertiesLoadingStep";
	public static final String GERMPLASM_MASTER_LOADING_STEP = "germplasmMasterLoadingStep";
	
	@Autowired
	private Environment env;
	
	@Autowired
    private JobBuilderFactory jobs;
	
	@Autowired
    private JobBuilderFactory jobBuilders;
	
	@Autowired
    protected StepBuilderFactory stepBuilders;
 
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    
    @Autowired
    private ItemReader<DbXref> dbReader;
    
    @Autowired
    private ItemReader <SourceStockDrivingQuery> sourceStockReader;
    
    @Autowired
    private ItemProcessor<SourceStockDrivingQuery, Stock> stockItemProcessor;
    
    @Autowired
    private ItemWriter<Stock> stockItemWriter;
    
    @Autowired
    private TaskExecutor taskExecutor;
    
            
	
	//@Autowired
	//JobLauncher jobLauncher;
	
//	@Autowired
	//JobRepository jobRepository;
	
	 @Autowired
	 JobExplorer jobExplorer;
	
	@Autowired 
	DataSource targetDataSource;
			
	public JobExplorer getJobExplorer() throws Exception {
		return jobExplorer;
	}

	public JobLauncher getJobLauncher() throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(getJobRepository());
		jobLauncher.afterPropertiesSet();
		return jobLauncher;
	}

	public JobRepository getJobRepository() throws Exception {
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
		factory.setDataSource(targetDataSource);
		factory.setTransactionManager(getTransactionManager());
		factory.afterPropertiesSet();
		return  (JobRepository) factory.getObject();
	}

	public PlatformTransactionManager getTransactionManager() throws Exception {
		return new DataSourceTransactionManager(targetDataSource);
	}
	
	// tag::jobstep[]
	@Bean
	public Job loadStocks(){
		return jobs.get("STOCK_MASTER_LOADING_STEP")
				.listener(protocolListener())
				//.start(stockMasterLoadingStep())
				//.next(dbLookupLoadingStep())
				.start(dbLookupLoadingStep())
				.next(stockMasterLoadingStep())
				.next(dbStockPropertiesCVTermLookup())
				.build();
	}	
	
	/*
	@Bean
	public Step step(){
		return stepBuilderFactory.get("step").listener(stepStartStopListener())
				.<DbXref,DbXref>chunk(1) //important to be one in this case to commit after every line read
				//.reader(dbXRefReader())
				.reader(dbReader)
				.processor(dbXrefProcessor())
				.writer(writer())
				.listener(logProcessListener())
				.faultTolerant()
				.build();
	}
	
	*/
	
	// last working config
	/*
	@Bean
	public Step step(){
		return stepBuilderFactory.get("step").listener(stepStartStopListener())
				.<SourceStockDrivingQuery,DbXref>chunk(50) //important to be one in this case to commit after every line read
				//.reader(dbXRefReader())
				.reader(sourceStockReader)
				.processor(dbXrefProcessor())
				.writer(writer())
				.listener(logProcessListener())
				.faultTolerant()
				.build();
	}
	*/
	
	
	@Bean
    public Step stockMasterLoadingStep() {
        return stepBuilders
                .get(STOCK_MASTER_LOADING_STEP)
                .listener(stepStartStopListener())
                .<SourceStockDrivingQuery,Stock>chunk(100) //important to be one in this case to commit after every line read
				//.reader(dbXRefReader())
				.reader(sourceStockReader)
				.processor(stockItemProcessor)
				.writer(stockItemWriter).taskExecutor(taskExecutor).throttleLimit(1)
				.listener(logProcessListener())
				 // .faultTolerant()
				.build();
    }
	
	@Bean
    public Step dbLookupLoadingStep() {
        return stepBuilders
                .get(DB_LOOKUP_LOADING_STEP)
                .tasklet(dbLookupTasklet())
				.build();
    }
	
		
	@Bean
    public Step dbStockPropertiesCVTermLookup() {
        return stepBuilders
                .get(DB_CVTERM_LOADING_STEP)
                .tasklet(dbCVTermLookupTasklet())
				.build();
    }
	
	 @Bean
	    public ItemWriter<DbXref> writer() {
	    	return new DbXrefItemWriter();
	    }
	 
	 @Bean
	 public DbLookupTasklet dbLookupTasklet(){
		 return new DbLookupTasklet();
	 }
	 
	 @Bean
	 public StockPropertiesCVTermLookupTasklet dbCVTermLookupTasklet(){
		 return new StockPropertiesCVTermLookupTasklet();
	 }
	
	/** configure the processor related stuff */
	 
	/*
    @Bean
    public ItemProcessor<DbXref, DbXref> dbXrefProcessor() {
        return new DbXrefItemProcessor();
    }
	*/
	 
    @Bean
    public ItemProcessor<SourceStockDrivingQuery, DbXref> dbXrefProcessor() {
        return new DbXrefItemProcessor1();
    }
	@Bean
	public ProtocolListener protocolListener(){
		return new ProtocolListener();
	}
			
		
	@Bean
	public LogProcessListener logProcessListener(){
		return new LogProcessListener();
	}
	
	@Bean
	public LogStepStartStopListener stepStartStopListener(){
		return new LogStepStartStopListener();
	}
	
	@Bean ItemFailureLoggerListener itemFailureListener(){
		return new ItemFailureLoggerListener();
	}

}
