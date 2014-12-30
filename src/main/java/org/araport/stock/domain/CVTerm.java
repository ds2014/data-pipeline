package org.araport.stock.domain;

public class CVTerm {

	private int cvTermId;
	private int cvId;
	private String name;
	private String definition;
	private int dbXrefId;
	private int is_relationshiptype;
	private int is_obsolete;
	
	public CVTerm(){
		
	}

	public CVTerm(int cvterm_id, int cv_id, String name, String definition,
			int is_relationshiptype, int is_obsolete) {
		super();
		this.cvTermId = cvterm_id;
		this.cvId = cv_id;
		this.name = name;
		this.definition = definition;
		this.is_relationshiptype = is_relationshiptype;
		this.is_obsolete = is_obsolete;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public int getIs_relationshiptype() {
		return is_relationshiptype;
	}

	public void setIs_relationshiptype(int is_relationshiptype) {
		this.is_relationshiptype = is_relationshiptype;
	}

	public int getIs_obsolete() {
		return is_obsolete;
	}

	public void setIs_obsolete(int is_obsolete) {
		this.is_obsolete = is_obsolete;
	}

	public int getCvTermId() {
		return cvTermId;
	}

	public void setCvTermId(int cvTermId) {
		this.cvTermId = cvTermId;
	}

	public int getCvId() {
		return cvId;
	}

	public void setCvId(int cvId) {
		this.cvId = cvId;
	}

	public int getDbXrefId() {
		return dbXrefId;
	}

	public void setDbXrefId(int dbXrefId) {
		this.dbXrefId = dbXrefId;
	}

	@Override
	public String toString() {
		return "CVTerm [cvTermId=" + cvTermId + ", cvId=" + cvId + ", name="
				+ name + ", definition=" + definition + ", dbXrefId="
				+ dbXrefId + ", is_relationshiptype=" + is_relationshiptype
				+ ", is_obsolete=" + is_obsolete + "]";
	}

	
	
	}
