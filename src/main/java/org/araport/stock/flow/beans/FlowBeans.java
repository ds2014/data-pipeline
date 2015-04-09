package org.araport.stock.flow.beans;

import org.araport.stock.processor.StockCVTermItemProcessor;
import org.araport.stock.tasklet.business.BulkLoadGermplasmSynonymsTasklet;
import org.araport.stock.tasklet.business.BulkLoadOrganismPropertiesTasklet;
import org.araport.stock.tasklet.business.BulkLoadOrganismTasklet;
import org.araport.stock.tasklet.business.BulkLoadStockCVTermTasklet;
import org.araport.stock.tasklet.business.BulkLoadStockDbxrefAccessionsTasklet;
import org.araport.stock.tasklet.business.BulkLoadStockRelationshipTasklet;
import org.araport.stock.tasklet.business.BulkLoadStockSynonymsTasklet;
import org.araport.stock.tasklet.business.GeneralModuleInitTasklet;
import org.araport.stock.tasklet.business.DbXrefPrimaryStockAccessionsPostLoadTasklet;
import org.araport.stock.tasklet.business.StockPostLoadingTasklet;
import org.araport.stock.tasklet.data.download.ImportOracleDataToPostgresTasklet;
import org.araport.stock.tasklet.staging.*;
import org.springframework.context.annotation.Bean;

public class FlowBeans {

	@Bean
	public BatchSchemaInitTasklet batchSchemaInitTasklet() {
		return new BatchSchemaInitTasklet();
	}
	
	@Bean
	public StagingSchemaInitTasklet stagingSchemaInitTasklet() {
		return new StagingSchemaInitTasklet();
	}
	
	@Bean
	public GeneralModuleInitTasklet generalModuleInitTasklet(){
		return new GeneralModuleInitTasklet();
	}
	
	@Bean
	public StageDbXrefExistingStockAccessionsTasklet stageDbXrefExistingStockAccessionsTasklet(){
		return new StageDbXrefExistingStockAccessionsTasklet();
	}
	
	@Bean
	public DbXrefPrimaryStockAccessionsPostLoadTasklet dbXrefPrimaryAccessionsPostLoadTasklet(){
		return new DbXrefPrimaryStockAccessionsPostLoadTasklet();
	}
	
	@Bean
	public StockStagingPreloadingTasklet dbStockStagingPreloadingTasklet(){
		return new StockStagingPreloadingTasklet();
	}
	
	@Bean
	public StockPostLoadingTasklet stockPostLoadingTasklet(){
		return new StockPostLoadingTasklet();
	}
	
	
	@Bean
	public BulkLoadOrganismTasklet bulkLoadOrganismTasklet(){
		return new BulkLoadOrganismTasklet();
	}
	
	@Bean
	public ImportOracleDataToPostgresTasklet importOracleDataToPostgresTasklet(){
		return new ImportOracleDataToPostgresTasklet();
	}
	
	
	@Bean
	public 
	BulkLoadStockCVTermTasklet bulkLoadStockCVTermTasklet(){
		return new BulkLoadStockCVTermTasklet();
	}
	
	@Bean
	public StagingStockRelationshipPreloadingTasklet stagingStockRelationshipPreloadingTasklet(){
		return new StagingStockRelationshipPreloadingTasklet();
	}
	
	@Bean
	public BulkLoadStockRelationshipTasklet bulkLoadStockRelationshipTasklet(){
		return new BulkLoadStockRelationshipTasklet();
	}
	
	@Bean
	public BulkLoadGermplasmSynonymsTasklet bulkLoadGermplasmSynonymsTasklet(){
		return new BulkLoadGermplasmSynonymsTasklet();
	}
	

	@Bean
	public BulkLoadStockSynonymsTasklet bulkLoadStockSynonymsTasklet(){
		return new BulkLoadStockSynonymsTasklet();
	}
	
	@Bean
	public BulkLoadStockDbxrefAccessionsTasklet bulkLoadStockDbxrefAccessionsTasklet(){
		return new BulkLoadStockDbxrefAccessionsTasklet();
	}
	
	@Bean
	public BulkLoadOrganismPropertiesTasklet bulkLoadOrganismPropertiesTasklet(){
		return new BulkLoadOrganismPropertiesTasklet();
	}
	
	
}
