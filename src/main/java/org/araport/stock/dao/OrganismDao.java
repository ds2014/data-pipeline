package org.araport.stock.dao;

import javax.sql.DataSource;

import org.araport.stock.domain.Organism;

public interface OrganismDao {
	
	public void create(Organism organism);
	public Organism findByName(String name);
	public void merge(Organism organism);
	public void update(Organism organism);
	
	public void setDataSource(DataSource datasource);

}
