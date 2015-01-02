package org.jcvi.araport.stock.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.araport.stock.domain.SourceStockDrivingQuery;
import org.araport.stock.domain.StockPropertySource;
import org.springframework.jdbc.core.RowMapper;

public class StockPropertiesSourceRowMapper implements RowMapper<StockPropertySource> {

	@Override
	public StockPropertySource mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		
		StockPropertySource drivingQuery = new StockPropertySource();
		drivingQuery.setStockId(rs.getLong("stock_id"));
		drivingQuery.setKey(rs.getString("key"));
		drivingQuery.setValue(rs.getString("value"));
		drivingQuery.setTypeId(rs.getInt("type_id"));
			
		return drivingQuery;
	}

}
