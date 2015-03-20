package org.araport.stock.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.araport.stock.domain.Stock;
import org.araport.stock.domain.StockCVTermSource;
import org.springframework.jdbc.core.RowMapper;

public class StockCVTermSourceRowMapper implements RowMapper<StockCVTermSource> {

	@Override
	public StockCVTermSource mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		StockCVTermSource stockCVTerm = new StockCVTermSource();
		stockCVTerm.setStockId(rs.getLong("stock_id"));
		stockCVTerm.setCvTermId(rs.getInt("cvterm_id"));
		//stockCVTerm.setRank(rs.getInt("rank"));
		stockCVTerm.setRank(0);
		stockCVTerm.setPredicate(rs.getBoolean("is_not"));
				
		return stockCVTerm;
	}

}
