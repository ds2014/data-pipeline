package org.jcvi.araport.stock.reader.batch;

import org.jcvi.araport.stock.reader.domain.DbXref;
import org.springframework.batch.item.ItemProcessor;

public class DbXrefItemProcessor implements ItemProcessor<DbXref, DbXref>{

	@Override
	public DbXref process(final DbXref dbxref) throws Exception {
	
		return dbxref;
	}

}
