package org.jcvi.araport.stock.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.jcvi.araport.stock.dao.GeneralDao;
import org.jcvi.araport.stock.rowmapper.StockPropertyRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component("general_dao")
@Repository
@Import({ DataSourceInfrastructureConfiguration.class })
public class GeneralDaoImpl implements GeneralDao {

	private static final Logger log = Logger
			.getLogger(GeneralDaoImpl.class);
	
	@Autowired
	DataSource targetDataSource;
	
	private NamedParameterJdbcOperations namedParameterJdbcTemplate;
	private JdbcTemplate jdbcTemplate;
	
	
	@Override
	public void executeSQL(String sql) {

		Map<String,Object> map=new HashMap<String,Object>();  
		
		this.namedParameterJdbcTemplate.execute(sql,map,new PreparedStatementCallback() {  
		    @Override  
		    public Object doInPreparedStatement(PreparedStatement ps)  
		            throws SQLException, DataAccessException {  
		        return ps.executeUpdate();  
		    }
		 
		});  
	
	
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
		

}
