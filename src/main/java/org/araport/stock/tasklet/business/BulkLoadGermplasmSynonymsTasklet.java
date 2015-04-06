package org.araport.stock.tasklet.business;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
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
import org.araport.stock.executors.LoaderThreadPool;
import org.araport.stock.executors.ThreadPoolFactory;
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
public class BulkLoadGermplasmSynonymsTasklet implements Tasklet {

	private static final Log log = LogFactory
			.getLog(BulkLoadGermplasmSynonymsTasklet.class);
	
	private final static StopWatch timer = new StopWatch();
		
	Map<String, String> inputSqlTasks = new LinkedHashMap<String, String>();
	
	private static final String CREATE_SYNONYMS_LOOKUP_SQL_PATH = "/sql/transformations/stock_synonym/synonym_lookup_mv.sql";
	private static final String CREATE_SYNONYMS_LOOKUP_SQL = FileUtils
			.getSqlFileContents(CREATE_SYNONYMS_LOOKUP_SQL_PATH);

	private static final String NON_EXISITING_SYNONYMS_SQL_PATH = "/sql/transformations/stock_synonym/create_non_existing_germplasm_synonyms_mv.sql";
	private static final String NON_EXISITING_SYNONYMS_SQL = FileUtils
			.getSqlFileContents(NON_EXISITING_SYNONYMS_SQL_PATH);

	private static final String POPULATE_CHADO_SYNONYMS_SQL_PATH = "/sql/transformations/stock_synonym/populate_chado_synonyms.sql";
	private static final String POPULATE_CHADO_SYNONYMS_SQL = FileUtils
			.getSqlFileContents(POPULATE_CHADO_SYNONYMS_SQL_PATH);

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
		this.inputSqlTasks.put("GERMPLASM SYNONYMS LOOKUP", CREATE_SYNONYMS_LOOKUP_SQL);
		this.inputSqlTasks.put("NON EXISTING GERMPLASM SYNONYMS", NON_EXISITING_SYNONYMS_SQL);
		this.inputSqlTasks.put("POPULATE CHADO SYNONYMS", POPULATE_CHADO_SYNONYMS_SQL);


	}
	
	
}
