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
public class BulkLoadStockDbxrefAccessionsTasklet implements Tasklet {

	private static final Log log = LogFactory
			.getLog(BulkLoadStockDbxrefAccessionsTasklet.class);
	
	Map<String, String> inputSqlTasks = new LinkedHashMap<String, String>();
	
	private final static StopWatch timer = new StopWatch();

	private static final String NON_EXISITING_STOCK_ACCESSIONS_SQL_PATH = "/sql/transformations/stock_dbxref/create_not_existing_stock_accessions_chado_dbxref_mv.sql";
	private static final String NON_EXISITING_STOCK_ACCESSIONS_SQL = FileUtils
			.getSqlFileContents(NON_EXISITING_STOCK_ACCESSIONS_SQL_PATH);

	private static final String POPULATE_STOCK_ACCESSIONS_SQL_PATH = "/sql/transformations/stock_dbxref/populate_dbxref_stock_accessions.sql";
	private static final String POPULATE_STOCK_ACCESSIONS_SQL = FileUtils
			.getSqlFileContents(POPULATE_STOCK_ACCESSIONS_SQL_PATH);
	
	private static final String NON_EXISITING_STOCK_DBXREF_SQL_PATH = "/sql/transformations/stock_dbxref/create_not_existing_stock_dbxref_mv.sql";
	private static final String NON_EXISITING_STOCK_DBXREF_SQL = FileUtils
			.getSqlFileContents(NON_EXISITING_STOCK_DBXREF_SQL_PATH);
	
	private static final String POPULATE_STOCK_DBXREF_SQL_PATH = "/sql/transformations/stock_dbxref/populate_stock_dbxref.sql";
	private static final String POPULATE_STOCK_DBXREF_SQL = FileUtils
			.getSqlFileContents(POPULATE_STOCK_DBXREF_SQL_PATH);
	
	private static final String EXTERNAL_URL_TRANSFORMATION_SQL_PATH = "/sql/transformations/stock_dbxref/external_url_transformation.sql";
	private static final String EXTERNAL_URL_TRANSFORMATION_SQL = FileUtils
			.getSqlFileContents(EXTERNAL_URL_TRANSFORMATION_SQL_PATH);
	
	private static final String EXTERNAL_DB_TRANSFORMATION_SQL_PATH = "/sql/transformations/stock_dbxref/external_db_transformation.sql";
	private static final String EXTERNAL_DB_TRANSFORMATION_SQL = FileUtils
			.getSqlFileContents(EXTERNAL_DB_TRANSFORMATION_SQL_PATH);
	
	private static final String EXTERNAL_DBXREF_TRANSFORMATION_SQL_PATH = "/sql/transformations/stock_dbxref/external_dbxref_transformation.sql";
	private static final String EXTERNAL_DBXREF_TRANSFORMATION_SQL = FileUtils
			.getSqlFileContents(EXTERNAL_DBXREF_TRANSFORMATION_SQL_PATH);
	
	private static final String LOAD_EXTERNAL_DB_SQL_PATH = "/sql/transformations/stock_dbxref/load_external_db.sql";
	private static final String LOAD_EXTERNAL_DB_SQL = FileUtils
			.getSqlFileContents(LOAD_EXTERNAL_DB_SQL_PATH);
	
	private static final String LOAD_EXTERNAL_DBXREF_SQL_PATH = "/sql/transformations/stock_dbxref/load_external_dbxref.sql";
	private static final String LOAD_EXTERNAL_DBXREF_SQL = FileUtils
			.getSqlFileContents(LOAD_EXTERNAL_DBXREF_SQL_PATH);

			
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
		this.inputSqlTasks.put("Task 1: NON_EXISITING_STOCK_ACCESSIONS_SQL",NON_EXISITING_STOCK_ACCESSIONS_SQL);
	    this.inputSqlTasks.put("Task 2: POPULATE_STOCK_ACCESSIONS_SQL", POPULATE_STOCK_ACCESSIONS_SQL);
	    	    
	    this.inputSqlTasks.put("Task 3: EXTERNAL_URL_TRANSFORMATION_SQL",EXTERNAL_URL_TRANSFORMATION_SQL);
	    
	    this.inputSqlTasks.put("Task 4: EXTERNAL_DB_TRANSFORMATION_SQL",EXTERNAL_DB_TRANSFORMATION_SQL);
	    
		this.inputSqlTasks.put("Task 5: EXTERNAL_DBXREF_TRANSFORMATION_SQL",EXTERNAL_DBXREF_TRANSFORMATION_SQL);
		this.inputSqlTasks.put("Task 6: LOAD_EXTERNAL_DB_SQL",LOAD_EXTERNAL_DB_SQL);
		this.inputSqlTasks.put("Task 7: LOAD_EXTERNAL_DBXREF_SQL",LOAD_EXTERNAL_DBXREF_SQL);
		
		this.inputSqlTasks.put("Task 8: NON_EXISITING_STOCK_DBXREF_SQL",NON_EXISITING_STOCK_DBXREF_SQL);
	    this.inputSqlTasks.put("Task 9: POPULATE_STOCK_DBXREF_SQL",POPULATE_STOCK_DBXREF_SQL);
	    
		
				
	}

}
