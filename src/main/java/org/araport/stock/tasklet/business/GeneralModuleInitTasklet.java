package org.araport.stock.tasklet.business;

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
public class GeneralModuleInitTasklet implements Tasklet{

	private static final Log log = LogFactory
			.getLog(GeneralModuleInitTasklet.class);

	private static final String DB_INIT_SQL_PATH = "/sql/bootstrap/db_init.sql";
	private static final String DB_INIT_SQL = FileUtils
			.getSqlFileContents(DB_INIT_SQL_PATH);
	
	private static final String DBXREF_INIT_STOCK_TYPE_ROOT_SQL_PATH = "/sql/bootstrap/dbxref_stock_type.sql";
	private static final String DBXREF_INIT_STOCK_TYPE_ROOT_SQL = FileUtils
			.getSqlFileContents(DBXREF_INIT_STOCK_TYPE_ROOT_SQL_PATH);
	
	private static final String DBXREF_INIT_TAIR_STOCK_TYPES_SQL_PATH = "/sql/bootstrap/dbxref_tair_stock_types.sql";
	private static final String DBXREF_INIT_TAIR_STOCK_TYPES_SQL = FileUtils
			.getSqlFileContents(DBXREF_INIT_TAIR_STOCK_TYPES_SQL_PATH);
	
	private static final String CV_INIT_STOCK_TYPE_SQL_PATH = "/sql/bootstrap/cv_stock_lookup.sql";
	private static final String CV_INIT_STOCK_TYPE_SQL = FileUtils
			.getSqlFileContents(CV_INIT_STOCK_TYPE_SQL_PATH);
	
	private static final String CVTERM_INIT_TAIR_STOCK_TYPES_SQL_PATH = "/sql/bootstrap/cvterm_stock_type.sql";
	private static final String CVTERM_INIT_TAIR_STOCK_TYPES_SQL = FileUtils
			.getSqlFileContents(CVTERM_INIT_TAIR_STOCK_TYPES_SQL_PATH);
	
	
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

		log.info("Injected Init Stock Database References SQL:" + DB_INIT_SQL);
		log.info("Stock Database References...");

		generalDao.executeSQL(DB_INIT_SQL);
		
		log.info("Injected DBXref Root of Stock Types SQL:" + DBXREF_INIT_STOCK_TYPE_ROOT_SQL);
		log.info("Loading DBXref Root of Stock Types ...");

		generalDao.executeSQL(DBXREF_INIT_STOCK_TYPE_ROOT_SQL);
		
		log.info("Injected DbXref TAIR Stock Types SQL:" + DBXREF_INIT_TAIR_STOCK_TYPES_SQL);
		log.info("Loading DbXref TAIR Stock Types ...");

		generalDao.executeSQL(DBXREF_INIT_TAIR_STOCK_TYPES_SQL);
		
		log.info("Injected CV Stock Type SQL:" + CV_INIT_STOCK_TYPE_SQL);
		log.info("Loading CV Stock Type ...");

		generalDao.executeSQL(CV_INIT_STOCK_TYPE_SQL);
		
		log.info("Injected CV Term TAIR Stock Types SQL:" + CVTERM_INIT_TAIR_STOCK_TYPES_SQL);
		log.info("Loading CV Term TAIR Stock Types ...");

		//generalDao.executeSQL(CVTERM_INIT_TAIR_STOCK_TYPES_SQL);

		return RepeatStatus.FINISHED;

	}

	@PostConstruct
	public void setDao() {

		this.generalDao = new GeneralDaoImpl();
		this.generalDao.setDataSource(targetDataSource);

	}


}
