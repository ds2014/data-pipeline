package org.jcvi.araport.stock.reader.batch.dao;

public interface DbXRefDao {
	
	public boolean addDbXRef(int dbId, String accession, String version, String description);
	
	public boolean updateDbXRef(int dbId, String accession, String version, String description);
	
	public boolean updateDbXRebyAccessionDb(int dbId, String accession);
	
	public int findDbXrefByAccessionDb(int dbId, String accession);

}
