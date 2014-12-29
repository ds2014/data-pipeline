package org.araport.jcvi.stock.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetadataExecutionContext {

	private static DbLookupHolder dbLookupHolder;
	private static int tairDbId;
	private static int tairStockDbId;
	private static Map<String, String> cvTermStockLookup = new HashMap<String, String>();
	private static Map<String, String> allCvTermStockLookup = new HashMap<String, String>();
	private static List<String> cvTermStockProperties = new ArrayList<String>();
	private static List<String> allcvTermStockProperties = new ArrayList<String>();

	private MetadataExecutionContext() {

	}
	
	static {
		initLookups();
	}

	private static class MetadataExecutionContextHolder {
		public static final MetadataExecutionContext INSTANCE = new MetadataExecutionContext();
	}

	public static MetadataExecutionContext getInstance() {

		return MetadataExecutionContextHolder.INSTANCE;
	}

	public DbLookupHolder get() {
		return dbLookupHolder;
	}

	public void set(final DbLookupHolder holder) {
		dbLookupHolder = holder;
	}

	public static void setTairDbId(int dbId) {
		tairDbId = dbId;
	}

	public static int getTairDbId() {
		return tairDbId;
	}

	public int getTairStockDbId() {
		return tairStockDbId;
	}

	public static void setTairStockDbId(int dbId) {
		tairStockDbId = dbId;
	}

	public static void initLookups() {

		if (cvTermStockLookup == null) {
			cvTermStockLookup = new HashMap<String, String>();
		}
		if (cvTermStockProperties == null) {
			cvTermStockProperties = new ArrayList<String>();
			cvTermStockProperties.clear();
		}
		
		populatecvTermStockProperties();

	}

	public Map<String, String> getCVTermStockLookup() {
		return cvTermStockLookup;
	}

	public List<String> getCvTermStockProperties() {
		return cvTermStockProperties;
	}

	
	public List<String> getAllCvTermStockProperties() {
		return allcvTermStockProperties;
	}
	
	private static void populatecvTermStockProperties() {

		if (cvTermStockProperties.size() == 0) {
			cvTermStockProperties.add(ApplicationContstants.IS_SEED);
			cvTermStockProperties.add(ApplicationContstants.DATE_LAST_MODIFIED);
			cvTermStockProperties.add(ApplicationContstants.FORMAT_RECEIVED);
			cvTermStockProperties.add(ApplicationContstants.MEDIA);
			cvTermStockProperties.add(ApplicationContstants.GROWTH_TEMPERATURE);
			cvTermStockProperties.add(ApplicationContstants.DURATION_OF_GROWTH);
			cvTermStockProperties.add(ApplicationContstants.FORMAT_SHIPPED);
			cvTermStockProperties.add(ApplicationContstants.KIT_CONTENTS);
			cvTermStockProperties.add(ApplicationContstants.RELEASE_DATE);
			cvTermStockProperties.add(ApplicationContstants.NUMBER_IN_SET);
			cvTermStockProperties.add(ApplicationContstants.BOX);
			cvTermStockProperties.add(ApplicationContstants.POSITION);
			cvTermStockProperties.add(ApplicationContstants.NUM_LINES);
			cvTermStockProperties.add(ApplicationContstants.LOCATION);
			cvTermStockProperties.add(ApplicationContstants.HAS_STOCK_NOTES);
			cvTermStockProperties.add(ApplicationContstants.ABRC_COMMENTS);
			cvTermStockProperties.add(ApplicationContstants.DATE_ENTERED);
			cvTermStockProperties
					.add(ApplicationContstants.IS_CLASSICAL_MAPPING);
			cvTermStockProperties
					.add(ApplicationContstants.IS_MOLECULAR_MAPPING);
			cvTermStockProperties
					.add(ApplicationContstants.SPECIAL_GROWTH_CONDITIONS);
			cvTermStockProperties.add(ApplicationContstants.IS_GERMPLASM);
			
		
		
		allcvTermStockProperties.clear();
		
		allcvTermStockProperties.addAll(cvTermStockProperties);
		
		allcvTermStockProperties.add(ApplicationContstants.HAS_FOREIGN_DNA);
		allcvTermStockProperties.add(ApplicationContstants.HAS_POLYMORPHISMS);
		allcvTermStockProperties.add(ApplicationContstants.IS_MUTANT);
		allcvTermStockProperties.add(ApplicationContstants.IS_ANEPLOID);
		allcvTermStockProperties
				.add(ApplicationContstants.IS_ANEPLOID_CHROMOSOME);
		allcvTermStockProperties.add(ApplicationContstants.IS_NATUTAL_VARIANT);
		allcvTermStockProperties.add(ApplicationContstants.IS_MAPPINGL_STRAIN);
		allcvTermStockProperties.add(ApplicationContstants.PLOIDY);
		allcvTermStockProperties.add(ApplicationContstants.SPECIES_VARIANT_ID);
		allcvTermStockProperties.add(ApplicationContstants.TAXON_ID);
		
		}

	}

}
