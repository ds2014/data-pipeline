package org.jcvi.araport.stock.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.jcvi.araport.stock.dao.StockDao;
import org.jcvi.araport.stock.domain.DbXref;
import org.jcvi.araport.stock.domain.Stock;
import org.jcvi.araport.stock.rowmapper.OrganismRowMapper;
import org.jcvi.araport.stock.rowmapper.StockRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component("stock_reader")
@Import({ DataSourceInfrastructureConfiguration.class })
public class StockDaoImpl implements StockDao{

	@Autowired
	DataSource targetDataSource;
	
	private static final Logger log = Logger
			.getLogger(OrganismDaoImpl.class);
	private final String FIND_BY_NAME_SQL = "select organism_id, abbreviation, genus, species,  common_name from chado.organism where common_name =:name";
	
	private final String FIND_BY_SOURCE_ID_SQL = "SELECT s.stock_id, " +
			"acc.dbxref_id, " +
			"6 organism_id, " +
			"cast(s.name as varchar(255)) as name, " +
			"cast(s.name as text) as uniquename, " +
			"cast(s.description as text) as description, " +
			"c.cvterm_id as type_id, " +
			"cast (s.is_obsolete as boolean) as is_obsolete, " +
			"tair_object_id " +
		"FROM  tair_stg.stock s JOIN tair_stg.stocktype st " +
				"ON s.stock_type_id = st.stock_type_id JOIN cvterm c " +
				"ON c.name = st.stock_type JOIN dbxref dbx " +
				"ON dbx.dbxref_id = c.dbxref_id JOIN dbxref acc " +
				"ON cast(s.stock_id as varchar(255)) = acc.accession AND " +
			    "acc.db_id = staging.get_tair_db_id_by_name('TAIR Stock') " +
		"WHERE 	dbx.db_id = 1 " +			
			"and s.stock_id =:stock_id";

	private NamedParameterJdbcOperations namedParameterJdbcTemplate;
	
	
	@Override
	public void create(Stock stock) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Stock stock) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void merge(Stock stock) {
		
		
	}

	@Override
	public void setDataSource(DataSource datasource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				datasource);
		
	}

	@Override
	public Stock findStockById(int stockId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stock lookupSourceStockById(long stockId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("stock_id", stockId);
		
		log.info("Find Source Stock Id: "
				+ "stock Id = " +  stockId);

		List<Stock> results = namedParameterJdbcTemplate.query(
				FIND_BY_SOURCE_ID_SQL, params, rowMapper());

		if (results != null && results.size() > 0) {
			
			log.info("Found Source Stock Record: " + results.get(0));
			
			return results.get(0);
		} else {
			return null;
		}
		
		}
	
	@PostConstruct
	public void setDataSource() {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				targetDataSource);
	}
	
	public StockRowMapper rowMapper() {
		return new StockRowMapper();
	}

	
}
