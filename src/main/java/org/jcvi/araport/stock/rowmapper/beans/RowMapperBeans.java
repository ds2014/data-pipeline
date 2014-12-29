package org.jcvi.araport.stock.rowmapper.beans;

import org.jcvi.araport.stock.processor.StockItemProcessor;
import org.jcvi.araport.stock.rowmapper.SourceStockDrivingQueryRowMapper;
import org.jcvi.araport.stock.rowmapper.StockRowMapper;
import org.jcvi.araport.stock.writer.StockItemWriter;
import org.springframework.context.annotation.Bean;

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
	public StockItemWriter stockItemWriter(){
		return new StockItemWriter();
	}
	
	
}
