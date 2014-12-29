package org.jcvi.araport.stock.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.jcvi.stock.common.ApplicationContstants;
import org.araport.jcvi.stock.common.MetadataExecutionContext;
import org.jcvi.araport.stock.dao.DbDao;
import org.jcvi.araport.stock.domain.Db;
import org.jcvi.araport.stock.processor.DbXrefItemProcessor;
import org.jcvi.araport.stock.rowmapper.DbRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component("dbb_reader")
@Import({ DataSourceInfrastructureConfiguration.class })
public class DbDaoImpl implements DbDao {

	@Autowired
	DataSource targetDataSource;

	private static final Logger log = Logger
			.getLogger(DbDaoImpl.class);
	private final String FIND_BY_NAME_SQL = "select db_id, name, description, urlprefix,  url from chado.db where name =:name";

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

		log.info("Metadata: TAIR Db Id " + MetadataExecutionContext.getInstance().getTairDbId());
		log.info("Metadata: TAIR Stock Db Id " + MetadataExecutionContext.getInstance().getTairStockDbId());
		
		log.info("Find Db By Name Parameter ="
				+ name);

		List<Db> databaseList = namedParameterJdbcTemplate.query(
				FIND_BY_NAME_SQL, params, dbRowMapper());

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

	@PostConstruct
	public void setDataSource() {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				targetDataSource);
	
		setTairDbId();
		
		setTairStockDb();
		
		
	}

    public int getTairDBId(){
    	
    	setTairDbId();
    	
    	return MetadataExecutionContext.getInstance().getTairDbId();
    }
    
    public int getTairStockDbId(){
    	
    	setTairStockDb();
    	
    	return MetadataExecutionContext.getInstance().getTairStockDbId();
    }
	
    
    public void setTairDbId(){
    	
    	if (MetadataExecutionContext.getInstance().getTairDbId()==0){
			Db tair_db = findDbByName(ApplicationContstants.TAIR_DB_NAME);
			MetadataExecutionContext.getInstance().setTairDbId(tair_db.getDbId());
		}
		
    }
    
    public void setTairStockDb(){
    	
    	if (MetadataExecutionContext.getInstance().getTairStockDbId()==0){
			Db tair_stock_db =  findDbByName(ApplicationContstants.TAIR_STOCK_DB_NAME);
			MetadataExecutionContext.getInstance().setTairStockDbId(tair_stock_db.getDbId());
		}
    }
    
	public DbRowMapper dbRowMapper() {
		return new DbRowMapper();
	}

}
