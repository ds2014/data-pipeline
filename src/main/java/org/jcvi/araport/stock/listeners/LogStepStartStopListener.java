package org.jcvi.araport.stock.listeners;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class LogStepStartStopListener implements StepExecutionListener {
	
	private static final Logger  log = Logger.getLogger(LogStepStartStopListener.class);

	@Override
	public ExitStatus afterStep(StepExecution execution) {
	        log.info(execution.getStepName() + " has ended!");

	       return execution.getExitStatus();
	    }
	

	@Override
	public void beforeStep(StepExecution execution) {
		log.info(execution.getStepName() + " has begun!");
		
	}

}
