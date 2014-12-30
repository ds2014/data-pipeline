package org.jcvi.araport.stock.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.araport.stock.domain.Db;
import org.araport.stock.domain.Stock;
import org.araport.stock.domain.StockDbXref;
import org.springframework.jdbc.core.RowMapper;

public class StockDbXrefRowMapper implements RowMapper<StockDbXref> {

	@Override
	public StockDbXref mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		StockDbXref stockRef = new StockDbXref();
		stockRef.setStockId(rs.getLong("stock_id"));
		stockRef.setDbXrefId(rs.getInt("dbxref_id"));
		stockRef.setCurrent(true);
		
		return stockRef;
		
	}

}
