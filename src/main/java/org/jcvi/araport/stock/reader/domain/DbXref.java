package org.jcvi.araport.stock.reader.domain;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.jcvi.araport.stock.listeners.ProtocolListener;

public class DbXref {
	
	private static final Log log = LogFactory.getLog(DbXref.class);
	
	private int dbId;
	private String primaryAccession;
	private String secondaryAccession;
	private String version;
	private String description;
	private Db db;
	
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


	public Db getDb() {
		return db;
	}

	public void setDb(Db db) {
		this.db = db;
	}

	public String toString(){
		
		StringBuilder result = new StringBuilder();
		
		result.append(" " + "dbId: " + dbId);
		
		if (!StringUtils.isBlank(this.primaryAccession)){
			result.append("; " + "accession: " + this.primaryAccession);
		}
		
		if (!StringUtils.isBlank(this.secondaryAccession)){
			result.append("; " + "secondary accession: " +this.secondaryAccession);
		}
		
		if (!StringUtils.isBlank(this.version)){
			result.append("; " + "version: " + this.version);
		}
		
		if (!StringUtils.isBlank(this.description)){
			result.append("; " + "description: " + this.description);
		}
		
		if (result.length() >0){
			return result.toString();
		}else
		{
			return null;
		}
	
		
		
	}
}
