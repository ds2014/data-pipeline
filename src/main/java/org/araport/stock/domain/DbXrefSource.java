package org.araport.stock.domain;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.araport.stock.listeners.ProtocolListener;

public class DbXrefSource {
	
	private static final Log log = LogFactory.getLog(DbXrefSource.class);
	
	private int dbXrefId;
	private int dbId;
	private long primaryAccession;
	private String secondaryAccession;
	private String version;
	private String description;
	private Db db;
	
	public DbXrefSource(){
		
	}
	
	public DbXrefSource(int dbId, long primaryAccession, String version, String description) {
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


	public long getPrimaryAccession() {
		return primaryAccession;
	}

	public String getSecondaryAccession() {
		return secondaryAccession;
	}

	public void setSecondaryAccession(String secondaryAccession) {
		this.secondaryAccession = secondaryAccession;
	}

	public void setPrimaryAccession(long primaryAccession) {
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

		
	public int getDbXrefId() {
		return dbXrefId;
	}

	public void setDbXrefId(int dbXrefId) {
		this.dbXrefId = dbXrefId;
	}

	@Override
	public String toString() {
		return "DbXref [dbXrefId=" + dbXrefId + ", dbId=" + dbId
				+ ", primaryAccession=" + primaryAccession + ", version="
				+ version + ", description=" + description + "]";
	}
	
	
}
