package org.araport.stock.domain;

public class CV {
	
	private int cvId;
	private String name;
	private String defintion;
	
	public CV(){
		
	}

	public CV(int cvId, String name, String defintion) {
		super();
		this.cvId = cvId;
		this.name = name;
		this.defintion = defintion;
	}

	public int getCvId() {
		return cvId;
	}

	public void setCvId(int cvId) {
		this.cvId = cvId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefintion() {
		return defintion;
	}

	public void setDefintion(String defintion) {
		this.defintion = defintion;
	}
	
	

}
