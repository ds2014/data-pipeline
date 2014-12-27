package org.araport.jcvi.stock.policy;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

public class ExceptionSkipPolicy implements SkipPolicy {

	@Override
	public boolean shouldSkip(Throwable arg0, int arg1)
			throws SkipLimitExceededException {
		// TODO Auto-generated method stub
		return false;
	}

}
