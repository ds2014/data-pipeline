package org.araport.stock.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.araport.stock.domain.DbXref;
import org.araport.stock.domain.DbXrefSource;
import org.springframework.jdbc.core.RowMapper;

public class DbXrefSourceRowMapper implements RowMapper<DbXrefSource> {

	@Override
	public DbXrefSource mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		DbXrefSource dbXRef = new DbXrefSource();
		dbXRef.setDbId(rs.getInt("db_id"));
		dbXRef.setPrimaryAccession(rs.getLong("accession"));
				
		return dbXRef;
	}

}
