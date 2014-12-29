package org.araport.jcvi.stock.common;

import java.util.HashMap;
import java.util.Map;

public class DbLookupHolder {

	private Map<String, Integer> dbLookup = new HashMap<String, Integer>();
	
 
	public int getTairDBId() {
		return dbLookup.get(ApplicationContstants.TAIR_DB_ID);
	}
	
	public void setTairDBId(int dbId) {
			dbLookup.put(ApplicationContstants.TAIR_DB_ID, dbId);
	}

	public int getTairStockDBId() {
		return dbLookup.get(ApplicationContstants.TAIR_STOCK_DB_ID);
	}
	
	public void setTairStockDBId(int dbId) {
		dbLookup.put(ApplicationContstants.TAIR_STOCK_DB_ID, dbId);
}
		
}
