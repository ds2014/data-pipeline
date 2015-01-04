package org.araport.stock.reader;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.stock.dao.impl.DbDaoImpl;
import org.araport.stock.domain.DbXref;
import org.araport.stock.domain.DbXrefSource;
import org.araport.stock.domain.StockPropertySource;
import org.araport.stock.rowmapper.DbXrefRowMapper;
import org.araport.stock.rowmapper.DbXrefSourceRowMapper;
import org.araport.stock.rowmapper.StockPropertiesSourceRowMapper;
import org.araport.stock.rowmapper.beans.RowMapperBeans;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.PostgresPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component("dbxref_jdbc_item_paging_reader")
@Import({ DataSourceInfrastructureConfiguration.class,
		RowMapperBeans.class })
@PropertySources(value = { @PropertySource("classpath:/partition.properties") })
public class DbXrefJdbcPagingItemReader {

	
	private static final Logger log = Logger
			.getLogger(DbXrefJdbcPagingItemReader.class);
	
	@Autowired
	Environment environment;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	DataSource targetDataSource;

	@Autowired
	DbXrefSourceRowMapper dbXrefSourceRowMapper;
	
	 @Value("${dbxref.select.clause}")
	 private String selectClause;
	 
	 @Value("${dbxref.where.clause}")
	 private String whereClause;
	 
	 @Value("${dbxref.from.clause}")
	 private String fromClause;
	 
	 @Value("${dbxref.sortkey}")
	 private String sortKey;
	 
	 @Value("${dbxref.page.size}")
	 private int pageSize;
		
	@Bean
	@StepScope
	public JdbcPagingItemReader<DbXrefSource> dbXrefJdbcItemReader(
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

		JdbcPagingItemReader<DbXrefSource> reader = new JdbcPagingItemReader<DbXrefSource>();
		reader.setDataSource(targetDataSource);
		reader.setQueryProvider(provider);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("minValue", minValue);
		params.put("maxValue", maxValue);
		reader.setParameterValues(params);
				
		reader.setPageSize(pageSize);
		reader.setRowMapper(dbXrefSourceRowMapper);
		reader.afterPropertiesSet();

		log.info("Partition: " + partitionName + "; Id Range: " + "MinValue: "
				+ minValue + " MaxValue: " + maxValue);

		return reader;
	}
}
