package org.araport.jcvi.stock.application;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.araport.stock.domain.Db;
import org.araport.stock.domain.DbXref;
import org.araport.stock.domain.DbXrefSource;
import org.araport.stock.domain.SourceStockDrivingQuery;
import org.araport.stock.domain.Stock;
import org.araport.stock.domain.StockProperty;
import org.araport.stock.domain.StockPropertySource;
import org.araport.stock.executors.TaskExecutorConfig;
import org.araport.stock.flow.beans.FlowBeans;
import org.postgresql.util.PSQLException;
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
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.PartitionStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.PostgresPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.araport.stock.listeners.ItemFailureLoggerListener;
import org.araport.stock.listeners.LogProcessListener;
import org.araport.stock.listeners.LogStepStartStopListener;
import org.araport.stock.listeners.ProtocolListener;
import org.araport.stock.listeners.StagingStockPropertiesStepListener;
import org.araport.stock.partitioner.StockColumnRangePartitioner;
import org.araport.stock.policy.ExceptionSkipPolicy;
import org.araport.stock.policy.PolicyBean;
import org.araport.stock.policy.StockSkipListener;
import org.araport.stock.processor.DbXrefItemProcessor;
import org.araport.stock.processor.DbXrefItemProcessor1;
import org.araport.stock.reader.DbXrefItemReader;
import org.araport.stock.reader.DbXrefJdbcPagingItemReader;
import org.araport.stock.reader.SourceStockDrivingQueryReader;
import org.araport.stock.reader.StockPropertiesItemReader;
import org.araport.stock.reader.StockSourceJdbcPagingItemReader;
import org.araport.stock.rowmapper.DbXrefRowMapper;
import org.araport.stock.rowmapper.StockPropertiesSourceRowMapper;
import org.araport.stock.rowmapper.beans.RowMapperBeans;
import org.araport.stock.tasklet.business.BulkLoadOrganismTasklet;
import org.araport.stock.tasklet.business.BulkLoadStockPropertiesTasklet;
import org.araport.stock.tasklet.business.DbLookupTasklet;
import org.araport.stock.tasklet.business.DbXrefPrimaryStockAccessionsPostLoadTasklet;
import org.araport.stock.tasklet.business.GeneralModuleInitTasklet;
import org.araport.stock.tasklet.business.StockPostLoadingTasklet;
import org.araport.stock.tasklet.business.StockPropertiesCVTermLookupTasklet;
import org.araport.stock.tasklet.staging.BatchSchemaInitTasklet;
import org.araport.stock.tasklet.staging.StageDbXrefExistingStockAccessionsTasklet;
import org.araport.stock.tasklet.staging.StagingSchemaInitTasklet;
import org.araport.stock.tasklet.staging.StagingStockPropertiesTruncateTasklet;
import org.araport.stock.tasklet.staging.StockStagingPreloadingTasklet;
import org.araport.stock.writer.DbXrefItemWriter;
import org.araport.stock.writer.DbXrefJdbcBatchWriter;
import org.araport.stock.writer.StockJdbcBatchWriter;
import org.araport.stock.writer.StockPropertiesJdbcBatchWriter;

@Configuration
@EnableBatchProcessing
@Import({ DataSourceInfrastructureConfiguration.class, DbXrefItemReader.class,
		SourceStockDrivingQueryReader.class, StockPropertiesItemReader.class,
		RowMapperBeans.class, TaskExecutorConfig.class, DbLookupTasklet.class,
		StockPropertiesCVTermLookupTasklet.class,
		StagingStockPropertiesTruncateTasklet.class,
		BulkLoadStockPropertiesTasklet.class,
		StockPostLoadingTasklet.class,
		StockColumnRangePartitioner.class,
		StockPropertiesJdbcBatchWriter.class, DbXrefJdbcPagingItemReader.class, StockSourceJdbcPagingItemReader.class, DbXrefJdbcBatchWriter.class, StockJdbcBatchWriter.class, FlowBeans.class, PolicyBean.class })
@PropertySources(value = { @PropertySource("classpath:/partition.properties") })
public class LoadStocksJobBatchConfiguration {

	private static final Logger log = Logger
			.getLogger(LogStepStartStopListener.class);

	// Main Job
	public static final String MAIN_JOB = "mainJob";

	// Application Schemas
	public static final String BATCH_SCHEMA_INITIALIZATION_STEP = "batchSchemaInitStep";
	public static final String STAGING_DB_INITIALIZATION_STEP = "stagingSchemaInitStep";

	// General Module
	public static final String GENERAL_MODULE_INITIALIZATION_STEP = "generalModuleStep";
	
	//Organism
	public static final String ORGANISM_LOADING_STEP = "organismLoadingStep";

	//DbXref Primary Accessions
	public static final String DBXREF_PRIMARY_ACCESSIONS_STAGING_STEP = "dbXRefPrimaryAcessionsStagingStep";
	public static final String DBXREF_PRIMARY_ACCESSIONS_MASTER_LOADING_STEP = "dbXRefPrimaryAcessionsMasterLoadingStep";
	public static final String DBXREF_PRIMARY_ACCESSIONS_SLAVE_LOADING_STEP = "dbXRefPrimaryAcessionsSlaveLoadingStep";
	public static final String DBXREF_PRIMARY_ACCESSIONS_POST_LOADING_STEP = "dbXRefPrimaryAcessionsPostLoadingStep";
	
	//Stocks
	public static final String STOCKS_STAGING_STEP = "stockStagingStep";
	public static final String STOCK_MASTER_STEP = "stockMasterLoadingStep";
	public static final String STOCK_MASTER_LOADING_STEP = "stockMasterLoadingStep";
	public static final String STOCK_SLAVE_LOADING_STEP = "stockSlaveLoadingStep";
	public static final String STOCK_POST_LOADING_STEP = "stockPostLoadingStep";
	

	public static final String DB_LOOKUP_LOADING_STEP = "dbLookupLoadingStep";
	public static final String DB_CVTERM_LOADING_STEP = "dbCVTermLookupLoadingStep";
	public static final String STOCK_CROSSREFERENCES_LOADING_STEP = "stockCrossReferencesLoadingStep";

	public static final String STOCK_PROPERTIES_MASTER_STAGING_LOADING_STEP = "stockPropertiesMasterStagingLoadingStep";
	public static final String STOCK_PROPERTIES_STAGING_LOADING_STEP = "stockPropertiesStagingLoadingStep";
	public static final String STOCK_PROPERTIES_BULK_LOADING_STEP = "stockPropertiesBulkLoadingStep";
	
	public static final String STOCK_PROPERTIES_STAGING_CLEANUP_STEP = "stockPropertiesStagingCleanupStep";

	public static final String GERMPLASM_MASTER_LOADING_STEP = "germplasmMasterLoadingStep";

	@Autowired
	private Environment environment;

	@Autowired
	private ResourceLoader resourceLoader;
	
	// Stock Partition
	@Value("${stock.partitioner.table}")
	private String stockPartitionerTable;

	@Value("${stock.partitioner.where.clause}")
	private String stockPartitionerWhereClause;

	@Value("${stock.partitioner.column}")
	private String stockPartitionerColumn;

	@Value("${stock.partition.size}")
	private int stockPartitionCount;

	@Value("${stock.chunk.size}")
	private int stockChunkSize;

	// Stock Properties Partition
	@Value("${stockprop.partitioner.table}")
	private String stockPropPartitionerTable;

	@Value("${stockprop.partitioner.where.clause}")
	private String stockPropPartitionerWhereClause;

	@Value("${stockprop.partitioner.column}")
	private String stockPropPartitionerColumn;

	@Value("${stockprop.partition.size}")
	private int stockPropPartitionCount;

	@Value("${stockprop.chunk.size}")
	private int stockPropChunkSize;
	
	//DbXref Partition
	
	@Value("${dbxref.partitioner.table}")
	private String dbXrefPartitionerTable;

	@Value("${dbxref.partitioner.where.clause}")
	private String dbXrefPartitionerWhereClause;

	@Value("${dbxref.partitioner.column}")
	private String dbXrefPartitionerColumn;

	@Value("${dbxref.partition.size}")
	private int dbXrefPartitionCount;

	@Value("${dbxref.chunk.size}")
	private int dbXrefChunkSize;
	

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
	private ItemReader<SourceStockDrivingQuery> sourceStockReader;

	@Autowired
	private JdbcPagingItemReader<StockPropertySource> sourceStockPropertyReader;
	
	@Autowired
	JdbcPagingItemReader<DbXrefSource> dbXrefJdbcItemReader;
	
	@Autowired
	JdbcPagingItemReader<Stock> sourceStockBatchReader;

	@Autowired
	StockPropertiesSourceRowMapper stockPropertiesSourceMapper;

	@Autowired
	private ItemProcessor<SourceStockDrivingQuery, Stock> stockItemProcessor;

	@Autowired
	private ItemProcessor<StockPropertySource, StockProperty> stockPropertyItemProcessor;
	
	@Autowired
	private ItemProcessor <Stock, Stock> stockBatchItemProcessor;

	@Autowired
	private ItemWriter<Stock> stockItemWriter;

	@Autowired
	private ItemWriter<StockProperty> stockPropertyItemWriter;

	@Autowired
	ItemWriter<StockProperty> stockPropertyJdbcBatchWriter;
	
	@Autowired
	ItemWriter<DbXref> dbXrefJdbcBatchWriter;
	
	@Autowired
	ItemWriter<Stock> stockJdbcBatchWriter;
	
	@Autowired
	ItemProcessor <DbXrefSource, DbXref> dbXrefBatchProcessor;

	@Autowired
	BatchSchemaInitTasklet batchSchemaInitTasklet;

	@Autowired
	StagingSchemaInitTasklet stagingSchemaInitTasklet;

	@Autowired
	GeneralModuleInitTasklet generalModuleInitTasklet;
	
	@Autowired
	StageDbXrefExistingStockAccessionsTasklet stageDbXrefExistingStockAccessionsTasklet;
	
	@Autowired
	DbXrefPrimaryStockAccessionsPostLoadTasklet dbXrefPrimaryAccessionsPostLoadTasklet;
	
	@Autowired
	StockStagingPreloadingTasklet dbStockStagingPreloadingTasklet;
	
	@Autowired
	public StockPostLoadingTasklet stockPostLoadingTasklet;
	
	//Organism Tasklet
	@Autowired
	BulkLoadOrganismTasklet bulkLoadOrganismTasklet;

	@Autowired
	private TaskExecutor taskExecutor;

	@Autowired
	private TaskExecutor concurrentExecutor;

	@Autowired
	ExceptionSkipPolicy exceptionSkipPolicy;

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
		return (JobRepository) factory.getObject();
	}

	public PlatformTransactionManager getTransactionManager() throws Exception {
		return new DataSourceTransactionManager(targetDataSource);
	}

	// tag::jobstep[]
	@Bean
	public Job loadStocks() throws Exception {
		return jobs
				.get(MAIN_JOB)
				.listener(protocolListener())
				 .start(batchSchemaInitStep()).next(stagingSchemaInitStep())
				 .next(organismLoadingStep())
				//.next(generalModuleInitStep()).next(dbLookupLoadingStep())
				//.next(stageDbXrefExistingStockAccessionsStep())
				//.next(dbXrefMasterLoadingStep())
				//.next(dbXrefPrimaryAccessionsPostLoadingStep())
				//.next(stockStagingPreloadingTasklet())
				//.next(stockSourceMasterLoadingStep())
				//.next(stockPostLoadingTasklet())
				//.next(dbStockPropertiesCVTermLookup())
				//.next(stagingStockPropertiesCleanup())
				//.next(stepStockPropertyMaster())
				//.next(dbBulkLoadingStockProperties())
				.build();
	}

	/*
	 * @Bean public Step step(){ return
	 * stepBuilderFactory.get("step").listener(stepStartStopListener())
	 * .<DbXref,DbXref>chunk(1) //important to be one in this case to commit
	 * after every line read //.reader(dbXRefReader()) .reader(dbReader)
	 * .processor(dbXrefProcessor()) .writer(writer())
	 * .listener(logProcessListener()) .faultTolerant() .build(); }
	 */

	// last working config
	/*
	 * @Bean public Step step(){ return
	 * stepBuilderFactory.get("step").listener(stepStartStopListener())
	 * .<SourceStockDrivingQuery,DbXref>chunk(50) //important to be one in this
	 * case to commit after every line read //.reader(dbXRefReader())
	 * .reader(sourceStockReader) .processor(dbXrefProcessor())
	 * .writer(writer()) .listener(logProcessListener()) .faultTolerant()
	 * .build(); }
	 */

	@Bean
	public Step batchSchemaInitStep() {

		StepBuilder stepBuilder = stepBuilders
				.get(BATCH_SCHEMA_INITIALIZATION_STEP);

		Step step = stepBuilder.tasklet(batchSchemaInitTasklet).build();

		return step;

	}

	@Bean
	public Step stagingSchemaInitStep() {

		StepBuilder stepBuilder = stepBuilders
				.get(STAGING_DB_INITIALIZATION_STEP);

		Step step = stepBuilder.tasklet(stagingSchemaInitTasklet).build();

		return step;

	}

	@Bean
	public Step generalModuleInitStep() {

		StepBuilder stepBuilder = stepBuilders
				.get(GENERAL_MODULE_INITIALIZATION_STEP);

		Step step = stepBuilder.tasklet(generalModuleInitTasklet).build();
		
		return step;

	}
	
	@Bean
	public Step stageDbXrefExistingStockAccessionsStep() {

		StepBuilder stepBuilder = stepBuilders
				.get(DBXREF_PRIMARY_ACCESSIONS_STAGING_STEP);

		Step step = stepBuilder.tasklet(stageDbXrefExistingStockAccessionsTasklet).build();
		
		return step;

	}
	
	
	@Bean
	public Step stockMasterLoadingStep() {
		return stepBuilders
				.get(STOCK_MASTER_LOADING_STEP)
				.listener(stepStartStopListener())
				.<SourceStockDrivingQuery, Stock> chunk(10000)
				.faultTolerant()
				.skipPolicy(exceptionSkipPolicy)
				.skip(org.springframework.dao.DataIntegrityViolationException.class)
				.skip(PSQLException.class)
				// .reader(dbXRefReader())
				.reader(sourceStockReader).processor(stockItemProcessor)
				.writer(stockItemWriter).listener(logProcessListener()).build();
	}

		
	@Bean
	public Step dbLookupLoadingStep() {
		return stepBuilders.get(DB_LOOKUP_LOADING_STEP)
				.tasklet(dbLookupTasklet()).build();
	}

	@Bean
	public Step dbStockPropertiesCVTermLookup() {
		return stepBuilders.get(DB_CVTERM_LOADING_STEP)
				.tasklet(dbCVTermLookupTasklet()).build();
	}

	@Bean
	public Step stockPropertiesStagingLoadingStep() {
		return stepBuilders
				.get(STOCK_PROPERTIES_STAGING_LOADING_STEP)
				.listener(stepStartStopListener())
				.<StockPropertySource, StockProperty> chunk(stockPropChunkSize)
				.faultTolerant()
				.skipPolicy(exceptionSkipPolicy)
				.skip(org.springframework.dao.DataIntegrityViolationException.class)
				.skip(PSQLException.class).reader(sourceStockPropertyReader)
				.processor(stockPropertyItemProcessor)
				.writer(stockPropertyJdbcBatchWriter)
				.listener(logProcessListener())
				.listener(stagingStockPropertiesStepListener()).build();
	}


	@Bean
	public Step dbXrefMasterLoadingStep() throws Exception {

		StepBuilder stepDBXrefMaster = stepBuilders
				.get(DBXREF_PRIMARY_ACCESSIONS_MASTER_LOADING_STEP);

		log.info("Grid Partition Count: " + dbXrefPartitionCount);

		Step masterStep = stepDBXrefMaster
				.partitioner(dbXrefSlaveLoadingStep())
				.partitioner(DBXREF_PRIMARY_ACCESSIONS_SLAVE_LOADING_STEP,
						dbXrefColumnRangePartitioner())
				.gridSize(dbXrefPartitionCount)
				.taskExecutor(concurrentExecutor).build();

		return masterStep;
	}
	
	@Bean
	public Step dbXrefSlaveLoadingStep() {
		return stepBuilders
				.get(DBXREF_PRIMARY_ACCESSIONS_SLAVE_LOADING_STEP)
				.listener(stepStartStopListener())
				.<DbXrefSource, DbXref> chunk(dbXrefChunkSize)
				.faultTolerant()
				.skipPolicy(exceptionSkipPolicy)
				.skip(org.springframework.dao.DataIntegrityViolationException.class)
				.skip(PSQLException.class).reader(dbXrefJdbcItemReader)
				.processor(dbXrefBatchProcessor)
				.writer(dbXrefJdbcBatchWriter)
				.listener(logProcessListener())
				.listener(stagingStockPropertiesStepListener()).build();
	}

	@Bean
	public Step stockSourceMasterLoadingStep() throws Exception {

		StepBuilder stepStockMaster = stepBuilders
				.get(STOCK_MASTER_STEP);

		log.info("Grid Partition Count: " + stockPartitionCount);

		Step masterStep = stepStockMaster
				.partitioner(stockSlaveLoadingStep())
				.partitioner(STOCK_SLAVE_LOADING_STEP,
						stockSourceColumnRangePartitioner())
				.gridSize(stockPartitionCount)
				.taskExecutor(concurrentExecutor).build();

		return masterStep;
	}
		
	@Bean
	public Step stockSlaveLoadingStep() {
		return stepBuilders
				.get(STOCK_SLAVE_LOADING_STEP)
				.listener(stepStartStopListener())
				.<Stock, Stock> chunk(stockChunkSize)
				.faultTolerant()
				.skipPolicy(exceptionSkipPolicy)
				.skip(org.springframework.dao.DataIntegrityViolationException.class)
				.skip(PSQLException.class).reader(sourceStockBatchReader)
				.processor(stockBatchItemProcessor)
				.writer(stockJdbcBatchWriter)
				.listener(logProcessListener())
				.listener(stagingStockPropertiesStepListener()).build();
	}
	
	@Bean
	public Step dbXrefPrimaryAccessionsPostLoadingStep() {
		return stepBuilders.get(DBXREF_PRIMARY_ACCESSIONS_POST_LOADING_STEP)
				.tasklet(dbXrefPrimaryAccessionsPostLoadTasklet).build();
	}
	
	@Bean
	public Step stockStagingPreloadingTasklet() {
		return stepBuilders.get(STOCKS_STAGING_STEP)
				.tasklet(dbStockStagingPreloadingTasklet).build();
	}
	
	@Bean
	public Step stockPostLoadingTasklet() {
		return stepBuilders.get(STOCK_POST_LOADING_STEP)
				.tasklet(stockPostLoadingTasklet).build();
	}
	
		
	@Bean
	public Step dbBulkLoadingStockProperties() {
		return stepBuilders.get(STOCK_PROPERTIES_BULK_LOADING_STEP)
				.tasklet(bulkLoadStockProperties()).build();
	}

	@Bean
	public Step stagingStockPropertiesCleanup() {

		return stepBuilders.get(STOCK_PROPERTIES_STAGING_CLEANUP_STEP)
				.tasklet(stagingStockPropertiesTruncateTasklet()).build();

	}
	
	@Bean
	public Step organismLoadingStep() {

		return stepBuilders.get(ORGANISM_LOADING_STEP)
				.tasklet(bulkLoadOrganismTasklet).build();

	}

	@Bean
	public ItemWriter<DbXref> writer() {
		return new DbXrefItemWriter();
	}

	@Bean
	public DbLookupTasklet dbLookupTasklet() {
		return new DbLookupTasklet();
	}

	@Bean
	public StockPropertiesCVTermLookupTasklet dbCVTermLookupTasklet() {
		return new StockPropertiesCVTermLookupTasklet();
	}

	@Bean
	public BulkLoadStockPropertiesTasklet bulkLoadStockProperties() {
		return new BulkLoadStockPropertiesTasklet();
	}

	@Bean
	public StagingStockPropertiesTruncateTasklet stagingStockPropertiesTruncateTasklet() {
		return new StagingStockPropertiesTruncateTasklet();
	}

	@Bean
	public StockColumnRangePartitioner stockColumnRangePartitioner() {
		StockColumnRangePartitioner partitioner = new StockColumnRangePartitioner();
		partitioner.setDataSource(targetDataSource);
		partitioner.setTable(stockPropPartitionerTable);
		partitioner.setColumn(stockPropPartitionerColumn);
		
		return partitioner;
	}
	
	@Bean
	public StockColumnRangePartitioner dbXrefColumnRangePartitioner() {
		StockColumnRangePartitioner partitioner = new StockColumnRangePartitioner();
		partitioner.setDataSource(targetDataSource);
		partitioner.setTable(dbXrefPartitionerTable);
		partitioner.setColumn(dbXrefPartitionerColumn);
		
		return partitioner;
	}
	
	@Bean
	public StockColumnRangePartitioner stockSourceColumnRangePartitioner() {
		StockColumnRangePartitioner partitioner = new StockColumnRangePartitioner();
		partitioner.setDataSource(targetDataSource);
		partitioner.setTable(stockPartitionerTable);
		partitioner.setColumn(stockPartitionerColumn);
		
		return partitioner;
	}


	@Bean
	public Step stepStockPropertyMaster() throws Exception {

		StepBuilder stepStockPropertyMaster = stepBuilders
				.get(STOCK_PROPERTIES_MASTER_STAGING_LOADING_STEP);

		log.info("Grid Partition Count: " + stockPropPartitionCount);

		Step masterStep = stepStockPropertyMaster
				.partitioner(stockPropertiesStagingLoadingStep())
				.partitioner(STOCK_PROPERTIES_STAGING_LOADING_STEP,
						stockColumnRangePartitioner())
				.gridSize(stockPropPartitionCount)
				.taskExecutor(concurrentExecutor).build();

		return masterStep;
	}

	/*
	 * @Bean public ItemProcessor<DbXref, DbXref> dbXrefProcessor() { return new
	 * DbXrefItemProcessor(); }
	 */

	@Bean
	public ItemProcessor<SourceStockDrivingQuery, DbXref> dbXrefProcessor() {
		return new DbXrefItemProcessor1();
	}

	@Bean
	public ProtocolListener protocolListener() {
		return new ProtocolListener();
	}

	@Bean
	public LogProcessListener logProcessListener() {
		return new LogProcessListener();
	}

	@Bean
	public LogStepStartStopListener stepStartStopListener() {
		return new LogStepStartStopListener();
	}

	@Bean
	ItemFailureLoggerListener itemFailureListener() {
		return new ItemFailureLoggerListener();
	}

	@Bean
	public StagingStockPropertiesStepListener stagingStockPropertiesStepListener() {
		return new StagingStockPropertiesStepListener();
	}

	@Bean
	public StockPropertiesItemReader stockPropertiesItemReaderListener() {
		return new StockPropertiesItemReader();
	}

}
