package org.jcvi.araport.stock.writer;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jcvi.araport.stock.domain.DbXref;
import org.jcvi.araport.stock.domain.Stock;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;


/**
 * Dummy {@link ItemWriter} which only logs data it receives.
 */
@Component("writer")
public class StockItemWriter implements ItemWriter<Stock> {

	private static final Log log = LogFactory.getLog(StockItemWriter.class);
	
	/**
	 * @see ItemWriter#write(java.util.List)
	 */
	public void write(List<? extends Stock> items) throws Exception {
		
		if(items.get(0) != null){
			Stock stock = items.get(0);
			
			System.out.println("Stock = " + stock);
		}
	}

}
