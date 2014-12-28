package org.jcvi.araport.stock.reader.batch;

import org.apache.log4j.Logger;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.jcvi.araport.stock.dao.DbDao;
import org.jcvi.araport.stock.reader.domain.Db;
import org.jcvi.araport.stock.reader.domain.DbXref;
import org.jcvi.araport.stock.reader.domain.SourceStockDrivingQuery;
import org.jcvi.araport.stock.service.impl.DbDaoImpl;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({ DataSourceInfrastructureConfiguration.class, DbDaoImpl.class })
public class DbXrefItemProcessor1 implements ItemProcessor<SourceStockDrivingQuery, DbXref> {

	@Autowired
	private DbDao dbDao;

	private static final Logger log = Logger
			.getLogger(DbXrefItemProcessor1.class);

	@Override
	public DbXref process(SourceStockDrivingQuery sourceRecord) throws Exception {

		return createDbXRef(sourceRecord.getStockId());
	}

	public DbXref createDbXRef(Long sourceStockId) {

		Db tairStockDb = dbDao.findDbByName("TAIR Stock");

		if (tairStockDb != null) {
			log.info("TAIR Stock Db" + tairStockDb);
		} else {
			log.info("TAIR Stock not found!");
		}

		String accession = String.valueOf(sourceStockId);
		DbXref dbXref = new DbXref();

		dbXref.setDbId(tairStockDb.getDbId());
		dbXref.setPrimaryAccession(accession);
		dbXref.setDescription("TAIR Stock Primary Accession");
		dbXref.setVersion("");

		return dbXref;

	}

}
