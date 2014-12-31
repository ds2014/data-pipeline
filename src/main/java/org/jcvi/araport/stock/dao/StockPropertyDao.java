package org.jcvi.araport.stock.dao;

import javax.sql.DataSource;

import org.araport.stock.domain.StockProperty;

public interface StockPropertyDao {

	public boolean create (StockProperty property);
	public StockProperty findDbByNameAndStockId(String name);
	
	public boolean save(StockProperty property);
	
	public void merge(StockProperty property);
	
	public StockProperty mergeAndReturn(StockProperty property);
	
	public void setDataSource(DataSource datasource);
	
	
}
