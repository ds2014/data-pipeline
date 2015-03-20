package org.araport.stock.processor;

import org.araport.stock.domain.SourceStockDrivingQuery;
import org.araport.stock.domain.Stock;
import org.araport.stock.domain.StockCVTerm;
import org.araport.stock.domain.StockCVTermSource;
import org.araport.stock.domain.StockProperty;
import org.araport.stock.domain.StockPropertySource;
import org.springframework.batch.item.ItemProcessor;

public class StockCVTermItemProcessor implements ItemProcessor <StockCVTermSource, StockCVTerm> {

	@Override
	public StockCVTerm process(StockCVTermSource sourceRecord) throws Exception {
		
		return createStockCVterm(sourceRecord);
	}

	private StockCVTerm createStockCVterm(final StockCVTermSource sourceRecord){
		
		StockCVTerm item = new StockCVTerm();
		item.setStockId(sourceRecord.getStockId());
		item.setTypeId(sourceRecord.getCvTermId());
		item.setPredicate(sourceRecord.getPredicate());
		item.setRank(0);
		return item;
	}
	
}
