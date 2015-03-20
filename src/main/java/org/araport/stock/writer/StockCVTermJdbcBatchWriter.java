package org.araport.stock.writer;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.stock.dao.StockPropertyDao;
import org.araport.stock.dao.impl.StockPropertyDaoImpl;
import org.araport.stock.domain.StockCVTerm;
import org.araport.stock.domain.StockProperty;
import org.araport.stock.utils.FileUtils;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Component("stock_cvterm_batch_writer")
@Import({ DataSourceInfrastructureConfiguration.class})
public class StockCVTermJdbcBatchWriter {
	
	@Autowired
	DataSource targetDataSource;
	
	private static final Log log = LogFactory.getLog(StockCVTermJdbcBatchWriter.class);
	
	//private static final String INSERT_NAMED_PARAM_SQL_PATH = "/sql/transformations/stock_cvtem/insert_stock_cvterm_namedparams_batchwriter.sql";
	
	private static final String INSERT_NAMED_PARAM_SQL_PATH = "/sql/transformations/stock_properties/insert_stock_property_namedparams_batchwriter1.sql";
	private static final String INSERT_NAMED_PARAM_SQL = FileUtils.getSqlFileContents(INSERT_NAMED_PARAM_SQL_PATH);
	
	@Bean
	public ItemWriter<StockCVTerm> stockCVTermJdbcBatchWriter(){
		
		JdbcBatchItemWriter<StockCVTerm> itemWriter = new JdbcBatchItemWriter<StockCVTerm>();
		itemWriter.setSql(INSERT_NAMED_PARAM_SQL);
		itemWriter.setDataSource(targetDataSource);
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<StockCVTerm>());
		return itemWriter;
		
	}

}
