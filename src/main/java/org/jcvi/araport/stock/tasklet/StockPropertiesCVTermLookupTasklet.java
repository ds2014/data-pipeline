package org.jcvi.araport.stock.tasklet;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.jcvi.stock.common.ApplicationContstants;
import org.araport.jcvi.stock.common.MetadataExecutionContext;
import org.araport.jcvi.stock.exception.ExceptionLogger;
import org.araport.jcvi.stock.exception.StockLoaderException;
import org.araport.jcvi.stock.utils.FileUtils;
import org.jcvi.araport.stock.dao.DbDao;
import org.jcvi.araport.stock.dao.DbXRefDao;
import org.jcvi.araport.stock.dao.OrganismDao;
import org.jcvi.araport.stock.dao.StockDao;
import org.jcvi.araport.stock.dao.impl.DbDaoImpl;
import org.jcvi.araport.stock.dao.impl.DbXrefDaoImpl;
import org.jcvi.araport.stock.dao.impl.OrganismDaoImpl;
import org.jcvi.araport.stock.dao.impl.StockDaoImpl;
import org.jcvi.araport.stock.domain.Db;
import org.jcvi.araport.stock.domain.DbXref;
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
		
			
		String sql = FileUtils.getSqlFileContents("/sql/bootstrap/db_init_cvterm_stockproperties.sql");
			log.info("Injected SQL:" + sql);
	    dbDao.executeSQL(sql);
		
		Db stockTermDb = dbDao.findDbByName("stock term");
		
		if (stockTermDb == null){
			throw new StockLoaderException("Database with name stock_term not ! Cannot load stock properties cvterms data.");
		}
		
		log.info("Stock Term Database:" + stockTermDb);
		
		
		populateLookups(stockTermDb.getDbId(), ApplicationContstants.CV_STOCK_PROPERTY_NAME);
		
		return RepeatStatus.FINISHED;
	}
	
	
	@PostConstruct
	public void setDao(){
		this.dbXrefDao = new DbXrefDaoImpl();
		this.dbXrefDao.setDataSource(targetDataSource);
		this.stockDao = new StockDaoImpl();
		this.stockDao.setDataSource(targetDataSource);
	}
	
	
	private void populateLookups(int dbId, String name) {
		
		populateCVLookup(name);
		populateDBCVTermStockProperties(dbId);
   
	}
	
	private void populateCVLookup(String name){
		
	}
	
	private void populateDBCVTermStockProperties(int dbId){
		
		List<String> cvTerms = MetadataExecutionContext.getInstance().getAllCvTermStockProperties();
		
		for (String item: cvTerms){
			log.info("Stock CV Term: " + item);
			
			log.info("Loading Term to database");
			
			DbXref dbXref = new DbXref();
			dbXref.setDbId(dbId);
			dbXref.setPrimaryAccession(item);
			dbXref.setVersion("");
			dbXref.setDescription("");
			
			dbXref = dbXrefDao.mergeAndReturn(dbXref);
			
			log.info("Loaded DbXref: " + dbXref);
			
			
		}
		
	}
		
}
