package org.jcvi.araport.stock.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.jcvi.araport.stock.dao.OrganismDao;
import org.jcvi.araport.stock.domain.Db;
import org.jcvi.araport.stock.domain.Organism;
import org.jcvi.araport.stock.rowmapper.DbXrefRowMapper;
import org.jcvi.araport.stock.rowmapper.OrganismRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component("organism_reader")
@Import({ DataSourceInfrastructureConfiguration.class })
public class OrganismDaoImpl implements OrganismDao {

	@Autowired
	DataSource targetDataSource;
	
	private static final Logger log = Logger
			.getLogger(OrganismDaoImpl.class);
	private final String FIND_BY_NAME_SQL = "select organism_id, abbreviation, genus, species,  common_name from chado.db where common_name =:name";

	private NamedParameterJdbcOperations namedParameterJdbcTemplate;
	
	@Override
	public void create(Organism organism) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Organism findByName(String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);

		log.info("Find Organism By Name Parameter ="
				+ name);

		List<Organism> results = namedParameterJdbcTemplate.query(
				FIND_BY_NAME_SQL, params, rowMapper());

		if (results != null && results.size() > 0) {
			return results.get(0);
		} else {
			return null;
		}
		
	}

	@Override
	public void merge(Organism organism) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Organism organism) {
		// TODO Auto-generated method stub
		
	}

	
	@PostConstruct
	public void setDataSource() {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				targetDataSource);
	}
	
	public OrganismRowMapper rowMapper() {
		return new OrganismRowMapper();
	}
}
