package org.jcvi.araport.stock.writer;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.stock.domain.Stock;
import org.araport.stock.domain.StockProperty;
import org.jcvi.araport.stock.dao.StockDao;
import org.jcvi.araport.stock.dao.impl.StockDaoImpl;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Component("stockproperty_writer")
@Import({ DataSourceInfrastructureConfiguration.class, StockDaoImpl.class })
public class StockPropertyItemWriter implements ItemWriter<StockProperty> {

	@Autowired
	DataSource targetDataSource;
	
	private StockDao stockDao;
	
	private static final Log log = LogFactory.getLog(StockPropertyItemWriter.class);
	
	@Override
	public void write(List<? extends StockProperty> items) throws Exception {
		
			for (StockProperty item : items)  {
			
			log.info("StockProperty To Write= " + item);
			//stockDao.merge(item);
			
			//if (item.getStockRef().getDbXrefId()!=0){
				//stockDao.merge(item.getStockRef());
		//	}
		}
		
	}

}
