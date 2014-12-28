package org.jcvi.araport.stock.reader;

import javax.sql.DataSource;

import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.jcvi.araport.stock.dao.impl.DbDaoImpl;
import org.jcvi.araport.stock.domain.DbXref;
import org.jcvi.araport.stock.domain.SourceStockDrivingQuery;
import org.jcvi.araport.stock.rowmapper.SourceStockDrivingQueryRowMapper;
import org.jcvi.araport.stock.rowmapper.beans.RowMapperBeans;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Component("driving_query_reader")
@Import({DataSourceInfrastructureConfiguration.class, DbDaoImpl.class, RowMapperBeans.class})

public class SourceStockDrivingQueryReader {

	@Autowired 
	DataSource targetDataSource;
	
	@Autowired 
	SourceStockDrivingQueryRowMapper rowMapper;
	
	@Bean
	public ItemReader<SourceStockDrivingQuery> sourceStockReader(){
		
		JdbcCursorItemReader<SourceStockDrivingQuery> reader = new JdbcCursorItemReader<SourceStockDrivingQuery>();
		String sql = "select stock_id from tair_stg.stock limit 100000";
		
		reader.setSql(sql);
		reader.setDataSource(targetDataSource);
		reader.setRowMapper(rowMapper);
		
		return reader;
	}
	
	
}
