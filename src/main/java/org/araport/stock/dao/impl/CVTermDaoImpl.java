package org.araport.stock.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.stock.dao.CVTermDao;
import org.araport.stock.domain.CVTerm;
import org.araport.stock.rowmapper.CVTermRowMapper;
import org.araport.stock.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


@Component("cv_term_dao")
@Repository
@Import({ DataSourceInfrastructureConfiguration.class })
public class CVTermDaoImpl implements CVTermDao{

	@Autowired
	DataSource targetDataSource;
	
	private static final String INSERT_NAMED_PARAM_SQL_PATH = "/sql/bootstrap/cvterm/insert_cvterm_property_namedparams.sql";
	private static final String UPDATE_NAMED_PARAM_SQL_PATH = "/sql/bootstrap/cvterm/update_cvterm_property_namedparams.sql";
	
	private static final String INSERT_NONAMED_PARAM_SQL_PATH = "/sql/bootstrap/cv/insert_cv_stock_property_nonamedparams.sql";
	private static final String UPDATE_NONAMED_PARAM_SQL_PATH = "/sql/bootstrap/cv/update_cv_stock_property_nonamedparams.sql";
	
	private static final String INSERT_NAMED_PARAM_SQL = FileUtils.getSqlFileContents(INSERT_NAMED_PARAM_SQL_PATH);
	private static final String UPDATE_NAMED_PARAM_SQL = FileUtils.getSqlFileContents(UPDATE_NAMED_PARAM_SQL_PATH);
	
	
	private static final String INSERT_NONAMED_PARAM_SQL = FileUtils.getSqlFileContents(INSERT_NONAMED_PARAM_SQL_PATH);
	private static final String UPDATE_NONAMED_PARAM_SQL = FileUtils.getSqlFileContents(UPDATE_NONAMED_PARAM_SQL_PATH);
	
	private NamedParameterJdbcOperations namedParameterJdbcTemplate;
	private JdbcTemplate jdbcTemplate;
	
	private static final Logger log = Logger
			.getLogger(CVTermDaoImpl.class);
	
	@Override
	public boolean create(CVTerm cv) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CVTerm findByCVTermNameCVNameAndDbName(String cvTermName,
			String cvName, String dbName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CVTerm> findByCvIdAndDbId(int cvId, int dbId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CVTerm> findByCvIdAndDbName(int cvId, String dbName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CVTerm> findByCvNameAndDbId(String cvName, int dbId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CVTerm> findByCvNameAndDbName(String cvName, String dbName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean save(CVTerm cvTerm) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void merge(CVTerm cvTerm) {
		log.info("Insert SQL Named Params: " + INSERT_NAMED_PARAM_SQL);
		log.info("Update SQL Named Params: " + UPDATE_NAMED_PARAM_SQL);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cv_id", cvTerm.getCvId());
		params.put("name", cvTerm.getName());
		params.put("definition", cvTerm.getDefinition());
		params.put("dbxref_id", cvTerm.getDbXrefId());
		params.put("is_obsolete", cvTerm.getIs_obsolete());
		params.put("is_relationshiptype", cvTerm.getIs_relationshiptype());
		
		int updatedCount = namedParameterJdbcTemplate.update(UPDATE_NAMED_PARAM_SQL, params);
		int insertedCount = 0;
		
		if (updatedCount == 0){ //perform insert
			insertedCount = namedParameterJdbcTemplate.update(INSERT_NAMED_PARAM_SQL, params);
		}
		
		log.info("Total Row Count Updated:" + updatedCount);
		log.info("Total Row Count Inserted:" + insertedCount);
		
	}

	@Override
	public CVTerm mergeAndReturn(CVTerm cvTerm) {
		// TODO Auto-generated method stub
		return null;
	}

	@PostConstruct
	public void setDataSource() {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				targetDataSource);
		this.jdbcTemplate = new JdbcTemplate(targetDataSource);
			
	}

	public CVTermRowMapper rowMapper() {
		return new CVTermRowMapper();
	}

	@Override
	public void setDataSource(DataSource datasource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				datasource);
		this.jdbcTemplate = new JdbcTemplate(datasource);
		
		
	}

}
