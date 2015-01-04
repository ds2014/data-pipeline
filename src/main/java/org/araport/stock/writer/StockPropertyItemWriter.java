package org.araport.stock.writer;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.stock.dao.StockDao;
import org.araport.stock.dao.StockPropertyDao;
import org.araport.stock.dao.impl.StockDaoImpl;
import org.araport.stock.dao.impl.StockPropertyDaoImpl;
import org.araport.stock.domain.Stock;
import org.araport.stock.domain.StockProperty;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Component("stockproperty_writer")
@Import({ DataSourceInfrastructureConfiguration.class, StockPropertyDaoImpl.class })
public class StockPropertyItemWriter implements ItemWriter<StockProperty> {

	@Autowired
	DataSource targetDataSource;
	
	private StockPropertyDao stockPropertyDao;
	
	private static final Log log = LogFactory.getLog(StockPropertyItemWriter.class);
	
	@Override
	public void write(List<? extends StockProperty> items) throws Exception {
		
			for (StockProperty item : items)  {
			
			log.info("StockProperty To Write= " + item);
			
			stockPropertyDao.merge(item);
			//stockDao.merge(item);
			
			//if (item.getStockRef().getDbXrefId()!=0){
				//stockDao.merge(item.getStockRef());
		//	}
		}
		
	}
	
	@PostConstruct
	public void setDao(){
		this.stockPropertyDao = new StockPropertyDaoImpl();
		this.stockPropertyDao.setDataSource(targetDataSource);
	}

}
