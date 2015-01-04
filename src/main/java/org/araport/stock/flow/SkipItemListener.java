package org.araport.stock.flow;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class SkipItemListener implements JobExecutionDecider {

	@Override
	public FlowExecutionStatus decide(JobExecution arg0, StepExecution arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
