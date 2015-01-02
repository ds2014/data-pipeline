package org.jcvi.araport.stock.rowmapper.beans;

import org.araport.stock.domain.StockPropertySource;
import org.jcvi.araport.stock.processor.StockItemProcessor;
import org.jcvi.araport.stock.processor.StockPropertyItemProcessor;
import org.jcvi.araport.stock.reader.StockPropertiesItemReader;
import org.jcvi.araport.stock.rowmapper.SourceStockDrivingQueryRowMapper;
import org.jcvi.araport.stock.rowmapper.StockPropertiesSourceRowMapper;
import org.jcvi.araport.stock.rowmapper.StockPropertyRowMapper;
import org.jcvi.araport.stock.rowmapper.StockRowMapper;
import org.jcvi.araport.stock.writer.StockItemWriter;
import org.jcvi.araport.stock.writer.StockPropertyItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

public class RowMapperBeans {

	@Bean
	public SourceStockDrivingQueryRowMapper sourceStockDrivingQueryMapper(){
		return new SourceStockDrivingQueryRowMapper(); 
	}
	
	@Bean
	public StockRowMapper stockRowMapper(){
		return new StockRowMapper(); 
	}
	
	@Bean
	public StockItemProcessor stockItemProcessor(){
		return new StockItemProcessor(); 
	}
	
	@Bean
	public StockPropertyItemProcessor stockPropertyItemProcessor(){
		return new StockPropertyItemProcessor();
	}
	
	@Bean
	public StockItemWriter stockItemWriter(){
		return new StockItemWriter();
	}
	
	@Bean
	public StockPropertyItemWriter stockPropertyItemWriter(){
		return new StockPropertyItemWriter();
	}
	
	
	@Bean
	public StockPropertiesSourceRowMapper stockPropertiesSourceMapper(){
		return new StockPropertiesSourceRowMapper();
	}
	
	@Bean
	public StockPropertyRowMapper stockPropertyRowMapper(){
		return new StockPropertyRowMapper();
	}
		
}
