package org.jcvi.araport.stock.tasklet;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.jcvi.stock.utils.FileUtils;
import org.jcvi.araport.stock.dao.GeneralDao;
import org.jcvi.araport.stock.dao.impl.GeneralDaoImpl;
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
public class StagingStockPropertiesTruncateTasklet implements Tasklet {

	private static final Log log = LogFactory
			.getLog(BulkLoadStockPropertiesTasklet.class);
	
	private static final String TRUNCATE_STAGING_STOCKPROP_SQL_PATH = "/sql/transformations/stock_properties/truncate_staging.stockprop.sql";
	private static final String TRUNCATE_STAGING_STOCKPROP_SQL = FileUtils
			.getSqlFileContents(TRUNCATE_STAGING_STOCKPROP_SQL_PATH);
	
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
		
		// 1. Truncating Staging StockProp
	       
			log.info("Injected Truncate Staging Stock Properties SQL:"
					+ TRUNCATE_STAGING_STOCKPROP_SQL);
			log.info("Truncate Staging Stock Properties Table...");

			generalDao.executeSQL(TRUNCATE_STAGING_STOCKPROP_SQL);
			
			return RepeatStatus.FINISHED;
			
	}

	@PostConstruct
	public void setDao() {

		this.generalDao = new GeneralDaoImpl();
		this.generalDao.setDataSource(targetDataSource);

	}
	
}
