package telefonica.aaee.capture977r.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import telefonica.aaee.capture977r.process.Capture977rProcessor;

@Configuration
//Specifies which package to scan
@ComponentScan(value={
		"telefonica.aaee.capture977r"
		,"telefonica.aaee.dao"
		,"telefonica.aaee.util"})
public class Capture977rConfig {
	@Bean
	public Capture977rProcessor processor() {
		return new Capture977rProcessor();
	}

}
