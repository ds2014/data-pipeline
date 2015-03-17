package org.araport.stock.tasklet.staging;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araport.stock.dao.GeneralDao;
import org.araport.stock.dao.impl.GeneralDaoImpl;
import org.araport.stock.utils.FileUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

public class StockStagingPreloadingTasklet implements Tasklet{

	private static final Log log = LogFactory
			.getLog(StockStagingPreloadingTasklet.class);
	
	private static final String ALTER_CHADO_STOCK_SQL_PATH = "/sql/transformations/stock/alter_chado_stock.sql";
	private static final String ALTER_CHADO_STOCK_SQL_SQL = FileUtils
			.getSqlFileContents(ALTER_CHADO_STOCK_SQL_PATH);
	
	private static final String CREATE_STAGING_BK_CHADO_SQL_PATH = "/sql/transformations/stock/create_chado_stock_bk.sql";
	private static final String CREATE_STAGING_BK_CHADO_SQL = FileUtils
			.getSqlFileContents(CREATE_STAGING_BK_CHADO_SQL_PATH);

	private static final String DISABLE_CHADO_STOCK_CONSTRAINTS_SQL_PATH = "/sql/transformations/stock/disable_chado_stock_constraints.sql";
	private static final String DISABLE_CHADO_STOCK_CONSTRAINTS_SQL = FileUtils
			.getSqlFileContents(DISABLE_CHADO_STOCK_CONSTRAINTS_SQL_PATH);

	private static final String CREATE_GERMPLASM_SOURCE_SQL_PATH = "/sql/transformations/stock/germplasm_source.sql";
	private static final String CREATE_GERMPLASM_SOURCE_SQL = FileUtils
			.getSqlFileContents(CREATE_GERMPLASM_SOURCE_SQL_PATH);
	
	private static final String CREATE_NOT_EXISTING_STOCK_MVVIEW_SQL_PATH = "/sql/transformations/stock/create_non_existing_tair_stocks_mvview.sql";
	private static final String CREATE_NOT_EXISTING_STOCK_SQL = FileUtils
			.getSqlFileContents(CREATE_NOT_EXISTING_STOCK_MVVIEW_SQL_PATH);

	
	//germplasm_source.sql
	
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

		log.info("Injected: Alter Chado Stock Table SQL:"
				+ ALTER_CHADO_STOCK_SQL_SQL);
		log.info("Altering Chado Stock Table ...");

		//generalDao.executeSQL(ALTER_CHADO_STOCK_SQL_SQL);
		
		log.info("Injected: Backup Chado Stock Table SQL:"
				+ CREATE_STAGING_BK_CHADO_SQL);
		log.info("Creating Backup of Chado Stock Table ...");

		generalDao.executeSQL(CREATE_STAGING_BK_CHADO_SQL);
		
		//germplasm source

		log.info("Injected: Germplasm Source SQL:"
				+ CREATE_GERMPLASM_SOURCE_SQL);
		log.info("Creating Germplasm Source SQL ...");

		generalDao.executeSQL(CREATE_GERMPLASM_SOURCE_SQL);
		
		log.info("Injected: Disable Constraints on Chado Stock Table SQL:"
				+ DISABLE_CHADO_STOCK_CONSTRAINTS_SQL);
		log.info("Disable Constraints on Chado Table ...");

		generalDao.executeSQL(DISABLE_CHADO_STOCK_CONSTRAINTS_SQL);
		
		log.info("Injected: Create Non Existing TAIR Stocks Materialized View SQL:"
				+ CREATE_NOT_EXISTING_STOCK_SQL);
		log.info("Creating Non Existing TAIR Stock Accessions Materialized View ...");

		generalDao.executeSQL(CREATE_NOT_EXISTING_STOCK_SQL);

		return RepeatStatus.FINISHED;

	}

	@PostConstruct
	public void setDao() {

		this.generalDao = new GeneralDaoImpl();
		this.generalDao.setDataSource(targetDataSource);

	}

	
}
