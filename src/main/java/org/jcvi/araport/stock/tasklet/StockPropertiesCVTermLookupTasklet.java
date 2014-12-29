package org.jcvi.araport.stock.tasklet;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.jcvi.stock.utils.FileUtils;
import org.jcvi.araport.stock.dao.DbDao;
import org.jcvi.araport.stock.dao.DbXRefDao;
import org.jcvi.araport.stock.dao.OrganismDao;
import org.jcvi.araport.stock.dao.StockDao;
import org.jcvi.araport.stock.dao.impl.DbDaoImpl;
import org.jcvi.araport.stock.dao.impl.DbXrefDaoImpl;
import org.jcvi.araport.stock.dao.impl.OrganismDaoImpl;
import org.jcvi.araport.stock.dao.impl.StockDaoImpl;
import org.omg.CORBA.portable.InputStream;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
@StepScope
@Import({DataSourceInfrastructureConfiguration.class, DbDaoImpl.class, DbXrefDaoImpl.class, StockDaoImpl.class})
@PropertySources(value = {@PropertySource("classpath:/sql/bootstrap/db_init_cvterm_stockproperties.sql")})
public class StockPropertiesCVTermLookupTasklet implements Tasklet {

	private static final Log log = LogFactory.getLog(StockPropertiesCVTermLookupTasklet.class);

	@Autowired
	Environment environment;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	DataSource targetDataSource;
	
	@Autowired
	private DbDao dbDao;
	
	private DbXRefDao dbXrefDao;
	
	private StockDao stockDao;
			
	@Override
	public RepeatStatus execute(StepContribution step, ChunkContext context)
			throws Exception {
		String sql = environment
		.getProperty("db.cvterm.sql");
		
		sql = FileUtils.getSqlFileContents("/sql/bootstrap/db_init_cvterm_stockproperties.sql");

		log.info("Injected SQL:" + sql);
		
		dbDao.executeSQL(sql);
		return RepeatStatus.FINISHED;
	}
	
	
	@PostConstruct
	public void setDao(){
		this.dbXrefDao = new DbXrefDaoImpl();
		this.dbXrefDao.setDataSource(targetDataSource);
		this.stockDao = new StockDaoImpl();
		this.stockDao.setDataSource(targetDataSource);
	}
		
}
