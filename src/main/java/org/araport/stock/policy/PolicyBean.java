package org.araport.stock.policy;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.context.annotation.Bean;

public class PolicyBean {

@Bean 
ExceptionSkipPolicy exceptionSkipPolicy(){
	return new ExceptionSkipPolicy();
}

@Bean
StockSkipListener stockSkipListener(){
	return new StockSkipListener();
}
}
