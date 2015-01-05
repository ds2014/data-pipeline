package org.araport.stock.reader;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.stock.common.ApplicationConstants;
import org.araport.stock.dao.GeneralDao;
import org.araport.stock.dao.impl.DbDaoImpl;
import org.araport.stock.dao.impl.GeneralDaoImpl;
import org.araport.stock.domain.Stock;
import org.araport.stock.domain.StockPropertySource;
import org.araport.stock.listeners.LogStepStartStopListener;
import org.araport.stock.rowmapper.StockPropertiesSourceRowMapper;
import org.araport.stock.rowmapper.StockSourceRowMapper;
import org.araport.stock.rowmapper.beans.RowMapperBeans;
import org.araport.stock.tasklet.business.BulkLoadStockPropertiesTasklet;
import org.araport.stock.utils.FileUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.PostgresPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component("source_stock_jdbc_reader")
@Import({ DataSourceInfrastructureConfiguration.class, DbDaoImpl.class,
		RowMapperBeans.class })
@PropertySources(value = { @PropertySource("classpath:/partition.properties") })
public class StockSourceJdbcPagingItemReader {

	private static final Logger log = Logger
			.getLogger(StockSourceJdbcPagingItemReader.class);

	@Autowired
	Environment environment;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	DataSource targetDataSource;

	@Autowired
	StockSourceRowMapper stockSourceRowMapper;
	
	 @Value("${stock.select.clause}")
	 private String selectClause;
	 
	 @Value("${stock.where.clause}")
	 private String whereClause;
	 
	 @Value("${stock.from.clause}")
	 private String fromClause;
	 
	 @Value("${stock.sortkey}")
	 private String sortKey;
	 
	 @Value("${stock.page.size}")
	 private int pageSize;
 
	
	@Bean
	@StepScope
	public JdbcPagingItemReader<Stock> sourceStockBatchReader(
			@Value("#{stepExecutionContext['minValue']}") long minValue,
			@Value("#{stepExecutionContext['maxValue']}") long maxValue,
			@Value("#{stepExecutionContext['partitionName']}") String partitionName)
			throws Exception {

		PostgresPagingQueryProvider provider = new PostgresPagingQueryProvider();
		provider.setSelectClause(selectClause);
		provider.setFromClause(fromClause);

		Map<String, Order> sortKeys = new HashMap<String, Order>();
		sortKeys.put(sortKey, Order.ASCENDING);
		provider.setSortKeys(sortKeys);
		
	    provider.setWhereClause(whereClause);

		JdbcPagingItemReader<Stock> reader = new JdbcPagingItemReader<Stock>();
		reader.setDataSource(targetDataSource);
		reader.setQueryProvider(provider);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("minValue", minValue);
		params.put("maxValue", maxValue);
		reader.setParameterValues(params);
				
		reader.setPageSize(pageSize);
		reader.setRowMapper(stockSourceRowMapper);
		reader.afterPropertiesSet();

		log.info("Partition: " + partitionName + "; Id Range: " + "MinValue: "
				+ minValue + " MaxValue: " + maxValue);

		return reader;
	}

}
