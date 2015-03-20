package org.araport.stock.tasklet.staging;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.stock.dao.GeneralDao;
import org.araport.stock.dao.impl.GeneralDaoImpl;
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
public class StagingSchemaInitTasklet implements Tasklet{

	private static final Log log = LogFactory
			.getLog(StagingSchemaInitTasklet.class);

	private static final String STAGING_SCHEMA_INIT_SQL_PATH = "/sql/schema/staging/init_staging_schema.sql";
	private static final String STAGING_SCHEMA_INIT_SQL = FileUtils
			.getSqlFileContents(STAGING_SCHEMA_INIT_SQL_PATH);
	
	private static final String STAGING_TABLES_INIT_SQL_PATH = "/sql/schema/staging/init_staging_tables.sql";
	private static final String STAGING_TABLES_INIT_SQL = FileUtils
			.getSqlFileContents(STAGING_TABLES_INIT_SQL_PATH);
	
	private static final String STAGING_STOCKPROP_MVVIEW_ALL_PATH = "/sql/schema/staging/mvviews/create_mv_view_stock_properties_all.sql";
	private static final String STAGING_STOCKPROP_MVVIEW_ALL_SQL = FileUtils
			.getSqlFileContents(STAGING_STOCKPROP_MVVIEW_ALL_PATH);
	
	
	private static final String STAGING_STOCKPROP_MVVIEW_EXISTING_PATH = "/sql/schema/staging/mvviews/create_mv_view_stock_properties_existing.sql";
	private static final String STAGING_STOCKPROP_MVVIEW_EXISTING_SQL = FileUtils
			.getSqlFileContents(STAGING_STOCKPROP_MVVIEW_EXISTING_PATH);
	
	private static final String STAGING_STOCKPROP_INDEX_MVVIEW_ALL_PATH = "/sql/schema/staging/mvviews/create_index_mv_stock_properties_all.sql";
	private static final String STAGING_STOCKPROP_INDEX_MVVIEW_ALL_SQL = FileUtils
			.getSqlFileContents(STAGING_STOCKPROP_INDEX_MVVIEW_ALL_PATH);
	
	private static final String STAGING_STOCKPROP_INDEX_MVVIEW_EXISTING_PATH = "/sql/schema/staging/mvviews/create_index_mv_stock_properties_existing.sql";
	private static final String STAGING_STOCKPROP_INDEX_MVVIEW_EXISTINGL_SQL = FileUtils
			.getSqlFileContents(STAGING_STOCKPROP_INDEX_MVVIEW_EXISTING_PATH);

	private static final String STAGING_STOCK_CVTERM_MVVIEW_PATH = "/sql/transformations/stock_cvterm/create_stock_cvterm_mvview.sql";
	private static final String STAGING_STOCK_CVTERM_MVVIEW_SQL = FileUtils
			.getSqlFileContents(STAGING_STOCK_CVTERM_MVVIEW_PATH);
	
	
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

		log.info("Injected Init Staging Schema SQL:" + STAGING_SCHEMA_INIT_SQL);
		log.info("Initializing Staging Schema...");

		generalDao.executeSQL(STAGING_SCHEMA_INIT_SQL);
		
		log.info("Injected Init Staging Tables SQL:" + STAGING_TABLES_INIT_SQL);
		log.info("Initializing Staging Tables...");

		generalDao.executeSQL(STAGING_TABLES_INIT_SQL);
		
		log.info("Injected Create All TAIR Stock Properties Materialized View SQL:" + STAGING_STOCKPROP_MVVIEW_ALL_SQL);
		log.info("Creating All TAIR Stock Properties Materialized View...");

		generalDao.executeSQL(STAGING_STOCKPROP_MVVIEW_ALL_SQL);
		
		log.info("Injected Create TAIR Stock Properties for Injested Stocks Materialized View SQL:" + STAGING_STOCKPROP_MVVIEW_EXISTING_SQL);
		log.info("Creating TAIR Stock Properties for Injested Stocks Materialized View...");

		generalDao.executeSQL(STAGING_STOCKPROP_MVVIEW_EXISTING_SQL);
		
		log.info("Injected Create Index for All Stocks Materialized View SQL:" + STAGING_STOCKPROP_INDEX_MVVIEW_ALL_SQL);
		log.info("Creating Index for All Stocks Materialized View...");

		generalDao.executeSQL(STAGING_STOCKPROP_INDEX_MVVIEW_ALL_SQL);
				
		log.info("Injected Create Index for Ingested Stocks Materialized View SQL:" + STAGING_STOCKPROP_INDEX_MVVIEW_EXISTINGL_SQL);
		log.info("Creating Index for Ingested Stocks Materialized View...");

		generalDao.executeSQL(STAGING_STOCKPROP_INDEX_MVVIEW_EXISTINGL_SQL);
		
		log.info("Injected Create Stock CVTerm Materialized Views SQL:" + STAGING_STOCK_CVTERM_MVVIEW_SQL);
		log.info("Creating Create Stock CVTerm Materialized Views...");

		generalDao.executeSQL(STAGING_STOCK_CVTERM_MVVIEW_SQL);

		return RepeatStatus.FINISHED;

	}

	@PostConstruct
	public void setDao() {

		this.generalDao = new GeneralDaoImpl();
		this.generalDao.setDataSource(targetDataSource);

	}


}
