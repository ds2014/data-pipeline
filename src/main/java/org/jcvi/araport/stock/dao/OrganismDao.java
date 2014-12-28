package org.jcvi.araport.stock.dao;

import org.jcvi.araport.stock.domain.Organism;

public interface OrganismDao {
	
	public void create(Organism organism);
	public Organism findByName(String name);
	public void merge(Organism organism);
	public void update(Organism organism);

}
