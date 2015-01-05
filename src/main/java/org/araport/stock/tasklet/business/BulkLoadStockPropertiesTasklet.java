package org.araport.stock.tasklet.business;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

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
public class BulkLoadStockPropertiesTasklet implements Tasklet {

	private static final Log log = LogFactory
			.getLog(BulkLoadStockPropertiesTasklet.class);

	private static final String ALTER_CHADO_STOCKPROP_SQL_PATH = "/sql/transformations/stock_properties/alter_chado_stockprop.sql";
	private static final String ALTER_CHADO_STOCKPROP_SQL = FileUtils
			.getSqlFileContents(ALTER_CHADO_STOCKPROP_SQL_PATH);

	private static final String CREATE_STAGING_BK_STOCKPROP_SQL_PATH = "/sql/transformations/stock_properties/create_staging_stockprop_bk.sql";
	private static final String CREATE_STAGING_BK_STOCKPROP_SQL = FileUtils
			.getSqlFileContents(CREATE_STAGING_BK_STOCKPROP_SQL_PATH);

	private static final String ENABLE_STAGING_BK_CONSTRAINTS_SQL_PATH = "/sql/transformations/stock_properties/enable_chado_stockprop_bk_constraints.sql";
	private static final String ENABLE_STAGING_BK_CONSTRAINTS_SQL = FileUtils
			.getSqlFileContents(ENABLE_STAGING_BK_CONSTRAINTS_SQL_PATH);

	private static final String TRUNCATE_CHADO_STOCKPROP_SQL_PATH = "/sql/transformations/stock_properties/truncate_chado_stockprop.sql";
	private static final String TRUNCATE_CHADO_STOCKPROP_SQL = FileUtils
			.getSqlFileContents(TRUNCATE_CHADO_STOCKPROP_SQL_PATH);

	private static final String DISABLE_CONSTRAINTS_CHADO_STOCKPROP_SQL_PATH = "/sql/transformations/stock_properties/disable_chado_stockprop_constraints.sql";
	private static final String DISABLE_CONSTRAINTS_CHADO_STOCKPROP_SQL = FileUtils
			.getSqlFileContents(DISABLE_CONSTRAINTS_CHADO_STOCKPROP_SQL_PATH);

	private static final String POPULATE_CHADO_STOCKPROP_SQL_PATH = "/sql/transformations/stock_properties/populate_chado_stockprop.sql";
	private static final String POPULATE_CHADO_STOCKPROP_SQL = FileUtils
			.getSqlFileContents(POPULATE_CHADO_STOCKPROP_SQL_PATH);

	private static final String ENABLE_CONSTRAINTS_CHADO_STOCKPROP_SQL_PATH = "/sql/transformations/stock_properties/enable_chado_stockprop_constraints.sql";
	private static final String ENABLE_CONSTRAINTS_CHADO_STOCKPROP_SQL = FileUtils
			.getSqlFileContents(ENABLE_CONSTRAINTS_CHADO_STOCKPROP_SQL_PATH);

	private static final String CREATE_INDEXES_CHADO_STOCKPROP_SQL_PATH = "/sql/transformations/stock_properties/create_chado_stockprop_indexes.sql";
	private static final String CREATE_INDEXES_CHADO_STOCKPROP_SQL = FileUtils
			.getSqlFileContents(CREATE_INDEXES_CHADO_STOCKPROP_SQL_PATH);

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

		// 1. Copy loaded Stock Properties to Staging Backup Properties Table
		log.info("Injected Create Stock Properties Backup SQL:"
				+ CREATE_STAGING_BK_STOCKPROP_SQL);
		log.info("Creating Stock Properties Backup...");

		generalDao.executeSQL(CREATE_STAGING_BK_STOCKPROP_SQL);

		// 2. Enable Constraints on Staging Backup Properties Table
		log.info("Injected Enable Constraints on Staging Backup Properties Table SQL:"
				+ ENABLE_STAGING_BK_CONSTRAINTS_SQL);

		log.info("Enabling Constraints on Staging Backup Properties Table...");

		generalDao.executeSQL(ENABLE_STAGING_BK_CONSTRAINTS_SQL);

		// 3. Truncate Chado Stock Properties
		log.info("Injected Truncate Chado Stock Properties SQL:"
				+ TRUNCATE_CHADO_STOCKPROP_SQL);
		log.info("Truncating Chado Stock Properties Table...");

		generalDao.executeSQL(TRUNCATE_CHADO_STOCKPROP_SQL);

		// 4. Disable Constraints on Chado Stock Properties
		log.info("Injected Disable Constraints on Chado Stock Properties SQL:"
				+ DISABLE_CONSTRAINTS_CHADO_STOCKPROP_SQL);
		log.info("Disabling Constraints on the Chado Stock Properties Table...");

		generalDao.executeSQL(DISABLE_CONSTRAINTS_CHADO_STOCKPROP_SQL);

		// 5. Populate Chado Stock Properties Table
		log.info("Populate Chado Stock Properties Table SQL:"
				+ POPULATE_CHADO_STOCKPROP_SQL);
		log.info("Populating Chado Stock Properties Table...");

		generalDao.executeSQL(POPULATE_CHADO_STOCKPROP_SQL);

		// 8. Enable Constraints on Chado Stock Properties Table
		log.info("Injected Enable Constraints on Chado Stock Properties Table SQL:"
				+ ENABLE_CONSTRAINTS_CHADO_STOCKPROP_SQL);
		log.info("Enabling Constraints on Chado Stock Properties Table...");

		generalDao.executeSQL(ENABLE_CONSTRAINTS_CHADO_STOCKPROP_SQL);

		// 9. Create Indexes on Chado Stock Properties Table
		log.info("Injected Create Indexes on Chado Stock Properties Table SQL:"
				+ CREATE_INDEXES_CHADO_STOCKPROP_SQL);
		log.info("Creating Indexes on Chado Stock Properties Table...");

		generalDao.executeSQL(CREATE_INDEXES_CHADO_STOCKPROP_SQL);

		return RepeatStatus.FINISHED;

	}

	@PostConstruct
	public void setDao() {

		this.generalDao = new GeneralDaoImpl();
		this.generalDao.setDataSource(targetDataSource);

	}

}
