package org.jcvi.araport.stock.reader.batch;

import org.apache.log4j.Logger;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.jcvi.araport.stock.dao.DbDao;
import org.jcvi.araport.stock.listeners.LogProcessListener;
import org.jcvi.araport.stock.reader.domain.Db;
import org.jcvi.araport.stock.reader.domain.DbXref;
import org.jcvi.araport.stock.service.impl.DbDaoImpl;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({ DataSourceInfrastructureConfiguration.class, DbDaoImpl.class })
public class DbXrefItemProcessor implements ItemProcessor<DbXref, DbXref> {

	@Autowired
	private DbDao dbDao;

	private static final Logger log = Logger
			.getLogger(DbXrefItemProcessor.class);

	@Override
	public DbXref process(final DbXref dbxref) throws Exception {

		Db tairStockDb = dbDao.findDbByName("TAIR Stock");

		if (tairStockDb != null) {
			log.info("TAIR Stock Db" + tairStockDb);
		} else {
			log.info("TAIR Stock not found!");
		}

		return dbxref;
	}

	public void setDbDao(DbDao dbDao) {
		this.dbDao = dbDao;
	}

}
