package org.araport.stock.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.araport.stock.domain.DbXref;
import org.springframework.jdbc.core.RowMapper;

public class DbXrefRowMapper implements RowMapper<DbXref> {

	@Override
	public DbXref mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		DbXref dbXRef = new DbXref();
		dbXRef.setDbXrefId(rs.getInt("dbxref_id"));
		dbXRef.setDbId(rs.getInt("db_id"));
		dbXRef.setPrimaryAccession(rs.getString("accession"));
		dbXRef.setVersion(rs.getString("version"));
		dbXRef.setDescription(rs.getString("description"));
		
		return dbXRef;
	}

}
