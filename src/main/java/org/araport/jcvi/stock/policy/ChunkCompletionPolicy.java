package org.araport.jcvi.stock.policy;

import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;

public class ChunkCompletionPolicy implements CompletionPolicy {

	@Override
	public boolean isComplete(RepeatContext arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isComplete(RepeatContext arg0, RepeatStatus arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public RepeatContext start(RepeatContext arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(RepeatContext arg0) {
		// TODO Auto-generated method stub
		
	}

}
