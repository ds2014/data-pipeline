package org.jcvi.araport.stock.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.stock.domain.SourceStockDrivingQuery;
import org.jcvi.araport.stock.dao.impl.DbDaoImpl;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.RowMapper;

public class SourceStockDrivingQueryRowMapper implements RowMapper<SourceStockDrivingQuery> {

	@Override
	public SourceStockDrivingQuery mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		
		
		SourceStockDrivingQuery drivingQuery = new SourceStockDrivingQuery();
		drivingQuery.setStockId(rs.getLong("stock_id"));
			
		return drivingQuery;
	}

}
