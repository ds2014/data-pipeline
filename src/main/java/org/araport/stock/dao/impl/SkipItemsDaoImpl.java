package org.araport.stock.dao.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.stock.dao.BaseDao;
import org.araport.stock.dao.SkipItemsDao;
import org.araport.stock.policy.SkipItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component("skip_dao")
@Import({ DataSourceInfrastructureConfiguration.class })
public class SkipItemsDaoImpl extends BaseDao implements SkipItemsDao {

	@Autowired
	DataSource targetDataSource;

	private NamedParameterJdbcOperations namedParameterJdbcTemplate;

	private String insertStatement = "INSERT INTO SKIP_ITEMS (type, item, msg,runId,jobExecutionId,stepExecutionId ) VALUES(:type,:item,:msg,:runId,:jobExecutionId,:stepExecutionId)";

	public void save(SkipItems skipElement){
    	Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", skipElement.getType());
		params.put("item", skipElement.getItem());
		
		params.put("msg", skipElement.getMsg());
		params.put("runId", skipElement.getRunId());
		params.put("jobExecutionId", skipElement.getJobExecutionId());
		params.put("stepExecutionId", skipElement.getStepExecutionId());
		
	
		
    	namedParameterJdbcTemplate.update(insertStatement, params);
    }

	@Override
	public void setDataSource(DataSource datasource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				datasource);
	}

	@PostConstruct
	public void setDataSource() {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				targetDataSource);
	}

}
