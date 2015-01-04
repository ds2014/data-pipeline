package org.araport.stock.flow.beans;

import org.araport.stock.tasklet.business.GeneralModuleInitTasklet;
import org.araport.stock.tasklet.business.DbXrefPrimaryStockAccessionsPostLoadTasklet;
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
}
