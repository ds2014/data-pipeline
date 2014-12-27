package org.jcvi.araport.stock.reader.batch;

import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.jcvi.araport.stock.service.impl.DbDaoImpl;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Component("driving_query_reader")
@Import({DataSourceInfrastructureConfiguration.class, DbDaoImpl.class})

public class SourceStockDrivingQueryReader {

}
