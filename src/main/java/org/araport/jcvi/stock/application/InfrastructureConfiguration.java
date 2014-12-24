package org.araport.jcvi.stock.application;

import javax.sql.DataSource;

public interface InfrastructureConfiguration {

		public abstract DataSource dataSource();
}
