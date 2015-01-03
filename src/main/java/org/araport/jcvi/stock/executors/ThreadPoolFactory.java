package org.araport.jcvi.stock.executors;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;


public class ThreadPoolFactory implements ThreadFactory {

	  private final String poolName;
	  private final List<String> stats;
	  private static final AtomicInteger poolNumber = new AtomicInteger(1);
	  private final ThreadGroup group;
	  private final AtomicInteger threadNumber = new AtomicInteger(1);
	  private final String namePrefix;

	  
		private static final Logger log = Logger
				.getLogger(ThreadPoolFactory.class);

	  public ThreadPoolFactory(String name) {

	    this.poolName = name;
	    stats = new ArrayList<String>();

	    SecurityManager s = System.getSecurityManager();
	    group = (s != null) ? s.getThreadGroup() : Thread.currentThread()
	        .getThreadGroup();
	    namePrefix = "pool-" + poolNumber.getAndIncrement() + " " + poolName
	        + "-Thread-";

	  }

	  /**
	   * Starts pool
	   */
	  @Override
	  public Thread newThread(Runnable r) {

	    Thread t = new Thread(group, r,
	        namePrefix + threadNumber.getAndIncrement(), 0);

	    stats.add(String.format("Created thread %d with name %s on %s\n",
	        t.getId(), t.getName(), new Date()));

	    if (log.isInfoEnabled()) {
	      log.info(getStats());
	    }

	    return t;

	  }

	  /**
	   * Generates pool creation statistics
	   */
	  public String getStats() {

		String result = null;
		
	    StringBuilder buffer = new StringBuilder();
	    Iterator<String> it = stats.iterator();

	    while (it.hasNext()) {
	      buffer.append(it.next());
	      buffer.append("\n");
	    }
	    
	    if (buffer.length() > 0){
	    	return buffer.toString();
	    }else{
	    	return null;
	    }
	  }

	  /**
	   * 
	   * Generates pool name
	   */
	  public String getThreadFactoryInfo() {
	    return this.namePrefix + " " + this.threadNumber;
	  }

	  @Override
	  public String toString() {
	    return getThreadFactoryInfo();
	  }
}
