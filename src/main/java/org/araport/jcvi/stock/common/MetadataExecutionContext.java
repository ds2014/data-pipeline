package org.araport.jcvi.stock.common;

public class MetadataExecutionContext {

	private static DbLookupHolder dbLookupHolder;
	private static int tairDbId;
	private static int tairStockDbId;
	
	private MetadataExecutionContext() {

	}
	
	private static class MetadataExecutionContextHolder {
		public static final MetadataExecutionContext INSTANCE = new MetadataExecutionContext();
	}
	
	
	public static MetadataExecutionContext getInstance() {
		
		return MetadataExecutionContextHolder.INSTANCE;
	}
			
	public DbLookupHolder get(){
		return dbLookupHolder;
	}
	
	
	public void set(final DbLookupHolder holder) {
		dbLookupHolder = holder;
	}
	
	public static void setTairDbId(int dbId){
		tairDbId = dbId;
	}
	
	public static int getTairDbId(){
		return tairDbId;
	}
	
	public int getTairStockDbId(){
		return tairStockDbId;
	}
	
	public static void setTairStockDbId(int dbId){
		tairStockDbId = dbId;
	}
}
