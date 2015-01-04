package org.araport.stock.partitioner;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.jcvi.araport.stock.dao.impl.GeneralDaoImpl;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.StringUtils;

@Import({ DataSourceInfrastructureConfiguration.class })
@PropertySources(value = { @PropertySource("classpath:/partition.properties") })
public class StockColumnRangePartitioner implements Partitioner {

	@Autowired
	Environment environment;

	@Autowired
	private ResourceLoader resourceLoader;

	private static final Logger log = Logger
			.getLogger(StockColumnRangePartitioner.class);

	private static final int PARTITIONS_PER_NODE = 1;

	@Autowired
	DataSource targetDataSource;

	private JdbcTemplate jdbcTemplate;
	private String table;
	private String column;

	private String whereClause;

	/**
	 * The name of the SQL table the data are in.
	 *
	 * @param table
	 *            the name of the table
	 */
	public void setTable(String table) {
		this.table = table;
	}

	/**
	 * The name of the column to partition.
	 *
	 * @param column
	 *            the column name.
	 */
	public void setColumn(String column) {
		this.column = column;
	}

	/**
	 * An optional where clause
	 *
	 * @param whereClause
	 */
	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}

	/**
	 * The data source for connecting to the database.
	 *
	 * @param dataSource
	 *            a {@link DataSource}
	 */
	public void setDataSource(DataSource dataSource) {
			jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * Partition a database table assuming that the data in the column specified
	 * are uniformly distributed. The execution context values will have keys
	 * <code>minValue</code> and <code>maxValue</code> specifying the range of
	 * values to consider in each partition.
	 *
	 * @see Partitioner#partition(int)
	 */
	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {

		int partitionCount = gridSize;
		String minQuery = "SELECT MIN(" + column + ") from " + table;
		String maxQuery = "SELECT MAX(" + column + ") from " + table;
		String countQuery = "SELECT count(*) from " + table;

		if (StringUtils.hasLength(whereClause)) {
			minQuery = minQuery + " WHERE " + whereClause;
			maxQuery = maxQuery + " WHERE " + whereClause;
			countQuery = countQuery + " WHERE " + whereClause;
		}

		long min = jdbcTemplate.queryForLong(minQuery);
		long max = jdbcTemplate.queryForLong(maxQuery);
		long count = jdbcTemplate.queryForLong(countQuery);
		long targetSize = (max - min) / partitionCount + 1;
		
		log.info("***********************************************************");
		log.info("count = " + count + " min = " + min + " max = " + max
				+ " targetSize = " + targetSize);
		log.debug("***********************************************************");
		
		Map<String, ExecutionContext> result = new HashMap<String, ExecutionContext>();
		
		int number = 0;
		long start = min;
		long end = start + targetSize - 1;
		
		while (start <= max) {
			ExecutionContext value = new ExecutionContext();
			result.put("partition" + number, value);
			if (end >= max) {
				end = max;
			}
			value.putLong("minValue", start);
			value.putLong("maxValue", end);
			start += targetSize;
			end += targetSize;
			number++;
		}
		
		log.info("***********************************************************");
		log.info("Total Partition Count = " + result.size());
		log.info("***********************************************************");
		
		return result;

	}

	@PostConstruct
	public void setDataSource() {
		
		this.jdbcTemplate = new JdbcTemplate(targetDataSource);

	}

}
