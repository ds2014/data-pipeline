package org.jcvi.araport.stock.reader;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.jcvi.stock.common.ApplicationConstants;
import org.araport.jcvi.stock.utils.FileUtils;
import org.araport.stock.domain.StockPropertySource;
import org.jcvi.araport.stock.dao.GeneralDao;
import org.jcvi.araport.stock.dao.impl.DbDaoImpl;
import org.jcvi.araport.stock.dao.impl.GeneralDaoImpl;
import org.jcvi.araport.stock.listeners.LogStepStartStopListener;
import org.jcvi.araport.stock.rowmapper.StockPropertiesSourceRowMapper;
import org.jcvi.araport.stock.rowmapper.beans.RowMapperBeans;
import org.jcvi.araport.stock.tasklet.BulkLoadStockPropertiesTasklet;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.PostgresPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("source_stockproperty_reader")
@Import({ DataSourceInfrastructureConfiguration.class, DbDaoImpl.class, 
		RowMapperBeans.class })
public class StockPropertiesItemReader implements StepExecutionListener {

	private static final Logger log = Logger
			.getLogger(StockPropertiesItemReader.class);

	@Autowired
	DataSource targetDataSource;

	@Autowired
	StockPropertiesSourceRowMapper stockPropertiesSourceMapper;
	
	
	@Bean
	// @Scope("prototype")
	@StepScope
	public JdbcPagingItemReader<StockPropertySource> sourceStockPropertyReader()
			throws Exception {

		PostgresPagingQueryProvider provider = new PostgresPagingQueryProvider();
		provider.setSelectClause("select stock_id, key, value, type_id");
		provider.setFromClause("from staging.stock_properties");

		Map<String, Order> sortKeys = new HashMap<String, Order>();
		sortKeys.put("stock_id", Order.ASCENDING);
		provider.setSortKeys(sortKeys);

		//provider.setWhereClause("stock_id in (1,2)");

		JdbcPagingItemReader<StockPropertySource> reader = new JdbcPagingItemReader<StockPropertySource>();
		reader.setDataSource(targetDataSource);
		reader.setQueryProvider(provider);
		reader.setPageSize(1000);
		reader.setRowMapper(stockPropertiesSourceMapper);
		reader.setSaveState(false);
		reader.afterPropertiesSet();

		return reader;
	}

	@Override
	public ExitStatus afterStep(StepExecution execution) {
		log.info("Paging Item Reader Step: " + execution.getStepName() + " has ended!");

        ExecutionContext jobContext = execution.getJobExecution().getExecutionContext();
        
        if (jobContext.containsKey(ApplicationConstants.TAIR_DB_ID)){
        	 log.info(ApplicationConstants.TAIR_DB_ID + ": " + jobContext.get(ApplicationConstants.TAIR_DB_ID) );
        }
        if (jobContext.containsKey(ApplicationConstants.TAIR_STOCK_DB_ID)){
        	 log.info(ApplicationConstants.TAIR_STOCK_DB_ID + ": " + jobContext.get(ApplicationConstants.TAIR_STOCK_DB_ID) );
        }
        
       return execution.getExitStatus();
	}

	@Override
	public void beforeStep(StepExecution execution) {

		log.info("Paging Item Reader Step: " +execution.getStepName() + " has begun!");

		ExecutionContext jobContext = execution.getJobExecution()
				.getExecutionContext();

		if (jobContext.containsKey(ApplicationConstants.TAIR_DB_ID)) {
			log.info(ApplicationConstants.TAIR_DB_ID + ": "
					+ jobContext.get(ApplicationConstants.TAIR_DB_ID));

		}
		if (jobContext.containsKey(ApplicationConstants.TAIR_STOCK_DB_ID)) {
			log.info(ApplicationConstants.TAIR_STOCK_DB_ID + ": "
					+ jobContext.get(ApplicationConstants.TAIR_STOCK_DB_ID));

		}

				
	}
	
	
}
