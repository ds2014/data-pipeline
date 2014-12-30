package org.araport.jcvi.stock.application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@SpringBootApplication
public class ApplicationLauncher {

	private static final String LOAD_STOCKS = "loadStocks";

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job loadStocks;

	public static void main(String[] args)
			throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException,
			InterruptedException {

		Log log = LogFactory.getLog(ApplicationLauncher.class);

		//SpringApplication app = new SpringApplication(LoadStocksJobBatchConfiguration.class);
		SpringApplication app = new SpringApplication(ApplicationLauncher.class);
		app.setWebEnvironment(false);
		ConfigurableApplicationContext ctx = app.run(args);
		JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);

		Job stockReader = ctx.getBean(LOAD_STOCKS, Job.class);

		JobParameters jobParameters = new JobParametersBuilder().addLong("time",System.currentTimeMillis()).toJobParameters();

		JobExecution jobExecution = jobLauncher.run(stockReader, jobParameters);

		BatchStatus batchStatus = jobExecution.getStatus();

		System.out.println("Stock Reader Application has Started.");

		while (batchStatus.isRunning()) {
			log.info("*********** Still running.... **************");
			Thread.sleep(1000);
		}

		log.info(String.format("*********** Exit status: %s", jobExecution
				.getExitStatus().getExitCode()));
		JobInstance jobInstance = jobExecution.getJobInstance();
		log.info(String.format("********* Name of the job %s",
				jobInstance.getJobName()));

		log.info(String.format("*********** job instance Id: %d",
				jobInstance.getId()));

		System.exit(0);

	}

}
