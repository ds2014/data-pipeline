package org.araport.stock.reader;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.stock.dao.impl.DbDaoImpl;
import org.araport.stock.domain.StockProperty;
import org.araport.stock.rowmapper.StockPropertiesSourceRowMapper;
import org.araport.stock.rowmapper.beans.RowMapperBeans;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;


@Component("source_stockproperty_jdbc_reader")
@Import({ DataSourceInfrastructureConfiguration.class, RowMapperBeans.class })
@StepScope
public class StockPropertiesJdbcItemReader extends JdbcPagingItemReader<StockProperty>{

	private static final Logger log = Logger
			.getLogger(StockPropertiesJdbcItemReader.class);

	@Autowired
	DataSource targetDataSource;

	@Autowired
	StockPropertiesSourceRowMapper stockPropertiesSourceMapper;
	
	@Value("#{stepExecutionContext['minValue']}")
	private String minValue;
	
	@Value("#{stepExecutionContext['maxValue']}")
	private String maxValue;
	
	@Override
	@Resource
	public void setDataSource(DataSource dataSource){
	    super.setDataSource(targetDataSource);
	 	}
	
	@Override
	public void setQueryProvider(PagingQueryProvider queryProvider) {
		super.setQueryProvider(queryProvider);
	}
	
	@Override
	public void setRowMapper(RowMapper<StockProperty> rowMapper) {
		super.setRowMapper(rowMapper);
	}
		
	@Override
	public void setPageSize(int pageSize) {
		super.setPageSize(pageSize);
	}
	
	@Override
	public void setSaveState(boolean saveState){
		super.setSaveState(saveState);
	}
	
	@Override
	public void setFetchSize(int fetchSize){
		super.setFetchSize(fetchSize);
	}
	
	
	@BeforeStep
	public void initializeValues(StepExecution stepExecution) {

		ExecutionContext stepContext = stepExecution.getExecutionContext();
        
		log.info("JDBC PAGING ITEM READER!");
		
		if (stepContext.containsKey("minValue")) {
			log.info("MIN VALUE " + ": " + stepContext.get("minValue"));

		} else {
			log.info("NO MIN VALUE FOUND!");
		}

	}
}
