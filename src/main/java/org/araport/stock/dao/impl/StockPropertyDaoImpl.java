package org.araport.stock.dao.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.stock.dao.StockPropertyDao;
import org.araport.stock.domain.StockProperty;
import org.araport.stock.rowmapper.CVRowMapper;
import org.araport.stock.rowmapper.StockPropertyRowMapper;
import org.araport.stock.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component("stockproperty_dao")
@Repository
@Import({ DataSourceInfrastructureConfiguration.class })
public class StockPropertyDaoImpl implements StockPropertyDao {

	private static final String INSERT_NAMED_PARAM_SQL_PATH = "/sql/transformations/stock_properties/insert_stock_property_namedparams.sql";
	
	private static final String INSERT_NAMED_PARAM_SQL = FileUtils.getSqlFileContents(INSERT_NAMED_PARAM_SQL_PATH);
	
	private static final Logger log = Logger
			.getLogger(StockPropertyDaoImpl.class);
	
	@Autowired
	DataSource targetDataSource;
	
	private NamedParameterJdbcOperations namedParameterJdbcTemplate;
	private JdbcTemplate jdbcTemplate;
	
		
	
	@Override
	public boolean create(StockProperty property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public StockProperty findDbByNameAndStockId(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean save(StockProperty property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void merge(StockProperty property) {
		log.info("Insert SQL Named Params: " + INSERT_NAMED_PARAM_SQL);
		
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("stock_id", property.getStockId());
		params.put("type_id", property.getTypeId());
		params.put("value", property.getValue());
		params.put("rank", property.getRank());
		
		int insertedCount = 0;
		
		//perform insert
			insertedCount = namedParameterJdbcTemplate.update(INSERT_NAMED_PARAM_SQL, params);
		
			log.info("Total Row Count Inserted:" + insertedCount);
		
	}

	@Override
	public StockProperty mergeAndReturn(StockProperty property) {
		// TODO Auto-generated method stub
		return null;
	}

	@PostConstruct
	public void setDataSource() {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				targetDataSource);
		this.jdbcTemplate = new JdbcTemplate(targetDataSource);
			
	}

	public StockPropertyRowMapper rowMapper() {
		return new StockPropertyRowMapper();
	}

	@Override
	public void setDataSource(DataSource datasource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				datasource);
		this.jdbcTemplate = new JdbcTemplate(datasource);
		
		
	}

	@Override
	public void executeSQL(String sql) {
		// TODO Auto-generated method stub
		
	}
	

}
