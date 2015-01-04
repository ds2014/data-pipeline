package org.araport.stock.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.araport.stock.domain.Organism;
import org.springframework.jdbc.core.RowMapper;

public class OrganismRowMapper implements RowMapper<Organism> {

	@Override
	public Organism mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Organism organism = new Organism();
		organism.setOrganismId(rs.getInt("organism_id"));
		organism.setCommonName(rs.getString("common_name"));
		return organism;
	}

}
