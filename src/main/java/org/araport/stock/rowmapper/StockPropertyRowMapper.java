package org.araport.stock.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.araport.stock.domain.StockProperty;
import org.springframework.jdbc.core.RowMapper;

public class StockPropertyRowMapper implements RowMapper <StockProperty>{

	@Override
	public StockProperty mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		
		StockProperty property = new StockProperty();
		property.setStockId(rs.getLong("stock_id"));
		property.setTypeId(rs.getInt("type_id"));
		property.setValue(rs.getString("value"));
		property.setRank(rs.getInt("rank"));
		
		return property;
	}

}
