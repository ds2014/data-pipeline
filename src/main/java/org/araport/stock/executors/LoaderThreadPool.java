package org.araport.stock.executors;

import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class LoaderThreadPool extends ThreadPoolExecutor {

	private final static Logger log = LoggerFactory
			.getLogger(LoaderThreadPool.class);

	public LoaderThreadPool(int corePoolSize, int maxPoolSize,
			long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {

		super(corePoolSize, maxPoolSize, keepAliveTime, unit, workQueue,
				threadFactory);
		this.setMaximumPoolSize(maxPoolSize);
		this.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		this.prestartAllCoreThreads();

	}

	/**
	 * 
	 * Returns Pool Name
	 */
	public String getPoolName() {
		return this.getThreadFactory().toString();
	}

	/**
	 * Logs current pool statistics
	 */
	public void getPoolLog() {

		log.info("===Executor Pool Info====");
		log.info("Executor Pool Name: " + getPoolName());
	//	log.info("Executor Core Pool Size: " + this.getCorePoolSize());
	//	log.info("Executor Current number of threads in the pool: "
	//			+ this.getPoolSize());
		log.info("Executor Settings: Maximum number of threads in the pool: "
				+ this.getMaximumPoolSize());
		log.info("Executor the largest number of threads that have ever simultaneously been in the pool: "
				+ this.getLargestPoolSize());
		log.info("Executor: Active Thread Count: " + this.getActiveCount());
	//	log.info("Executor: Active Running Task Count: " + this.getTaskCount());
	//	log.info("Executor: Pending Task Count: " + this.getQueue().size());
		log.info("Executor: Completed Task Count: "
				+ this.getCompletedTaskCount());
	//	log.info("Executor: Shutdown Status: " + this.isShutdown());
	//	log.info("Executor: Terminating Status: " + this.isTerminating());
	//	log.info("Executor: Terminated ?: " + this.isTerminated());

	}

	public int getActiveThreadCount(){
		return this.getActiveCount();
	}
	
	public long getRunningTaskCount(){
		return this.getTaskCount();
	}
	/**
	 * Performs auxiliary operations before execution of the task
	 */
	@Override
	protected void beforeExecute(Thread t, Runnable r) {

		super.beforeExecute(t, r);

		if (log.isInfoEnabled()) {

			log.info("Executor:" + getPoolName() + ": A task is beginning: "
					+ t.getName() + " " + r.hashCode());

			//getPoolLog();

		}

	}

	/**
	 * Performs auxiliary operations after execution of the task
	 */
	@Override
	protected void afterExecute(Runnable r, Throwable t) {

		super.afterExecute(r, t);

		if (log.isInfoEnabled()) {
			log.info("Executor:" + getPoolName() + ": A task is finishing: "
					+ r.hashCode());
		}

	}

	/**
	 * 
	 * Performs auxiliary operations on pool shutdown event
	 */
	@Override
	public void shutdown() {

		if (log.isInfoEnabled()) {

			try {
				// Wait a while for existing tasks to terminate
				if (!super.awaitTermination(60,
						TimeUnit.SECONDS)) {
					// Disable new tasks from being submitted
					super.shutdownNow();
					// Wait a while for tasks to respond to being cancelled
					if (!super.awaitTermination(
							60, TimeUnit.SECONDS))
						log.error(this.getPoolName() + " did not terminate");
				}

			} catch (InterruptedException e) {
				log.error("Stopping " + this.getPoolName()  + " waiting:"
						+ 60 + " "
						+ TimeUnit.MILLISECONDS);

				log.error(this.getPoolName() + " did not terminate");

				// Re-Cancel if current thread also interrupted
				super.shutdownNow();

				// Preserve interrupt status
				Thread.currentThread().interrupt();

			} finally {
				if (!super.isTerminated()) {
					log.info(this.getPoolName() +  " has not been shutdown!");
				} else {
					log.info(this.getPoolName() + " Service has been shutdown successfully");
				}
			}
		}

		getPoolLog();

	}

	/**
	 * 
	 * Performs auxiliary operations on pool shutdown immediate event
	 */
	@Override
	public List<Runnable> shutdownNow() {

		if (log.isInfoEnabled()) {

			log.info("Executor:" + getPoolName()
					+ " is shutting down immediately");

			getPoolLog();

		}
		return super.shutdownNow();
	}

	@Override
	public String toString() {

		return "Pool:" + this.getPoolName()
				+ "; Executor Current number of threads in the pool: "
				+ this.getPoolSize();
	}

}

