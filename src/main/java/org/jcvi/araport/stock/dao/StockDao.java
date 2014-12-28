package org.jcvi.araport.stock.dao;

import javax.sql.DataSource;

import org.jcvi.araport.stock.domain.Stock;

public interface StockDao {

	public void create(Stock stock);
	
	public void update(Stock stock);
	
	public Stock findStockById(int stockId);
	
	public void merge(Stock stock);
	
	public void setDataSource(DataSource datasource);
	
}
