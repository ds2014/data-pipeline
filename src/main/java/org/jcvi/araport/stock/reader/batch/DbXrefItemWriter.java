package org.jcvi.araport.stock.reader.batch;

import java.util.List;

import org.jcvi.araport.stock.reader.domain.DbXref;
import org.springframework.batch.item.ItemWriter;


public class DbXrefItemWriter implements ItemWriter<DbXref>{

	@Override
	public void write(List<? extends DbXref> items) throws Exception {
		
	
		if(items.get(0) != null){
			DbXref dbXref = items.get(0);
			
			System.out.println("DbXref" + dbXref);
		}
		
	}

}
