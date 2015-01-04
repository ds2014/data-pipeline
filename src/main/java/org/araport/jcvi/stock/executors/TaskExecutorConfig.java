package org.araport.jcvi.stock.executors;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@PropertySources(value = { @PropertySource("classpath:/partition.properties") })
public class TaskExecutorConfig {

	@Autowired
	private Environment environment;

	@Autowired
	private ResourceLoader resourceLoader;
	
	@Bean
	public TaskExecutor taskExecutor() {
		return new SimpleAsyncTaskExecutor();
	}
	
	@Bean
	public TaskExecutor concurrentExecutor(){
		
		ThreadPoolTaskExecutor poolExecutor = new ThreadPoolTaskExecutor();
		poolExecutor.setCorePoolSize(5);
		
		int gridSize = Integer.parseInt(environment
				.getProperty("stockprop.partition.size"));
		
		poolExecutor.setMaxPoolSize(gridSize);
		
		
		return poolExecutor;
		
	}
	
}
