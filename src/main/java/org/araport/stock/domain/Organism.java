package org.araport.stock.domain;

public class Organism {
	
	private int organismId;
    private String common_name;
    
    public Organism(){
    	
    }
    
	public int getOrganismId() {
		return organismId;
	}
	public void setOrganismId(int organismId) {
		this.organismId = organismId;
	}
	public String getCommonName() {
		return common_name;
	}
	public void setCommonName(String name) {
		this.common_name = name;
	}
	public Organism(int organismId, String name) {
		super();
		this.organismId = organismId;
		this.common_name = name;
	}
    
	@Override
	public String toString() {
		return "Organism [organismId=" + organismId + ", name=" + common_name + "]";
	}
    

}
