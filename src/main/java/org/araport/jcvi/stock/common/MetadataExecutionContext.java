package org.araport.jcvi.stock.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MetadataExecutionContext {

	private static DbLookupHolder dbLookupHolder;
	private static int tairDbId;
	private static int tairStockDbId;
	private static Map<String, String> cvTermStockLookup = new ConcurrentHashMap<String, String>();
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
			cvTermStockProperties.add(ApplicationConstants.IS_SEED);
			cvTermStockProperties.add(ApplicationConstants.DATE_LAST_MODIFIED);
			cvTermStockProperties.add(ApplicationConstants.FORMAT_RECEIVED);
			cvTermStockProperties.add(ApplicationConstants.MEDIA);
			cvTermStockProperties.add(ApplicationConstants.GROWTH_TEMPERATURE);
			cvTermStockProperties.add(ApplicationConstants.DURATION_OF_GROWTH);
			cvTermStockProperties.add(ApplicationConstants.FORMAT_SHIPPED);
			cvTermStockProperties.add(ApplicationConstants.KIT_CONTENTS);
			cvTermStockProperties.add(ApplicationConstants.RELEASE_DATE);
			cvTermStockProperties.add(ApplicationConstants.NUMBER_IN_SET);
			cvTermStockProperties.add(ApplicationConstants.BOX);
			cvTermStockProperties.add(ApplicationConstants.POSITION);
			cvTermStockProperties.add(ApplicationConstants.NUM_LINES);
			cvTermStockProperties.add(ApplicationConstants.LOCATION);
			cvTermStockProperties.add(ApplicationConstants.HAS_STOCK_NOTES);
			cvTermStockProperties.add(ApplicationConstants.ABRC_COMMENTS);
			cvTermStockProperties.add(ApplicationConstants.DATE_ENTERED);
			cvTermStockProperties
					.add(ApplicationConstants.IS_CLASSICAL_MAPPING);
			cvTermStockProperties
					.add(ApplicationConstants.IS_MOLECULAR_MAPPING);
			cvTermStockProperties
					.add(ApplicationConstants.SPECIAL_GROWTH_CONDITIONS);
			cvTermStockProperties.add(ApplicationConstants.IS_GERMPLASM);
			
		
		
		allcvTermStockProperties.clear();
		
		allcvTermStockProperties.addAll(cvTermStockProperties);
		
		allcvTermStockProperties.add(ApplicationConstants.HAS_FOREIGN_DNA);
		allcvTermStockProperties.add(ApplicationConstants.HAS_POLYMORPHISMS);
		allcvTermStockProperties.add(ApplicationConstants.IS_MUTANT);
		allcvTermStockProperties.add(ApplicationConstants.IS_ANEPLOID);
		allcvTermStockProperties
				.add(ApplicationConstants.IS_ANEPLOID_CHROMOSOME);
		allcvTermStockProperties.add(ApplicationConstants.IS_NATUTAL_VARIANT);
		allcvTermStockProperties.add(ApplicationConstants.IS_MAPPINGL_STRAIN);
		allcvTermStockProperties.add(ApplicationConstants.PLOIDY);
		allcvTermStockProperties.add(ApplicationConstants.SPECIES_VARIANT_ID);
		allcvTermStockProperties.add(ApplicationConstants.TAXON_ID);
		
		}

	}

}
