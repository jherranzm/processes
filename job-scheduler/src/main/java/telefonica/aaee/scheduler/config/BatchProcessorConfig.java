package telefonica.aaee.scheduler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import telefonica.aaee.scheduler.BatchProcessor;

@Configuration
@ComponentScan(value={
		"telefonica.aaee.scheduler.config"
		,"telefonica.aaee.util"})
@Import(FirstJobConfiguration.class)
public class BatchProcessorConfig {
	@Bean
	public BatchProcessor batchProcessor() {
		return new BatchProcessor();
	}


}
