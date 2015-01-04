package org.araport.stock.tasklet.business;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araport.stock.dao.GeneralDao;
import org.araport.stock.dao.impl.GeneralDaoImpl;
import org.araport.stock.tasklet.staging.StageDbXrefExistingStockAccessionsTasklet;
import org.araport.stock.utils.FileUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

public class DbXrefPrimaryStockAccessionsPostLoadTasklet implements Tasklet{
	
	private static final Log log = LogFactory
			.getLog(DbXrefPrimaryStockAccessionsPostLoadTasklet.class);

	private static final String ENABLE_DBXREF_CONSTRAINTS_SQL_PATH = "/sql/transformations/dbxref/enable_chado_dbxref_constraints.sql";
	private static final String ENABLE_DBXREF_CONSTRAINTS_SQL = FileUtils
			.getSqlFileContents(ENABLE_DBXREF_CONSTRAINTS_SQL_PATH);

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

		log.info("Injected: Enable Constrainsts on DbXref Table SQL:"
				+ENABLE_DBXREF_CONSTRAINTS_SQL);
		log.info("Enabling Constrainsts on DbXref Table ...");

		generalDao.executeSQL(ENABLE_DBXREF_CONSTRAINTS_SQL);
		
		return RepeatStatus.FINISHED;

	}

	@PostConstruct
	public void setDao() {

		this.generalDao = new GeneralDaoImpl();
		this.generalDao.setDataSource(targetDataSource);

	}

}
