package org.jcvi.araport.stock.listeners;

import org.apache.log4j.Logger;
import org.araport.jcvi.stock.common.ApplicationConstants;
import org.araport.jcvi.stock.common.DbLookupHolder;
import org.araport.jcvi.stock.common.MetadataExecutionContext;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;

public class LogStepStartStopListener implements StepExecutionListener {
	
	private static final Logger  log = Logger.getLogger(LogStepStartStopListener.class);

	@Override
	public ExitStatus afterStep(StepExecution execution) {
	        log.info(execution.getStepName() + " has ended!");

	        ExecutionContext jobContext = execution.getJobExecution().getExecutionContext();
	        
	        if (jobContext.containsKey(ApplicationConstants.TAIR_DB_ID)){
	        	 log.info(ApplicationConstants.TAIR_DB_ID + ": " + jobContext.get(ApplicationConstants.TAIR_DB_ID) );
	        }
	        if (jobContext.containsKey(ApplicationConstants.TAIR_STOCK_DB_ID)){
	        	 log.info(ApplicationConstants.TAIR_STOCK_DB_ID + ": " + jobContext.get(ApplicationConstants.TAIR_STOCK_DB_ID) );
	        }
	        
	       return execution.getExitStatus();
	    }
	

	@Override
	public void beforeStep(StepExecution execution) {
		log.info(execution.getStepName() + " has begun!");
		
		ExecutionContext jobContext = execution.getJobExecution().getExecutionContext();
		
		DbLookupHolder dbLookupHolder = new DbLookupHolder();
		
	  if (jobContext.containsKey(ApplicationConstants.TAIR_DB_ID)){
       	 log.info(ApplicationConstants.TAIR_DB_ID + ": " + jobContext.get(ApplicationConstants.TAIR_DB_ID) );
       	dbLookupHolder.setTairDBId((Integer)jobContext.get(ApplicationConstants.TAIR_DB_ID));
       	     	
       }
       if (jobContext.containsKey(ApplicationConstants.TAIR_STOCK_DB_ID)){
       	 log.info(ApplicationConstants.TAIR_STOCK_DB_ID + ": " + jobContext.get(ApplicationConstants.TAIR_STOCK_DB_ID) );
       	dbLookupHolder.setTairStockDBId((Integer)jobContext.get(ApplicationConstants.TAIR_STOCK_DB_ID));
       	 
       }
       
       MetadataExecutionContext.getInstance().set(dbLookupHolder);
       
		
	}

}
