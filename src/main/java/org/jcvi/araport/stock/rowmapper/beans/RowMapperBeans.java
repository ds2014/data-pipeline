package org.jcvi.araport.stock.rowmapper.beans;

import org.jcvi.araport.stock.rowmapper.SourceStockDrivingQueryRowMapper;
import org.springframework.context.annotation.Bean;

public class RowMapperBeans {

	@Bean
	public SourceStockDrivingQueryRowMapper sourceStockDrivingQueryMapper(){
		return new SourceStockDrivingQueryRowMapper(); 
	}
}
