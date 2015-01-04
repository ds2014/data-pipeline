package org.araport.stock.listeners;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ItemProcessListener;

public class LogProcessListener implements ItemProcessListener<Object, Object> {

	private static final Logger  log = Logger.getLogger(LogProcessListener.class);

	public void afterProcess(Object item, Object result) {
		if(item!=null) log.info("Input to Item Processor: " + item.toString());
		if(result!=null) log.info("Output of Item Processor: " + result.toString());
		
	}

	public void beforeProcess(Object item) {
	}

	public void onProcessError(Object item, Exception e) {
	}

}