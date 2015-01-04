package org.araport.stock.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.araport.stock.domain.CV;
import org.araport.stock.domain.CVTerm;
import org.springframework.jdbc.core.RowMapper;

public class CVTermRowMapper implements RowMapper<CVTerm> {

	@Override
	public CVTerm mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		CVTerm cvTerm = new CVTerm();
		cvTerm.setCvTermId(rs.getInt("cvterm_id"));
		cvTerm.setCvId(rs.getInt("cv_id"));
		cvTerm.setName(rs.getString("name"));
		cvTerm.setDbXrefId(rs.getInt("dbxref_id"));
		cvTerm.setDefinition(rs.getString("definition"));
		cvTerm.setIs_obsolete(rs.getInt("is_obsolete"));
		cvTerm.setIs_relationshiptype(rs.getInt("is_relationshiptype"));
		
		return cvTerm;
	}

}
