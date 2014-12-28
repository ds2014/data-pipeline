package org.jcvi.araport.stock.dao.impl;

import javax.sql.DataSource;

import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.jcvi.araport.stock.dao.StockDao;
import org.jcvi.araport.stock.domain.Stock;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Component("stock_reader")
@Import({ DataSourceInfrastructureConfiguration.class })
public class StockDaoImpl implements StockDao{

	@Override
	public void create(Stock stock) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Stock stock) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void merge(Stock stock) {
		
		
	}

	@Override
	public void setDataSource(DataSource datasource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Stock findStockById(int stockId) {
		// TODO Auto-generated method stub
		return null;
	}

}
