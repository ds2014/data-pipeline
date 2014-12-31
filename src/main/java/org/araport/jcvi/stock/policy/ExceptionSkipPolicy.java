package org.araport.jcvi.stock.policy;

import org.apache.log4j.Logger;
import org.postgresql.util.PSQLException;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.dao.DataIntegrityViolationException;

public class ExceptionSkipPolicy implements SkipPolicy {

	private static final Logger log = Logger
			.getLogger(ExceptionSkipPolicy.class);
	
	private int skipLimit;

	@Override
	public boolean shouldSkip(Throwable t, int skipCount)
			throws SkipLimitExceededException {
		
		
		log.info("Should Skip: " + t.toString());
		
		if ((t instanceof PSQLException) || (t instanceof org.springframework.dao.DataIntegrityViolationException)) {
		return true;
		} else {
		return false;
		}

	}

	public void setSkipLimit(int skipLimit) {
		this.skipLimit = skipLimit;
	}

}
