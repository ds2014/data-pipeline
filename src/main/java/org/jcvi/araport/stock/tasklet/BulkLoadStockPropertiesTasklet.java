package org.jcvi.araport.stock.tasklet;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.jcvi.stock.utils.FileUtils;
import org.jcvi.araport.stock.dao.GeneralDao;
import org.jcvi.araport.stock.dao.impl.CVDaoImpl;
import org.jcvi.araport.stock.dao.impl.CVTermDaoImpl;
import org.jcvi.araport.stock.dao.impl.DbDaoImpl;
import org.jcvi.araport.stock.dao.impl.DbXrefDaoImpl;
import org.jcvi.araport.stock.dao.impl.GeneralDaoImpl;
import org.jcvi.araport.stock.dao.impl.StockDaoImpl;
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
public class BulkLoadStockPropertiesTasklet implements Tasklet{

	private static final Log log = LogFactory
			.getLog(BulkLoadStockPropertiesTasklet.class);
	
	private static final String INSERT_NAMED_PARAM_TRUNCATE_SQL_PATH = "/sql/transformations/stock_properties/truncate_chado_stockprop.sql";
	private static final String INSERT_NAMED_PARAM_TRUNCATE_SQL = FileUtils.getSqlFileContents(INSERT_NAMED_PARAM_TRUNCATE_SQL_PATH);
	
	private static final String INSERT_NAMED_PARAM_POPULATE_SQL_PATH = "/sql/transformations/stock_properties/populate_stockprop.sql";
	private static final String INSERT_NAMED_PARAM_POPULATE_SQL = FileUtils.getSqlFileContents(INSERT_NAMED_PARAM_POPULATE_SQL_PATH);
	
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
		
		log.info("Injected Truncate SQL:" + INSERT_NAMED_PARAM_TRUNCATE_SQL);
		generalDao.executeSQL(INSERT_NAMED_PARAM_TRUNCATE_SQL);
		
		
		log.info("Injected Truncate SQL:" + INSERT_NAMED_PARAM_POPULATE_SQL);
		generalDao.executeSQL(INSERT_NAMED_PARAM_POPULATE_SQL);
		
		return RepeatStatus.FINISHED;
		
	}
	
	
	@PostConstruct
	public void setDao() {
		
		this.generalDao = new GeneralDaoImpl();
		this.generalDao.setDataSource(targetDataSource);
				

	}

}
