package org.araport.stock.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.araport.stock.domain.Stock;
import org.springframework.jdbc.core.RowMapper;

public class StockSourceRowMapper implements RowMapper<Stock> {

	@Override
	public Stock mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Stock stock = new Stock();
		stock.setStockId(rs.getLong("stock_id"));
		stock.setOrganismId(rs.getInt("organism_id"));
		stock.setDbxrefId(rs.getInt("dbxref_id"));
		stock.setName(rs.getString("name"));
		stock.setStockTypeId(rs.getInt("type_id"));
		stock.setDescription(rs.getString("description"));
		stock.setObsolete(rs.getBoolean("is_obsolete"));
		
		return stock;
	}

}
