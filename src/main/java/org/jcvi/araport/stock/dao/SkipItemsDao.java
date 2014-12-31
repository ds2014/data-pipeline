package org.jcvi.araport.stock.dao;

import javax.sql.DataSource;

import org.araport.jcvi.stock.policy.SkipItems;

public interface SkipItemsDao {
	public void save(SkipItems skipElement);
	public void setDataSource(DataSource datasource);
}
