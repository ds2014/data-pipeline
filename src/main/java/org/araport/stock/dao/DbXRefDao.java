package org.araport.stock.dao;

import javax.sql.DataSource;

import org.araport.stock.domain.DbXref;

public interface DbXRefDao {
		
	public boolean create(DbXref dbXref);
	
	public boolean update(DbXref dbXref);
	
	public DbXref findDbXrefByAccessionAndDb(int dbId, String accession);
	
	public void merge(DbXref dbXref);
	
	public DbXref mergeAndReturn(DbXref dbXref);
	
	public void setDataSource(DataSource datasource);

}
