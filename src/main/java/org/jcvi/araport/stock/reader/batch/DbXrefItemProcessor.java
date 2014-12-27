package org.jcvi.araport.stock.reader.batch;

import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.jcvi.araport.stock.dao.DbDao;
import org.jcvi.araport.stock.reader.domain.DbXref;
import org.jcvi.araport.stock.service.impl.DbDaoImpl;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({DataSourceInfrastructureConfiguration.class, DbDaoImpl.class})
public class DbXrefItemProcessor implements ItemProcessor<DbXref, DbXref>{
	
@Autowired
private DbDao dbDao;

	@Override
	public DbXref process(final DbXref dbxref) throws Exception {
	
		return dbxref;
	}

	public void setAccountDao(DbDao dbDao) {
        this.dbDao = dbDao;
    }
	
}
