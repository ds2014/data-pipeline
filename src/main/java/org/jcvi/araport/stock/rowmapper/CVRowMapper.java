package org.jcvi.araport.stock.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jcvi.araport.stock.domain.CV;
import org.springframework.jdbc.core.RowMapper;

public class CVRowMapper implements RowMapper<CV>{

	@Override
	public CV mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		CV cv = new CV();
		cv.setCvId(rs.getInt("cv_id"));
		cv.setName(rs.getString("name"));
		cv.setDefintion(rs.getString("definition"));
		
		return cv;
	}

}
