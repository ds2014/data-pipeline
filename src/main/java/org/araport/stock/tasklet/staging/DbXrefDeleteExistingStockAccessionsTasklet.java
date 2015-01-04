package org.araport.stock.tasklet.staging;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araport.stock.dao.GeneralDao;
import org.araport.stock.dao.impl.GeneralDaoImpl;
import org.araport.stock.utils.FileUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

public class DbXrefDeleteExistingStockAccessionsTasklet implements Tasklet{

	private static final Log log = LogFactory
			.getLog(DbXrefDeleteExistingStockAccessionsTasklet.class);

	private static final String BATCH_SCHEMA_INIT_SQL_PATH = "/sql/schema/batch/init_batch_schema.sql";
	private static final String BATCH_SCHEMA_INIT_SQL = FileUtils
			.getSqlFileContents(BATCH_SCHEMA_INIT_SQL_PATH);

	@Autowired
	Environment environment;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	DataSource targetDataSource;

	private GeneralDao generalDao;

	@Override
	public RepeatStatus execute(StepContribution step, ChunkContext context)
			throws Exception {

		log.info("Injected Init Batch Schema SQL:" + BATCH_SCHEMA_INIT_SQL);
		log.info("Initializing Batch Schema...");

		generalDao.executeSQL(BATCH_SCHEMA_INIT_SQL);

		return RepeatStatus.FINISHED;

	}

	@PostConstruct
	public void setDao() {

		this.generalDao = new GeneralDaoImpl();
		this.generalDao.setDataSource(targetDataSource);

	}


}
