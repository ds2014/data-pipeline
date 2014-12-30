package org.jcvi.araport.stock.dao;

import java.util.List;

import javax.sql.DataSource;

import org.araport.stock.domain.CVTerm;
import org.araport.stock.domain.Db;
import org.araport.stock.domain.DbXref;

public interface CVTermDao {

	public boolean create (CVTerm cv);
	
	public CVTerm findByCVTermNameCVNameAndDbName(String cvTermName, String cvName, String dbName);
	
	public List<CVTerm> findByCvIdAndDbId(int cvId, int dbId);
	
	public List<CVTerm> findByCvIdAndDbName(int cvId, String dbName);
	
	public List<CVTerm> findByCvNameAndDbId(String cvName, int dbId);
	
	public List<CVTerm> findByCvNameAndDbName(String cvName, String dbName);
	
	public boolean save(CVTerm cvTerm);
	
	public void merge(CVTerm cvTerm);
	
	public CVTerm mergeAndReturn(CVTerm cvTerm);
	
	public void setDataSource(DataSource datasource);
	
	
}
