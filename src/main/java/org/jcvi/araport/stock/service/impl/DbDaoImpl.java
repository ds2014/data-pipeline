package org.jcvi.araport.stock.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.jcvi.araport.stock.dao.DbDao;
import org.jcvi.araport.stock.reader.batch.DbRowMapper;
import org.jcvi.araport.stock.reader.domain.Db;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component("dbb_reader")
@Import({DataSourceInfrastructureConfiguration.class})
public class DbDaoImpl implements DbDao{

	@Autowired 
	DataSource targetDataSource;
	
private final String FIND_BY_NAME_SQL = "select db_id, name, description, urlprefix. url from chado.db where name =:name";

private NamedParameterJdbcOperations namedParameterJdbcTemplate;

	@Override
	public boolean create(Db db) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Db findDbByName(String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		
		List<Db> databaseList = namedParameterJdbcTemplate.query(FIND_BY_NAME_SQL, params, dbRowMapper());
		
		if (databaseList != null && databaseList.size() > 0) {
            return databaseList.get(0);
        } else {
            return null;
        }
		
				
	}

	@Override
	public boolean save(Db db) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setDataSource(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(targetDataSource);
    }
	
	public DbRowMapper dbRowMapper(){
		return new DbRowMapper();
	}
	
}
