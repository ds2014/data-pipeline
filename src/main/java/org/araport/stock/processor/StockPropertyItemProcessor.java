package org.araport.stock.processor;

import org.araport.stock.domain.SourceStockDrivingQuery;
import org.araport.stock.domain.Stock;
import org.araport.stock.domain.StockProperty;
import org.araport.stock.domain.StockPropertySource;
import org.springframework.batch.item.ItemProcessor;

public class StockPropertyItemProcessor implements ItemProcessor <StockPropertySource, StockProperty> {

	@Override
	public StockProperty process(StockPropertySource sourceRecord) throws Exception {
		
		return createStockProperty(sourceRecord);
	}

	private StockProperty createStockProperty(final StockPropertySource sourceRecord){
		
		StockProperty property = new StockProperty();
		property.setStockId(sourceRecord.getStockId());
		property.setValue(sourceRecord.getValue());
		property.setTypeId(sourceRecord.getTypeId());
		property.setRank(0);
		return property;
	}
	
}
