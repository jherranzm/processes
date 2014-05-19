package telefonica.aaee.excel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import telefonica.aaee.excel.export.GeneraREGFicheroExcel;

@Configuration
//Specifies which package to scan
@ComponentScan(value={"telefonica.aaee.excel","telefonica.aaee.dao"})
public class GeneraExcelConfig {
	@Bean
	public GeneraREGFicheroExcel processor() {
		return new GeneraREGFicheroExcel();
	}

}
