package org.araport.stock.reader;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.stock.common.ApplicationConstants;
import org.araport.stock.common.DbLookupHolder;
import org.araport.stock.common.MetadataExecutionContext;
import org.araport.stock.dao.impl.DbDaoImpl;
import org.araport.stock.domain.DbXref;
import org.araport.stock.domain.SourceStockDrivingQuery;
import org.araport.stock.listeners.LogStepStartStopListener;
import org.araport.stock.rowmapper.SourceStockDrivingQueryRowMapper;
import org.araport.stock.rowmapper.beans.RowMapperBeans;
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
		reader.setVerifyCursorPosition(false);
		//for multithreading case state of the reader must be false
		reader.setSaveState(false);
		String sql = "select stock_id from tair_stg.stock limit 10";
		
		reader.setSql(sql);
		reader.setDataSource(targetDataSource);
		reader.setRowMapper(rowMapper);
				
		return reader;
	}
	
	
}
