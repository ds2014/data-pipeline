package org.araport.stock.writer;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.stock.dao.impl.StockPropertyDaoImpl;
import org.araport.stock.domain.DbXref;
import org.araport.stock.domain.StockProperty;
import org.araport.stock.utils.FileUtils;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Component("dbxref_batch_writer")
@Import({ DataSourceInfrastructureConfiguration.class, StockPropertyDaoImpl.class })
public class DbXrefJdbcBatchWriter {

	@Autowired
	DataSource targetDataSource;
	
	private static final Log log = LogFactory.getLog(DbXrefJdbcBatchWriter.class);
	
	private static final String INSERT_NAMED_PARAM_SQL_PATH = "/sql/transformations/dbxref/primary_stock_accession/insert_stock_property_namedparams_batchwriter.sql";
	
	private static final String INSERT_NAMED_PARAM_SQL = FileUtils.getSqlFileContents(INSERT_NAMED_PARAM_SQL_PATH);
	
	@Bean
	public ItemWriter<DbXref> dbXrefJdbcBatchWriter(){
		
		JdbcBatchItemWriter<DbXref> itemWriter = new JdbcBatchItemWriter<DbXref>();
		itemWriter.setSql(INSERT_NAMED_PARAM_SQL);
		itemWriter.setDataSource(targetDataSource);
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<DbXref>());
		return itemWriter;
		
	}
	
}
