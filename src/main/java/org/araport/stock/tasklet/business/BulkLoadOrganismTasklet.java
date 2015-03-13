package org.araport.stock.tasklet.business;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.stock.dao.GeneralDao;
import org.araport.stock.dao.impl.GeneralDaoImpl;
import org.araport.stock.utils.FileUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;


@Component
@StepScope
@Import({ DataSourceInfrastructureConfiguration.class, GeneralDaoImpl.class })
public class BulkLoadOrganismTasklet implements Tasklet {

	private static final Log log = LogFactory
			.getLog(BulkLoadStockPropertiesTasklet.class);

	private static final String LOAD_ORGANISM_SQL_PATH = "/sql/transformations/organism/load_match_organism.sql";
	private static final String LOAD_ORGANISM_SQL = FileUtils
			.getSqlFileContents(LOAD_ORGANISM_SQL_PATH);
	
	
	private static final String LOAD_ECOTYPE_ACCESSIONS_SQL_PATH = "/sql/transformations/organism/load_ecotype_accessions.sql";
	private static final String LOAD_ECOTYPE_ACCESSIONSM_SQL = FileUtils
			.getSqlFileContents(LOAD_ECOTYPE_ACCESSIONS_SQL_PATH);

	@Autowired
	Environment environment;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	DataSource targetDataSource;

	private GeneralDao generalDao;
	
	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1)
			throws Exception {
		
	
		// 1. Load  Ecotypes Table
				log.info("Injected Load Ecotypes to Organism Table SQL:"
						+ LOAD_ORGANISM_SQL);
				log.info("Loading Ecotypes to Organism Table...");

				generalDao.executeSQL(LOAD_ORGANISM_SQL);
				
		// 2. Load Ecotypes DbxRef Accessions
				log.info("Injected Ecotypes DbxRef Accessions SQL:"
						+ LOAD_ECOTYPE_ACCESSIONSM_SQL);
				log.info("Loading Ecotypes DbxRef Accessions...");

				generalDao.executeSQL(LOAD_ECOTYPE_ACCESSIONSM_SQL);
				
				
				return RepeatStatus.FINISHED;
	   
				
				
	}

}
