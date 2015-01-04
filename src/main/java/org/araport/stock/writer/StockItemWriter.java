package org.araport.stock.writer;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.stock.dao.DbXRefDao;
import org.araport.stock.dao.StockDao;
import org.araport.stock.dao.impl.DbXrefDaoImpl;
import org.araport.stock.dao.impl.StockDaoImpl;
import org.araport.stock.domain.DbXref;
import org.araport.stock.domain.Stock;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;


/**
 * Dummy {@link ItemWriter} which only logs data it receives.
 */
@Component("writer")
@Import({ DataSourceInfrastructureConfiguration.class, StockDaoImpl.class })
public class StockItemWriter implements ItemWriter<Stock> {

	@Autowired
	DataSource targetDataSource;
	
	private StockDao stockDao;
	
	private static final Log log = LogFactory.getLog(StockItemWriter.class);
	
	/**
	 * @see ItemWriter#write(java.util.List)
	 */
	public void write(List<? extends Stock> items) throws Exception {
		
			
		for (Stock item : items)  {
			
			log.info("Stock To Write= " + item);
			stockDao.merge(item);
			
			if (item.getStockRef().getDbXrefId()!=0){
				stockDao.merge(item.getStockRef());
			}
		}
		
		
	}
	
	@PostConstruct
	public void setDao(){
		this.stockDao = new StockDaoImpl();
		this.stockDao.setDataSource(targetDataSource);
	}

}
