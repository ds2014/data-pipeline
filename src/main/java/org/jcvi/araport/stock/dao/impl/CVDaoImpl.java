package org.jcvi.araport.stock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.jcvi.stock.utils.FileUtils;
import org.jcvi.araport.stock.dao.CVDao;
import org.jcvi.araport.stock.domain.CV;
import org.jcvi.araport.stock.domain.Db;
import org.jcvi.araport.stock.rowmapper.CVRowMapper;
import org.jcvi.araport.stock.rowmapper.DbXrefRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


@Component("cv_dao")
@Repository
@Import({ DataSourceInfrastructureConfiguration.class })
public class CVDaoImpl implements CVDao {

	@Autowired
	DataSource targetDataSource;
	
	private static final String INSERT_NAMED_PARAM_SQL_PATH = "/sql/bootstrap/cv/insert_cv_stock_property_namedparams.sql";
	private static final String UPDATE_NAMED_PARAM_SQL_PATH = "/sql/bootstrap/cv/update_cv_stock_property_namedparams.sql";
	
	private static final String INSERT_NONAMED_PARAM_SQL_PATH = "/sql/bootstrap/cv/insert_cv_stock_property_nonamedparams.sql";
	private static final String UPDATE_NONAMED_PARAM_SQL_PATH = "/sql/bootstrap/cv/update_cv_stock_property_nonamedparams.sql";
	
	private static final String INSERT_NAMED_PARAM_SQL = FileUtils.getSqlFileContents(INSERT_NAMED_PARAM_SQL_PATH);
	private static final String UPDATE_NAMED_PARAM_SQL = FileUtils.getSqlFileContents(UPDATE_NAMED_PARAM_SQL_PATH);
	
	
	private static final String INSERT_NONAMED_PARAM_SQL = FileUtils.getSqlFileContents(INSERT_NONAMED_PARAM_SQL_PATH);
	private static final String UPDATE_NONAMED_PARAM_SQL = FileUtils.getSqlFileContents(UPDATE_NONAMED_PARAM_SQL_PATH);
	
	private NamedParameterJdbcOperations namedParameterJdbcTemplate;
	private JdbcTemplate jdbcTemplate;
	
	private static final Logger log = Logger
			.getLogger(CVDaoImpl.class);
	
	@Override
	public boolean create(CV cv) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Db findDbByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean save(CV cv) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void merge(CV cv) {
		
		log.info("Insert SQL Named Params: " + INSERT_NAMED_PARAM_SQL);
		log.info("Update SQL Named Params: " + UPDATE_NAMED_PARAM_SQL);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", cv.getName());
		params.put("definition", cv.getDefintion());
					
		int updatedCount = namedParameterJdbcTemplate.update(UPDATE_NAMED_PARAM_SQL, params);
		int insertedCount = 0;
		
		if (updatedCount == 0){ //perform insert
			insertedCount = namedParameterJdbcTemplate.update(INSERT_NAMED_PARAM_SQL, params);
		}
		
		log.info("Total Row Count Updated:" + updatedCount);
		log.info("Total Row Count Inserted:" + insertedCount);
		
		
	}

	@Override
	public CV mergeAndReturn(final CV cv) {
		
		log.info("Insert SQL No Named Params: " + INSERT_NONAMED_PARAM_SQL);
		log.info("Update SQL No Named Params: " + UPDATE_NONAMED_PARAM_SQL);
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int updatedCount = jdbcTemplate.update(new PreparedStatementCreator() {
	        public PreparedStatement createPreparedStatement(
	            Connection connection) throws SQLException {
	                PreparedStatement ps = connection.prepareStatement(
	                		UPDATE_NONAMED_PARAM_SQL, new String[] { "cv_id" });
	                
	                ps.setString(1, cv.getName());
	                ps.setString(2, cv.getDefintion());
	                ps.setString(3, cv.getName());
	                
	                
	                
	                return ps;
	            }
	        }, keyHolder);
		
		int insertedCount = 0;
		if (updatedCount == 0){
			insertedCount = jdbcTemplate.update(new PreparedStatementCreator() {
			        public PreparedStatement createPreparedStatement(
			            Connection connection) throws SQLException {
			                PreparedStatement ps = connection.prepareStatement(
			                		INSERT_NONAMED_PARAM_SQL, new String[] { "dbxref_id" });
			                
			                ps.setString(1, cv.getName());
			                ps.setString(2, cv.getDefintion());
			                		                
			                return ps;
			            }
			        }, keyHolder);
		   }
			log.info("CV Primary Key generated :" + keyHolder.getKey().intValue());
		
			CV createdCV = new CV();
				createdCV.setCvId(keyHolder.getKey().intValue());
				createdCV.setName(cv.getName());
				createdCV.setDefintion(cv.getDefintion());
				
		return createdCV;
	}

		
	@PostConstruct
	public void setDataSource() {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				targetDataSource);
		this.jdbcTemplate = new JdbcTemplate(targetDataSource);
			
	}

	public CVRowMapper rowMapper() {
		return new CVRowMapper();
	}

	@Override
	public void setDataSource(DataSource datasource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				datasource);
		this.jdbcTemplate = new JdbcTemplate(datasource);
		
		
	}
	
	public void setSQL(){
		
	}

}
