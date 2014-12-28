package org.jcvi.araport.stock.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.jcvi.araport.stock.dao.DbXRefDao;
import org.jcvi.araport.stock.domain.Db;
import org.jcvi.araport.stock.domain.DbXref;
import org.jcvi.araport.stock.processor.DbXrefItemProcessor;
import org.jcvi.araport.stock.rowmapper.DbRowMapper;
import org.jcvi.araport.stock.rowmapper.DbXrefRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component("dbx_dao")
@Import({ DataSourceInfrastructureConfiguration.class })
public class DbXrefDaoImpl implements DbXRefDao {

	@Autowired
	DataSource targetDataSource;

	private static final Logger log = Logger
			.getLogger(DbXrefDaoImpl.class);
	
	private final String FIND_BY_ACCESSION_AND_DB_SQL = "select db_id, accession, version, description from chado.dbxref where db_id =:db_id and accession =:accession";
    private final String UPDATE_SQL = "UPDATE chado.dbxref t SET db_id =:db_id, accession =:accession, version = :version, " +
    		"description =:description " +
    		"WHERE 	t.db_id =:db_id AND " + 
    		"t.accession =:accession";
    
    private final String INSERT_SQL = "INSERT INTO chado.dbxref (db_id, accession, version, description) " +
"VALUES (:db_id, :accession, :version, :description)";
	
	private NamedParameterJdbcOperations namedParameterJdbcTemplate;

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
				FIND_BY_ACCESSION_AND_DB_SQL, params, rowRowMapper());

		if (results != null && results.size() > 0) {
			return results.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void merge(DbXref dbXref) {
		//DbXref founddbXref = findDbXrefByAccessionAndDb(dbXref.getDbId(), dbXref.getPrimaryAccession());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("db_id", dbXref.getDbId());
		params.put("accession", dbXref.getPrimaryAccession());
		params.put("version", dbXref.getVersion());
		params.put("description", dbXref.getDescription());
			
		int updatedCount = namedParameterJdbcTemplate.update(UPDATE_SQL, params);
		
		if (updatedCount == 0){ //perform insert
			namedParameterJdbcTemplate.update(INSERT_SQL, params);
		}
	
		/*
		if (founddbXref != null){
			update(dbXref);
		}else{
			create(dbXref);
		}
		*/

	}

	@PostConstruct
	public void setDataSource() {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				targetDataSource);
	}

	public DbXrefRowMapper rowRowMapper() {
		return new DbXrefRowMapper();
	}

	@Override
	public void setDataSource(DataSource datasource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				datasource);
		
	}

}
