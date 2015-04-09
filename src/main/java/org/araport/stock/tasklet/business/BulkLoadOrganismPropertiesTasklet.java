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
public class BulkLoadOrganismPropertiesTasklet implements Tasklet {

	private static final Log log = LogFactory
			.getLog(BulkLoadOrganismPropertiesTasklet.class);
	
	Map<String, String> inputSqlTasks = new LinkedHashMap<String, String>();
	
	private final static StopWatch timer = new StopWatch();

	private static final String ORGANISM_LOOKUP_MV_SQL_PATH = "/sql/transformations/organism/create_organism_lookup_mv.sql";
	private static final String ORGANISM_LOOKUP_MV_SQL = FileUtils
			.getSqlFileContents(ORGANISM_LOOKUP_MV_SQL_PATH);
	
	private static final String ORGANISM_PROPERTIES_MV_SQL_PATH = "/sql/transformations/organism/organism_properties_mv.sql";
	private static final String ORGANISM_PROPERTIES_MV_SQL = FileUtils
			.getSqlFileContents(ORGANISM_PROPERTIES_MV_SQL_PATH);
	
	private static final String GEO_LOCATION_MV_SQL_PATH = "/sql/transformations/organism/geolocation_mv.sql";
	private static final String GEO_LOCATION_MV_SQL = FileUtils
			.getSqlFileContents(GEO_LOCATION_MV_SQL_PATH);
	
	private static final String GEO_LOCATION_CVTERM_MV_SQL_PATH = "/sql/transformations/organism/geolocation_cvterm.sql";
	private static final String GEO_LOCATION_CVTERM_MV_SQL = FileUtils
			.getSqlFileContents(GEO_LOCATION_CVTERM_MV_SQL_PATH);
	
	private static final String GEO_LOCATION_PROP_MV_SQL_PATH = "/sql/transformations/organism/geolocationprop_mv.sql";
	private static final String GEO_LOCATION_PROP_MV_SQL = FileUtils
			.getSqlFileContents(GEO_LOCATION_PROP_MV_SQL_PATH);
	
	private static final String LOAD_GEO_LOCATION_SQL_PATH = "/sql/transformations/organism/load_geolocation.sql";
	private static final String LOAD_GEO_LOCATION_SQL = FileUtils
			.getSqlFileContents(LOAD_GEO_LOCATION_SQL_PATH);
	
	private static final String LOAD_GEO_LOCATION_PROP_SQL_PATH = "/sql/transformations/organism/load_geolocationprop.sql";
	private static final String LOAD_GEO_LOCATION_PROP_SQL = FileUtils
			.getSqlFileContents(LOAD_GEO_LOCATION_PROP_SQL_PATH);
	
	private static final String LOAD_GEO_LOCATION_CVTERM_SQL_PATH = "/sql/transformations/organism/load_geolocation_cvterm.sql";
	private static final String LOAD_GEO_LOCATION_CVTERM_SQL = FileUtils
			.getSqlFileContents(LOAD_GEO_LOCATION_CVTERM_SQL_PATH);
	
	private static final String LOAD_ORGANISM_PROP_SQL_PATH = "/sql/transformations/organism/load_organism_properties.sql";
	private static final String LOAD_ORGANISM_PROP_SQL = FileUtils
			.getSqlFileContents(LOAD_ORGANISM_PROP_SQL_PATH);
	
	private static final String LOAD_ORGANISM_GEO_LOCATION_SQL_PATH = "/sql/transformations/organism/load_organism_geolocation.sql";
	private static final String LOAD_ORGANISM_GEO_LOCATION_SQL = FileUtils
			.getSqlFileContents(LOAD_ORGANISM_GEO_LOCATION_SQL_PATH );
	

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
		this.inputSqlTasks.put("Task 1: ORGANISM_LOOKUP_MV_SQL", ORGANISM_LOOKUP_MV_SQL);
		this.inputSqlTasks.put("Task 2: ORGANISM_PROPERTIES_MV_SQL", ORGANISM_PROPERTIES_MV_SQL);
		this.inputSqlTasks.put("Task 3: GEO_LOCATION_MV_SQL", GEO_LOCATION_MV_SQL);
		this.inputSqlTasks.put("Task 4: GEO_LOCATION_CVTERM_MV_SQL", GEO_LOCATION_CVTERM_MV_SQL);
		this.inputSqlTasks.put("Task 5: GEO_LOCATION_PROP_MV_SQL", GEO_LOCATION_PROP_MV_SQL);
		
		this.inputSqlTasks.put("Task 6: LOAD_GEO_LOCATION_SQL", LOAD_GEO_LOCATION_SQL);
		this.inputSqlTasks.put("Task 7: LOAD_GEO_LOCATION_PROP_SQL", LOAD_GEO_LOCATION_PROP_SQL);
		this.inputSqlTasks.put("Task 8: LOAD_GEO_LOCATION_CVTERM_SQL",LOAD_GEO_LOCATION_CVTERM_SQL);
		this.inputSqlTasks.put("Task 9: LOAD_ORGANISM_GEO_LOCATION_SQL",LOAD_ORGANISM_GEO_LOCATION_SQL);

	}

}
