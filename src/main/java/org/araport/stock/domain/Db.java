package org.araport.stock.domain;

import org.apache.log4j.Logger;
import org.araport.stock.listeners.ItemFailureLoggerListener;

public class Db {

	private static final Logger  log = Logger.getLogger(Db.class);
	
	private int dbId;
	private String name;
	private String description;
	private String urlprefix;
	private String url;
	
	
	public Db(){
		
	}
	
	public Db(String name, String description, String urlprefix, String url) {
		this.name = name;
		this.description = description;
		this.urlprefix = urlprefix;
		this.url = url;
	}
	
	
	public Db(int dbId, String name, String description, String urlprefix, String url) {
		
		this(name, description, urlprefix, url);
		this.dbId = dbId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result
				+ ((urlprefix == null) ? 0 : urlprefix.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Db other = (Db) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (urlprefix == null) {
			if (other.urlprefix != null)
				return false;
		} else if (!urlprefix.equals(other.urlprefix))
			return false;
		return true;
	}


	public int getDbId() {
		return dbId;
	}


	public void setDbId(int dbId) {
		this.dbId = dbId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getUrlprefix() {
		return urlprefix;
	}


	public void setUrlprefix(String urlprefix) {
		this.urlprefix = urlprefix;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}
	
	
	@Override
	public String toString() {
		return "Db [db_id=" + dbId + ", name=" + name + ", description="
				+ description + ", urlprefix=" + urlprefix + ", url=" + url
				+ "]";
	}
}
