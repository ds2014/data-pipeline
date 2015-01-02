package org.jcvi.araport.stock.reader;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.stock.domain.StockPropertySource;
import org.jcvi.araport.stock.dao.impl.DbDaoImpl;
import org.jcvi.araport.stock.rowmapper.StockPropertiesSourceRowMapper;
import org.jcvi.araport.stock.rowmapper.beans.RowMapperBeans;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.PostgresPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("driving_query_reader")
@Import({DataSourceInfrastructureConfiguration.class, DbDaoImpl.class, RowMapperBeans.class})

public class StockPropertiesItemReader {

	
	@Autowired 
	DataSource targetDataSource;
	
	@Autowired
	StockPropertiesSourceRowMapper stockPropertiesSourceMapper;
	
	
	@Bean
	@Scope("step")
	public JdbcPagingItemReader<StockPropertySource> reader() throws Exception {

		PostgresPagingQueryProvider provider = new PostgresPagingQueryProvider();
	    provider.setSelectClause("select stock_id, key, value, type_id");
	    provider.setFromClause("from staging.stock_properties");
	    
	    Map <String, Order> sortKeys = new HashMap <String, Order> ();
	    sortKeys.put("stock_id", Order.ASCENDING);
	    provider.setSortKeys(sortKeys);

	    JdbcPagingItemReader<StockPropertySource> reader = new JdbcPagingItemReader<StockPropertySource>();
	    reader.setDataSource(targetDataSource);
	    reader.setQueryProvider(provider);
	    reader.setPageSize(50);
	    reader.setRowMapper(stockPropertiesSourceMapper);
	    reader.afterPropertiesSet();

	    return reader;
	}
}
