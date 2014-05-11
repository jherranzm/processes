package telefonica.aaee.segmentacion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import telefonica.aaee.segmentacion.process.SegmentacionProcessor;

//Marks this class as configuration
@Configuration
//Specifies which package to scan
@ComponentScan("telefonica.aaee.segmentacion")
//Enables Spring's annotations
//@EnableWebMvc
public class SegmentacionConfig {

	@Bean
	public SegmentacionProcessor processor() {
		return new SegmentacionProcessor();
	}
}
