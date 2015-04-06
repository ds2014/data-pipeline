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
public class BulkLoadStockRelationshipTasklet implements Tasklet {

	private static final Log log = LogFactory
			.getLog(BulkLoadStockRelationshipTasklet.class);

	private static final String ALTER_CHADO_STOCK_CVTERM_SQL_PATH = "/sql/transformations/stock_properties/alter_chado_stock_cvterm.sql";
	private static final String ALTER_CHADO_STOCK_CVTERM_SQL = FileUtils
			.getSqlFileContents(ALTER_CHADO_STOCK_CVTERM_SQL_PATH);


	private static final String TRUNCATE_CHADO_STOCK_CVTERM_SQL_PATH = "/sql/transformations/stock_properties/truncate_chado_stock_cvterm.sql";
	private static final String TRUNCATE_CHADO_STOCK_CVTERM_SQL = FileUtils
			.getSqlFileContents(TRUNCATE_CHADO_STOCK_CVTERM_SQL_PATH);

	private static final String POPULATE_CHADO_STOCK_RELATIONSHIP_SQL_PATH = "/sql/stock_relationship/populate_chado_stockrelationship.sql";
	private static final String POPULATE_CHADO_STOCK_RELATIONSHIP_SQL = FileUtils
			.getSqlFileContents(POPULATE_CHADO_STOCK_RELATIONSHIP_SQL_PATH);

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

		// 1. Alter stock_cvterm table, enable big_int for stock_id
		//log.info("Alter Stock CVTerm Table SQL:"
			//	+ ALTER_CHADO_STOCK_CVTERM_SQL);
		//log.info("Alter Stock CVTerm Table...");

		//generalDao.executeSQL(ALTER_CHADO_STOCK_CVTERM_SQL);

		// 2. Truncate Chado Stock CVTerm
		//log.info("Injected Truncate Chado Stock CVTerm SQL:"
			//	+ TRUNCATE_CHADO_STOCK_CVTERM_SQL);
	//	log.info("Truncating Chado Stock CVTerm Table...");

		//generalDao.executeSQL(TRUNCATE_CHADO_STOCK_CVTERM_SQL);
		
		// 3. Populate Chado Stock CVTerm Table
		log.info("Populate Chado Stock Relationship Table SQL:"
				+ POPULATE_CHADO_STOCK_RELATIONSHIP_SQL);
		log.info("Populating Chado Stock Relationship Table...");

		generalDao.executeSQL(POPULATE_CHADO_STOCK_RELATIONSHIP_SQL);
		
		return RepeatStatus.FINISHED;

	}

	@PostConstruct
	public void setDao() {

		this.generalDao = new GeneralDaoImpl();
		this.generalDao.setDataSource(targetDataSource);

	}

}
