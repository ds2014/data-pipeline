package org.jcvi.araport.stock.dao;

import javax.sql.DataSource;

public interface GeneralDao {

	public void executeSQL(String sql);
	
	public void setDataSource(DataSource datasource);
	
}
