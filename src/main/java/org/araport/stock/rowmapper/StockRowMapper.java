package org.araport.stock.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.araport.stock.domain.Stock;
import org.springframework.jdbc.core.RowMapper;

public class StockRowMapper implements RowMapper<Stock> {

	@Override
	public Stock mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Stock stock = new Stock();
		stock.setStockId(rs.getLong("stock_id"));
		stock.setDbxrefId(rs.getInt("dbxref_id"));
		stock.setName(rs.getString("name"));
		stock.setUniqueName(rs.getString("uniquename"));
		stock.setStockTypeId(rs.getInt("type_id"));
		stock.setTairObjectId(rs.getLong("tair_object_id"));
		stock.setDescription(rs.getString("description"));
		stock.setObsolete(rs.getBoolean("is_obsolete"));
		
		return stock;
		
	}

}
