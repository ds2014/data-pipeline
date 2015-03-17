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

public class StageDbXrefExistingStockAccessionsTasklet implements Tasklet {

	private static final Log log = LogFactory
			.getLog(StageDbXrefExistingStockAccessionsTasklet.class);

	private static final String CREATE_STAGING_BK_DBXREF_SQL_PATH = "/sql/transformations/dbxref/create_chado_dbxref_bk.sql";
	private static final String CREATE_STAGING_BK_DBXREF_SQL = FileUtils
			.getSqlFileContents(CREATE_STAGING_BK_DBXREF_SQL_PATH);

	private static final String DISABLE_DBXREF_CONSTRAINTS_SQL_PATH = "/sql/transformations/dbxref/disable_chado_dbxref_constraints.sql";
	private static final String DISABLE_DBXREF_CONSTRAINTS_SQL = FileUtils
			.getSqlFileContents(DISABLE_DBXREF_CONSTRAINTS_SQL_PATH);

	private static final String DELETE_EXISTING_ACCESSIONS_DBXREF_SQL_PATH = "/sql/transformations/dbxref/primary_stock_accession/delete_existing_accessions_chado_dbxref.sql";
	private static final String DELETE_EXISTING_ACCESSIONS_DBXREF_SQL = FileUtils
			.getSqlFileContents(DELETE_EXISTING_ACCESSIONS_DBXREF_SQL_PATH);

	private static final String CREATE_NOT_EXISTING_STOCK_ACCESSIONS_DBXREF_MVVIEW_SQL_PATH = "/sql/transformations/dbxref/primary_stock_accession/create_not_existing_primary_accessions_chado_dbxref_mvview.sql";
	private static final String CREATE_NOT_EXISTING_STOCK_ACCESSIONS_DBXREF_SQL = FileUtils
			.getSqlFileContents(CREATE_NOT_EXISTING_STOCK_ACCESSIONS_DBXREF_MVVIEW_SQL_PATH);

	private static final String CREATE_NOT_EXISTING_GERMPLASM_ACCESSIONS_DBXREF_MVVIEW_SQL_PATH =
						"/sql/transformations/dbxref/primary_stock_accession/create_not_existing_primary_germplasm_accessions_chado_dbxref_mvview.sql";
	
	private static final String CREATE_NOT_EXISTING_GERMPLASM_ACCESSIONS_DBXREF_SQL = FileUtils
			.getSqlFileContents(CREATE_NOT_EXISTING_GERMPLASM_ACCESSIONS_DBXREF_MVVIEW_SQL_PATH);

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

		log.info("Injected Backup DbXref Table SQL:"
				+ CREATE_STAGING_BK_DBXREF_SQL);
		log.info("Creating Backup of DbXref Table ...");

		generalDao.executeSQL(CREATE_STAGING_BK_DBXREF_SQL);

		log.info("Injected: Disable Constraints on DbXref Table SQL:"
				+ DISABLE_DBXREF_CONSTRAINTS_SQL);
		log.info("Disable Constraints on DbXref Table ...");

		generalDao.executeSQL(DISABLE_DBXREF_CONSTRAINTS_SQL);

		// Should not clear DbXref since they might be used by Loaded Stocks
		// log.info("Injected: Delete Existing Stock Accessions from DbXref Table SQL:"
		// + DELETE_EXISTING_ACCESSIONS_DBXREF_SQL);
		// log.info("Deleting Existing Stock Accessions from DbXref Table ...");

		// generalDao.executeSQL(DELETE_EXISTING_ACCESSIONS_DBXREF_SQL);

		//log.info("Injected: Create Non Existing Stock Accessions Materialized View SQL:"
			//	+ CREATE_NOT_EXISTING_STOCK_ACCESSIONS_DBXREF_SQL);
		//log.info("Creating Non Existing Stock Accessions Materialized View ...");

		log.info("Injected: Create Non Existing Germplasm Accessions Materialized View SQL:"
			+ CREATE_NOT_EXISTING_GERMPLASM_ACCESSIONS_DBXREF_SQL);
			
		log.info("Creating Non Existing Germplasm Accessions Materialized View ...");
		
		generalDao.executeSQL(CREATE_NOT_EXISTING_GERMPLASM_ACCESSIONS_DBXREF_SQL);

		return RepeatStatus.FINISHED;

	}

	@PostConstruct
	public void setDao() {

		this.generalDao = new GeneralDaoImpl();
		this.generalDao.setDataSource(targetDataSource);

	}

}
