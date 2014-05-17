package telefonica.aaee.capture977r.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import telefonica.aaee.capture977r.config.Capture977rConfig;
import telefonica.aaee.capture977r.process.Capture977rProcessor;
import telefonica.aaee.capture977r.process.Split977Config;

/**
 * Hello world!
 * 
 */
public class Procesa977RApp {
	public static void main(String[] args) {
		System.out.println("Procesa 977R App!");
		long ini = System.currentTimeMillis();

		@SuppressWarnings("resource")
		ApplicationContext ctx = new AnnotationConfigApplicationContext(
				Capture977rConfig.class);

		Capture977rProcessor processor = (Capture977rProcessor) ctx
				.getBean("processor");

		Split977Config config = new Split977Config.Builder()
		.acuerdo("TEST2")
				.detalleLlamadas(false)
				.detalleLlamadasRI(false)
				.directorioZipFiles("/Users/jherranzm/dev/2014-05-04/")
				.directorioOut("/Users/jherranzm/dev/2014-05-04/")
				.build();

		processor.setConfig(config);
		processor.execute();

		long fin = System.currentTimeMillis();

		System.out
				.println("Tiempo invertido:" + ((fin - ini) / 1000) + " seg.");
	}
}
