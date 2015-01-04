package org.araport.stock.writer;

import java.net.MalformedURLException;

import org.araport.stock.domain.StockProperty;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

public class StockPropertyFlatItemWriter  {
	
	@Bean
    public ItemWriter<StockProperty> writer() {
    	FlatFileItemWriter<StockProperty> writer = new FlatFileItemWriter<StockProperty>();
    	
    	
    	Resource resource;
		try {
			resource = new UrlResource("sys::${user.home}/text.txt");
		
    	writer.setResource(resource);
    	DelimitedLineAggregator<StockProperty> delLineAgg = new DelimitedLineAggregator<StockProperty>();
    	delLineAgg.setDelimiter(",");
    	BeanWrapperFieldExtractor<StockProperty> fieldExtractor = new BeanWrapperFieldExtractor<StockProperty>();
    	fieldExtractor.setNames(new String[] {"stockId", "typeId", "value, rank"});
    	delLineAgg.setFieldExtractor(fieldExtractor);
    	writer.setLineAggregator(delLineAgg);
    	
		}catch (MalformedURLException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
        return writer;
    }

}
