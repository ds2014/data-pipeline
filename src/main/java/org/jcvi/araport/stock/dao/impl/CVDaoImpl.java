package org.jcvi.araport.stock.dao.impl;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.jcvi.araport.stock.dao.CVDao;
import org.jcvi.araport.stock.domain.CV;
import org.jcvi.araport.stock.domain.Db;
import org.jcvi.araport.stock.rowmapper.DbXrefRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


@Component("cv_dao")
@Repository
@Import({ DataSourceInfrastructureConfiguration.class })
public class CVDaoImpl implements CVDao {

	@Autowired
	DataSource targetDataSource;
	
	private NamedParameterJdbcOperations namedParameterJdbcTemplate;
	private JdbcTemplate jdbcTemplate;
	
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public CV mergeAndReturn(CV cv) {
		// TODO Auto-generated method stub
		return null;
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

}
