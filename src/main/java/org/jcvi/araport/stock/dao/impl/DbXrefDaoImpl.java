package org.jcvi.araport.stock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.stock.domain.Db;
import org.araport.stock.domain.DbXref;
import org.jcvi.araport.stock.dao.DbXRefDao;
import org.jcvi.araport.stock.processor.DbXrefItemProcessor;
import org.jcvi.araport.stock.rowmapper.DbRowMapper;
import org.jcvi.araport.stock.rowmapper.DbXrefRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component("dbx_dao")
@Import({ DataSourceInfrastructureConfiguration.class })
public class DbXrefDaoImpl implements DbXRefDao {

	@Autowired
	DataSource targetDataSource;

	private static final Logger log = Logger
			.getLogger(DbXrefDaoImpl.class);
	
	private final String FIND_BY_ACCESSION_AND_DB_SQL = "select dbxref_id, db_id, accession, version, description from chado.dbxref where db_id =:db_id and accession =:accession";
    private final String UPDATE_SQL_BY_DB_ACCESSION = "UPDATE chado.dbxref t SET db_id =:db_id, accession =:accession, version = :version, " +
    		"description =:description " +
    		"WHERE 	t.db_id =:db_id AND " + 
    		"t.accession =:accession";
    
    private final String INSERT_SQLBY_DB_ACCESSION = "INSERT INTO chado.dbxref (db_id, accession, version, description) " +
"VALUES (:db_id, :accession, :version, :description)";
    
    private final String INSERT_SQL_BY_DB_ACCESSION_RETURN_KEY = "INSERT INTO chado.dbxref (db_id, accession, version, description) " +
			 "VALUES (?, ?, ?, ?)";
    
    private final String UPDATE_SQL_BY_DB_ACCESSION_RETURN_KEY = "UPDATE chado.dbxref t SET db_id =?, accession=?, version =?, " +
    		"description=? " +
    		"WHERE 	t.db_id=? AND " + 
    		"t.accession=?";
	
	private NamedParameterJdbcOperations namedParameterJdbcTemplate;
	private JdbcTemplate jdbcTemplate;

	@Override
	public boolean create(DbXref dbXref) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(DbXref dbXref) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DbXref findDbXrefByAccessionAndDb(int dbId, String accession) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("db_id", dbId);
		params.put("accession", accession);

		log.info("Find DbXref By DbId and Parameter: "
				+ "dbId = " + dbId + "accession = " + accession);

		List<DbXref> results = namedParameterJdbcTemplate.query(
				FIND_BY_ACCESSION_AND_DB_SQL, params, rowMapper());

		if (results != null && results.size() > 0) {
			return results.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void merge(DbXref dbXref) {
	   
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("db_id", dbXref.getDbId());
		params.put("accession", dbXref.getPrimaryAccession());
		params.put("version", dbXref.getVersion());
		params.put("description", dbXref.getDescription());
			
		int updatedCount = namedParameterJdbcTemplate.update(UPDATE_SQL_BY_DB_ACCESSION, params);
		
		if (updatedCount == 0){ //perform insert
			namedParameterJdbcTemplate.update(INSERT_SQLBY_DB_ACCESSION, params);
		}
			
	}

	@PostConstruct
	public void setDataSource() {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				targetDataSource);
		this.jdbcTemplate = new JdbcTemplate(targetDataSource);
	}

	public DbXrefRowMapper rowMapper() {
		return new DbXrefRowMapper();
	}

	@Override
	public void setDataSource(DataSource datasource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				datasource);
		this.jdbcTemplate = new JdbcTemplate(datasource);
		
		
	}

	@Override
	public DbXref mergeAndReturn(final DbXref dbXref) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("db_id", dbXref.getDbId());
		params.put("accession", dbXref.getPrimaryAccession());
		params.put("version", dbXref.getVersion());
		params.put("description", dbXref.getDescription());
			
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int rowCount = jdbcTemplate.update(new PreparedStatementCreator() {
	        public PreparedStatement createPreparedStatement(
	            Connection connection) throws SQLException {
	                PreparedStatement ps = connection.prepareStatement(
	                		UPDATE_SQL_BY_DB_ACCESSION_RETURN_KEY, new String[] { "dbxref_id" });
	                
	                ps.setInt(1, dbXref.getDbId());
	                ps.setString(2,dbXref.getPrimaryAccession());
	                ps.setString(3,dbXref.getVersion());
	                ps.setString(4,dbXref.getDescription());
	                ps.setInt(5, dbXref.getDbId());
	                ps.setString(6,dbXref.getPrimaryAccession());
	                
	                return ps;
	            }
	        }, keyHolder);
		
	   if (rowCount == 0){
		   rowCount = jdbcTemplate.update(new PreparedStatementCreator() {
		        public PreparedStatement createPreparedStatement(
		            Connection connection) throws SQLException {
		                PreparedStatement ps = connection.prepareStatement(
		                		INSERT_SQL_BY_DB_ACCESSION_RETURN_KEY, new String[] { "dbxref_id" });
		                
		                ps.setInt(1, dbXref.getDbId());
		                ps.setString(2,dbXref.getPrimaryAccession());
		                ps.setString(3,dbXref.getVersion());
		                ps.setString(4,dbXref.getDescription());
		                		                
		                return ps;
		            }
		        }, keyHolder);
	   }
		log.info("DbXRef Primary Key generated :" + keyHolder.getKey().intValue());
		
		DbXref createdDbXref = new DbXref();
		createdDbXref.setDbXrefId(keyHolder.getKey().intValue());
		createdDbXref.setDbId(dbXref.getDbId());
		createdDbXref.setPrimaryAccession(dbXref.getPrimaryAccession());
		createdDbXref.setVersion(dbXref.getVersion());
		createdDbXref.setDescription(dbXref.getDescription());
			
		return createdDbXref;
	}

}
