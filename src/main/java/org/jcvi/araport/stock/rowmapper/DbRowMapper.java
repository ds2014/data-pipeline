package org.jcvi.araport.stock.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jcvi.araport.stock.domain.Db;
import org.springframework.jdbc.core.RowMapper;

public class DbRowMapper implements RowMapper<Db> {

	@Override
	public Db mapRow(ResultSet rs, int rowNum) throws SQLException {

		Db db = new Db();
		db.setDbId(rs.getInt("db_id"));
		db.setName(rs.getString("name"));
		db.setDescription(rs.getString("description"));
		db.setUrlprefix(rs.getString("urlprefix"));
		db.setUrl(rs.getString("url"));

		return db;
	}

}
