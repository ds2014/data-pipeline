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

@Component("source_stockproperty_reader")
@Import({ DataSourceInfrastructureConfiguration.class, DbDaoImpl.class,
		RowMapperBeans.class })
@PropertySources(value = { @PropertySource("classpath:/partition.properties") })
public class StockPropertiesItemReader {

	private static final Logger log = Logger
			.getLogger(StockPropertiesItemReader.class);

	@Autowired
	Environment environment;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	DataSource targetDataSource;

	@Autowired
	StockPropertiesSourceRowMapper stockPropertiesSourceMapper;
	
	 @Value("${stockprop.select.clause}")
	 private String selectClause;
	 
	 @Value("${stockprop.where.clause}")
	 private String whereClause;
	 
	 @Value("${stockprop.from.clause}")
	 private String fromClause;
	 
	 @Value("${stockprop.sortkey}")
	 private String sortKey;
	 
	 @Value("${stockprop.page.size}")
	 private int pageSize;
		
	@Bean
	@StepScope
	public JdbcPagingItemReader<StockPropertySource> sourceStockPropertyReader(
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

		JdbcPagingItemReader<StockPropertySource> reader = new JdbcPagingItemReader<StockPropertySource>();
		reader.setDataSource(targetDataSource);
		reader.setQueryProvider(provider);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("minValue", minValue);
		params.put("maxValue", maxValue);
		reader.setParameterValues(params);
				
		reader.setPageSize(pageSize);
		reader.setRowMapper(stockPropertiesSourceMapper);
		reader.afterPropertiesSet();

		log.info("Partition: " + partitionName + "; Id Range: " + "MinValue: "
				+ minValue + " MaxValue: " + maxValue);

		return reader;
	}

}
