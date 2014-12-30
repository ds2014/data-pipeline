package org.jcvi.araport.stock.reader;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.jcvi.stock.common.ApplicationConstants;
import org.araport.jcvi.stock.common.DbLookupHolder;
import org.araport.jcvi.stock.common.MetadataExecutionContext;
import org.jcvi.araport.stock.dao.impl.DbDaoImpl;
import org.jcvi.araport.stock.domain.DbXref;
import org.jcvi.araport.stock.domain.SourceStockDrivingQuery;
import org.jcvi.araport.stock.listeners.LogStepStartStopListener;
import org.jcvi.araport.stock.rowmapper.SourceStockDrivingQueryRowMapper;
import org.jcvi.araport.stock.rowmapper.beans.RowMapperBeans;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	
	private static final Logger  log = Logger.getLogger(SourceStockDrivingQueryReader.class);
		
	@Bean
	public ItemReader<SourceStockDrivingQuery> sourceStockReader(){
		
		JdbcCursorItemReader<SourceStockDrivingQuery> reader = new JdbcCursorItemReader<SourceStockDrivingQuery>();
		String sql = "select stock_id from tair_stg.stock limit 10000";
		
		reader.setSql(sql);
		reader.setDataSource(targetDataSource);
		reader.setRowMapper(rowMapper);
				
		return reader;
	}
	
	
}
