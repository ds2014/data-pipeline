package org.araport.stock.tasklet.business;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.stock.dao.GeneralDao;
import org.araport.stock.dao.impl.CVDaoImpl;
import org.araport.stock.dao.impl.CVTermDaoImpl;
import org.araport.stock.dao.impl.DbDaoImpl;
import org.araport.stock.dao.impl.DbXrefDaoImpl;
import org.araport.stock.dao.impl.GeneralDaoImpl;
import org.araport.stock.dao.impl.StockDaoImpl;
import org.araport.stock.dataloader.DataService;
import org.araport.stock.dataloader.SQLTask;
import org.araport.stock.utils.FileUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
@StepScope
@Import({ DataSourceInfrastructureConfiguration.class, GeneralDaoImpl.class })
public class BulkLoadStockSynonymsTasklet implements Tasklet {

	private static final Log log = LogFactory
			.getLog(BulkLoadStockSynonymsTasklet.class);
	
	Map<String, String> inputSqlTasks = new LinkedHashMap<String, String>();
	
	private final static StopWatch timer = new StopWatch();

	private static final String CANDIDATE_MATCHING_RECORD_SQL_PATH = "/sql/transformations/stock_synonym/create_candidate_matching_records_mv.sql";
	private static final String CANDIDATE_MATCHING_RECORD_SQL = FileUtils
			.getSqlFileContents(CANDIDATE_MATCHING_RECORD_SQL_PATH);
	
	private static final String EXISTING_SYNONYMS_SQL_PATH = "/sql/transformations/stock_synonym/create_existing_synonyms_mv.sql";
	private static final String EXISTING_SYNONYMS_SQL = FileUtils
			.getSqlFileContents(EXISTING_SYNONYMS_SQL_PATH);
	
	private static final String SOURCE_SYNONYMS_SQL_PATH = "/sql/transformations/stock_synonym/create_synonyms_source_mv.sql";
	private static final String SOURCE_SYNONYMS_SQL = FileUtils
			.getSqlFileContents(SOURCE_SYNONYMS_SQL_PATH);
	
	private static final String NON_EXISITING_STOCK_SYNONYMS_SQL_PATH = "/sql/transformations/stock_synonym/create_existing_synonyms_multi_mv.sql";
	private static final String NON_EXISITING_STOCK_SYNONYMS_SQL = FileUtils
			.getSqlFileContents(NON_EXISITING_STOCK_SYNONYMS_SQL_PATH);

	private static final String POPULATE_STOCK_SYNONYMS_SQL_PATH = "/sql/transformations/stock_synonym/populate_chado_stock_synonyms.sql";
	private static final String POPULATE_STOCK_SYNONYMS_SQL = FileUtils
			.getSqlFileContents(POPULATE_STOCK_SYNONYMS_SQL_PATH);

	@Autowired
	Environment environment;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	DataSource targetDataSource;

	private GeneralDao generalDao;

	@Override
	public RepeatStatus execute(StepContribution step, ChunkContext context)
			throws Exception {

for (Map.Entry<String, String> item : inputSqlTasks.entrySet()) {
			
			final String sqlStmt = item.getValue();
			String taskId = item.getKey();
			
			log.info("Submitting Task : " + taskId + ";SQL Stmt: "
					+ sqlStmt);
			
			timer.reset();
			timer.start();
			
			SQLTask sqlTask = new SQLTask(sqlStmt, taskId, this.targetDataSource, generalDao);
			
			final Future<String> futureSqlTask = DataService
					.getDataServicePool().submit(sqlTask);
			
			String result = sqlTask.getResult(futureSqlTask, sqlTask.getMessage());
			
			timer.stop();
			
			log.info("SQL Task has been completed. Task Id " + sqlTask.getMessage() 
					+ "; Total time taken. " + timer.toString() + " Task Status:" + result);
			
		}
		
		
		return RepeatStatus.FINISHED;

	}

	@PostConstruct
	public void setDao() {

		this.generalDao = new GeneralDaoImpl();
		this.generalDao.setDataSource(targetDataSource);
		
		this.inputSqlTasks.clear();
		this.inputSqlTasks.put("Task 1: CANDIDATE_MATCHING_RECORD_SQL", CANDIDATE_MATCHING_RECORD_SQL);
		this.inputSqlTasks.put("Task 2: EXISTING_SYNONYMS_SQL", EXISTING_SYNONYMS_SQL);
		this.inputSqlTasks.put("Task 3: SOURCE_SYNONYMS_SQL", SOURCE_SYNONYMS_SQL);
		this.inputSqlTasks.put("Task 4: NON_EXISITING_STOCK_SYNONYMS_SQL", NON_EXISITING_STOCK_SYNONYMS_SQL);
		this.inputSqlTasks.put("Task 5: POPULATE_STOCK_SYNONYMS_SQL", POPULATE_STOCK_SYNONYMS_SQL);

	}

}
