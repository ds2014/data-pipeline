package org.jcvi.araport.stock.processor;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
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
import org.jcvi.araport.stock.domain.Organism;
import org.jcvi.araport.stock.domain.SourceStockDrivingQuery;
import org.jcvi.araport.stock.domain.StockDbXref;
import org.jcvi.araport.stock.writer.DbXrefItemWriter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.jcvi.araport.stock.domain.Stock;


@Import({ DataSourceInfrastructureConfiguration.class, DbDaoImpl.class, DbXrefDaoImpl.class, OrganismDaoImpl.class, StockDaoImpl.class })
public class StockItemProcessor implements ItemProcessor <SourceStockDrivingQuery, Stock>{

	@Autowired
	DataSource targetDataSource;
	
	@Autowired
	private DbDao dbDao;
	
	private DbXRefDao dbXrefDao;
	
	private OrganismDao organismDao;
	
	private StockDao stockDao;

	private static final Logger log = Logger
			.getLogger(StockItemProcessor.class);
	
			
	@Override
	public Stock process(SourceStockDrivingQuery sourceRecord) throws Exception {
		
		return createStock(sourceRecord.getStockId());
		
	}
	
	public Stock createStock(Long sourceStockId){
		
		Db tairStockDb = dbDao.findDbByName("TAIR Stock");
		int dbId = tairStockDb.getDbId();
		
		String accession = String.valueOf(sourceStockId);
		
		log.info("Source Stock Id = " + sourceStockId);
		log.info("DBXRef accession = " + accession);
		
		DbXref dbXref = dbXrefDao.findDbXrefByAccessionAndDb(dbId, accession);
			
		if (dbXref == null) { // create Primary DbXRef accession
			log.info("DBXref is null. Creating Primary DbXref Accession = " + accession);
			
			
			dbXref = new DbXref();
			dbXref.setDbId(tairStockDb.getDbId());
			dbXref.setPrimaryAccession(accession);
			dbXref.setDescription("TAIR Stock Primary Accession");
			dbXref.setVersion("");
			
			dbXrefDao.merge(dbXref);
		} else {
			log.info("Primary Acession DBXref exists! " + dbXref);
		}
				
		Organism organism = organismDao.findByName("mouse-ear cress");
		
		if (organism == null) { // create organism
			log.info("Organism is null. Creating Organism = " + "mouse-ear cress");
			//dbXrefDao.merge(dbXref);
		} else {
			log.info("Organism exists! " + organism);
		}
		
		
		Stock stock = new Stock();
		
		stock.setStockId(2);
		stock.setDbxrefId(163629);
		stock.setStockTypeId(44354);
		stock.setName("CD3-179");
		stock.setOrganismId(6);
						
		Stock sourceStock = stockDao.lookupSourceStockById(sourceStockId);
		sourceStock.setDbxrefId(dbXref.getDbXrefId());
		sourceStock.setOrganismId(organism.getOrganismId());
		
		
		int  tair_db_id = dbDao.getTairDBId();
		String secondaryAccession = String.valueOf(sourceStock.getTairObjectId());
		
		log.info("Looking up secondary accession in DBXRef" + secondaryAccession);
		
		DbXref secondarydbXref = dbXrefDao.findDbXrefByAccessionAndDb(tair_db_id, secondaryAccession);
					
		secondarydbXref = new DbXref();
		secondarydbXref.setDbId(tair_db_id);
		secondarydbXref.setPrimaryAccession(secondaryAccession);
		secondarydbXref.setDescription("TAIR Stock Secondary Accession");
		secondarydbXref.setVersion("");
		
		secondarydbXref = dbXrefDao.mergeAndReturn(secondarydbXref);
		log.info("Secondary Accession Primary key:" + secondarydbXref.getDbXrefId());
		
		StockDbXref stockRef = new StockDbXref();
		stockRef.setDbXrefId(secondarydbXref.getDbXrefId());
		stockRef.setStockId(sourceStock.getStockId());
		
		sourceStock.setStockRef(stockRef);
		
		return sourceStock;
		
	}

	
	@PostConstruct
	public void setDao(){
		this.dbXrefDao = new DbXrefDaoImpl();
		this.dbXrefDao.setDataSource(targetDataSource);
		this.organismDao = new OrganismDaoImpl();
		this.organismDao.setDataSource(targetDataSource);
		this.stockDao = new StockDaoImpl();
		this.stockDao.setDataSource(targetDataSource);
	}
	
		
	}
