package org.jcvi.araport.stock.dao;

import org.jcvi.araport.stock.reader.domain.Db;

public interface DbDao {

	public boolean create (Db db);
	public Db findDbByName(String name);
	public boolean save(Db db);
	
	
}
