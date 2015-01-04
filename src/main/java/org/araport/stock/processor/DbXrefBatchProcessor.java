package org.araport.stock.processor;

import org.araport.stock.domain.DbXref;
import org.araport.stock.domain.DbXrefSource;
import org.araport.stock.domain.StockProperty;
import org.araport.stock.domain.StockPropertySource;
import org.springframework.batch.item.ItemProcessor;

public class DbXrefBatchProcessor implements ItemProcessor <DbXrefSource, DbXref>{

	private static final String DESCRIPTION = "TAIR Primary Acession";
	private static final String VERSION = "";
	
	@Override
	public DbXref process(DbXrefSource sourceItem) throws Exception {
		
		DbXref item = new DbXref();
		item.setPrimaryAccession(String.valueOf(sourceItem.getPrimaryAccession()));
		item.setDbId(sourceItem.getDbId());
		item.setDescription(DESCRIPTION);
		item.setVersion(VERSION);
		
		return item;
	}

}
