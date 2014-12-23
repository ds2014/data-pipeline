package org.jcvi.araport.stock.reader.domain;

public class DbXref {
	
	private int dbId;
	private String primaryAccession;
	private String secondaryAccession;
	private String version;
	private String description;

	
	
	public DbXref(){
		
	}
	
	public DbXref(int dbId, String primaryAccession, String version, String description) {
		super();
		this.dbId = dbId;
		this.primaryAccession = primaryAccession;
		this.version = version;
		this.description = description;
	}
	
	public int getDbId() {
		return dbId;
	}


	public void setDbId(int dbId) {
		this.dbId = dbId;
	}


	public String getPrimaryAccession() {
		return primaryAccession;
	}

	public String getSecondaryAccession() {
		return secondaryAccession;
	}

	public void setSecondaryAccession(String secondaryAccession) {
		this.secondaryAccession = secondaryAccession;
	}

	public void setPrimaryAccession(String primaryAccession) {
		this.primaryAccession = primaryAccession;
	}

	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


}
