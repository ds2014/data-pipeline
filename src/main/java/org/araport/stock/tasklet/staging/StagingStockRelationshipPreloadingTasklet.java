package org.araport.stock.tasklet.staging;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.stock.dao.GeneralDao;
import org.araport.stock.dao.impl.GeneralDaoImpl;
import org.araport.stock.tasklet.business.BulkLoadStockPropertiesTasklet;
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
public class StagingStockRelationshipPreloadingTasklet implements Tasklet {

	private static final Log log = LogFactory
			.getLog(StagingStockRelationshipPreloadingTasklet.class);
	
	private static final String TRUNCATE_STOCK_RELATIONSHIP_SQL_PATH = "/sql/stock_relationship/truncate_chado_stock_relationship.sql";
	private static final String TRUNCATE_STOCK_RELATIONSHIP_SQL = FileUtils
			.getSqlFileContents(TRUNCATE_STOCK_RELATIONSHIP_SQL_PATH );
	
	private static final String INIT_STOCK_RELATIONSHIP_MV_VIEW_SQL_PATH = "/sql/stock_relationship/create_stock_relationship_mv.sql";
	private static final String INIT_STOCK_RELATIONSHIP_MV_VIEW_SQL = FileUtils
			.getSqlFileContents(INIT_STOCK_RELATIONSHIP_MV_VIEW_SQL_PATH);

	
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
		
		// 1. Truncating Stock Relationship
	       
			log.info("Injected Truncate Staging Stock CVTerm SQL:"
					+ TRUNCATE_STOCK_RELATIONSHIP_SQL);
			log.info("Truncate Staging Stock CVTerm Table...");

			generalDao.executeSQL(TRUNCATE_STOCK_RELATIONSHIP_SQL);
			
		// 2. Create Stock Relationship MV View
		       
				log.info("Injected Create Stock Relationship MV View SQL:"
						+ INIT_STOCK_RELATIONSHIP_MV_VIEW_SQL);
				log.info("Create Stock Relationship MV View...");

				generalDao.executeSQL(INIT_STOCK_RELATIONSHIP_MV_VIEW_SQL);
			
			return RepeatStatus.FINISHED;
			
	}

	@PostConstruct
	public void setDao() {

		this.generalDao = new GeneralDaoImpl();
		this.generalDao.setDataSource(targetDataSource);

	}
	
}
