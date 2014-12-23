package org.jcvi.araport.stock.reader.batch;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jcvi.araport.stock.reader.domain.Stock;
import org.springframework.jdbc.core.RowMapper;

public class StockRowMapper implements RowMapper<Stock> {

	@Override
	public Stock mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Stock stock = new Stock();
		stock.setStockId(rs.getInt("stockId"));
		stock.setDbxrefId(rs.getInt("dbxref_id"));
		stock.setName(rs.getString("name"));
		stock.setStockTypeId(rs.getInt("stock_type_id"));
		stock.setTairObjectId(rs.getInt("tair_object_id"));
		stock.setDescription(rs.getString("description"));
		stock.setObsolete(rs.getBoolean("is_obsolete"));
		
		return stock;
		
	}

}
