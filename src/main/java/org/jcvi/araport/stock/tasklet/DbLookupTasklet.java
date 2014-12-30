package org.jcvi.araport.stock.tasklet;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.jcvi.stock.common.ApplicationConstants;
import org.araport.jcvi.stock.common.DbLookupHolder;
import org.araport.jcvi.stock.common.MetadataExecutionContext;
import org.araport.stock.domain.Db;
import org.jcvi.araport.stock.dao.DbDao;
import org.jcvi.araport.stock.dao.impl.DbDaoImpl;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Component
@StepScope
@Import({DataSourceInfrastructureConfiguration.class, DbDaoImpl.class})
public class DbLookupTasklet implements Tasklet {

	private static final Log log = LogFactory.getLog(DbLookupTasklet.class);
	
	@Autowired 
	DataSource targetDataSource;
			
	@Autowired
	private DbDao dbDao;
	
	@Override
	public RepeatStatus execute(StepContribution step, ChunkContext context)
			throws Exception {

        ExecutionContext jobContext = context.getStepContext()
                                             .getStepExecution()
                                             .getJobExecution()
                                             .getExecutionContext();
        
        dbDao.setTairDbId();
        dbDao.setTairStockDb();
        
       	saveDbLookups(ApplicationConstants.TAIR_DB_ID, dbDao.getTairDBId(), jobContext);
		saveDbLookups(ApplicationConstants.TAIR_STOCK_DB_ID, dbDao.getTairStockDbId(), jobContext);
		
		log.info(ApplicationConstants.TAIR_DB_ID + ": " + dbDao.getTairStockDbId());
		log.info(ApplicationConstants.TAIR_STOCK_DB_ID + ": " + dbDao.getTairStockDbId());
		return RepeatStatus.FINISHED;
	}

	private void saveDbLookups(String key, int value, ExecutionContext jobContext){
			jobContext.put(key, value);
		
	}
}
