package org.araport.jcvi.stock.executors;

import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

public class TaskExecutorConfig {

	 @Bean
	    public TaskExecutor taskExecutor() {
	        return new SimpleAsyncTaskExecutor();
	    }
}
