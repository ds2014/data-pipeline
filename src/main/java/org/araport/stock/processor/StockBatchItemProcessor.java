package org.araport.stock.processor;

import org.araport.stock.domain.SourceStockDrivingQuery;
import org.araport.stock.domain.Stock;
import org.araport.stock.domain.StockProperty;
import org.araport.stock.domain.StockPropertySource;
import org.springframework.batch.item.ItemProcessor;

public class StockBatchItemProcessor implements ItemProcessor <Stock, Stock> {

	@Override
	public Stock process(Stock sourceRecord) throws Exception {
		
		return createStock(sourceRecord);
	}

	private Stock createStock(final Stock sourceRecord){
		
		Stock item = new Stock();
		item.setStockId(sourceRecord.getStockId());
		item.setDbxrefId(sourceRecord.getDbxrefId());
		item.setStockTypeId(sourceRecord.getStockTypeId());
		item.setOrganismId(6);
		item.setName(sourceRecord.getName());
		item.setUniqueName(sourceRecord.getUniqueName());
		item.setDescription(sourceRecord.getDescription());
		item.setObsolete(sourceRecord.isObsolete());
		
		return item;
	}
	
}
