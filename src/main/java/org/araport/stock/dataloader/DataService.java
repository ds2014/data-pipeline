package org.araport.stock.dataloader;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araport.stock.executors.LoaderThreadPool;
import org.araport.stock.executors.ThreadPoolFactory;
import org.springframework.stereotype.Component;

@Component
public class DataService {

	private static final Log log = LogFactory
			.getLog(DataService.class);
		
	private static ExecutorService dataServicePool = new LoaderThreadPool(
			1, // core thread pool size
			1, // maximum thread pool size
			1, // time to wait before resizing pool
			TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(1, true),
			new ThreadPoolFactory("Data Service Pool"));
	
	private static ExecutorService sqlTaskPool = new LoaderThreadPool(
			1, // core thread pool size
			1, // maximum thread pool size
			1, // time to wait before resizing pool
			TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(1, true),
			new ThreadPoolFactory("SQL Task Pool"));
	

	private DataService() {

	}

	private static class DataServiceHolder {
		public static final DataService INSTANCE = new  DataService();
	}

	public static DataService getInstance() {
			return DataServiceHolder.INSTANCE;
	}
	
	public static ExecutorService getDataServicePool() {
		return dataServicePool;
	}

	public static ExecutorService getSQLTaskPooll() {
		return sqlTaskPool;
	}

	public static void shutdownPools() {

		dataServicePool.shutdownNow();
		sqlTaskPool.shutdownNow();
		
	}
	
	@PreDestroy
	public void cleanUp() throws Exception {
	 log.info("Spring Container is destroy! Container clean up");
	  
	 shutdownPools();
	}
}
