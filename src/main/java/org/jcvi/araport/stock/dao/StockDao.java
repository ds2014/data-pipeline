package org.jcvi.araport.stock.dao;

import javax.sql.DataSource;

import org.araport.stock.domain.Stock;
import org.araport.stock.domain.StockDbXref;

public interface StockDao {

	public void create(Stock stock);
	
	public void update(Stock stock);
	
	public Stock findStockById(int stockId);
	
	public void merge(Stock stock);
	
	public Stock lookupSourceStockById(long stockId);
	
	public void merge(StockDbXref stockRef);
	
	public void setDataSource(DataSource datasource);
	
}
