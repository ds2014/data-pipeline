package org.araport.stock.dataloader;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.stock.dao.GeneralDao;
import org.araport.stock.dao.impl.GeneralDaoImpl;
import org.araport.stock.tasklet.business.BulkLoadGermplasmSynonymsTasklet;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
@StepScope
@Import({ DataSourceInfrastructureConfiguration.class, GeneralDaoImpl.class })
public class SQLTask implements Callable<String>{

	private static final Log log = LogFactory
			.getLog(SQLTask.class);
	
	private final static StopWatch timer = new StopWatch();
	
	private String sqlStmt;
	private String message;
		
	private String status = "success";
		
	@Autowired
	Environment environment;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	DataSource targetDataSource;

	private GeneralDao generalDao;
	
	public SQLTask(){
		
	}
	
	public SQLTask(String sqlStmt, String message, DataSource dataSource, GeneralDao generalDao){
		this.sqlStmt = sqlStmt;
		this.message = message;
		setDao(generalDao);
	}
	
	
	public void setDao(GeneralDao generalDao) {
		this.generalDao = generalDao;
	}
	
	
	@Override
	public String call() throws Exception {
		
		timer.reset();
		timer.start();

		Exception exception = null;
		String errorMessage = null;
		
		log.info("Task Id : " + message);
		log.info("SQL Statement: " + sqlStmt);
		
		
		
				try {
					generalDao.executeSQL(sqlStmt);
				} catch (Exception e) {

					errorMessage = "Error executing SQL: "
							+ sqlStmt
							+ " Message: " + e.getMessage() + "; Cause: "
							+ e.getCause();
					e.printStackTrace();
					log.error(errorMessage);
					
				} finally {
					if (exception != null) {

						log.error(errorMessage);
						
						this.status = "failure";

					}

					timer.stop();
					
					log.info("SQL Task has been completed. Task Id " + message
							+ "; Total time taken. " + timer.toString() + " Task Status:" + status);
					
				}
			
	
		return status;
	}
	
	
	public String getStatus() {
		return status;
	}
	
	public String getSqlStmt() {
		return sqlStmt;
	}

	public String getMessage() {
		return message;
	}
	
public String getResult(Future<String> sqlTask, final String taskId){
		
		String task = new String();
		
		while (true) {
			if (sqlTask.isDone()) {

				log.info("Task is executed:" + "Task ID : = " + taskId);
				break;
			}
			if (!sqlTask.isDone()) {
				try {
					task= sqlTask.get();
				} catch (InterruptedException e) {
					log.error("Error executing task: " + taskId);
				} catch (ExecutionException e) {
					log.error("Error executing task: " +taskId);
					e.printStackTrace();
				}

				log.info("Waiting for task being executed. Task ID : = "
						+ taskId);
			}

		}
	
		return task;
		
	}

	
}


