package telefonica.aaee.segmentacion.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import telefonica.aaee.segmentacion.config.SegmentacionConfig;
import telefonica.aaee.segmentacion.process.SegmentacionProcessor;

public class MainApp {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {

		System.out.println("App!");	  
		long ini = System.currentTimeMillis();
		
		ApplicationContext ctx = 
			      new AnnotationConfigApplicationContext(SegmentacionConfig.class);
		
		SegmentacionProcessor processor = (SegmentacionProcessor) ctx.getBean("processor");
		
		//processor.setDir("/Users/jherranzm/dev/testFiles/");
		processor.setDir("/var/tmp/Segmentacion/Semana_23/");
		processor.execute();
		
		
		long fin = System.currentTimeMillis();
		
		System.out.println("Tiempo invertido:" + ((fin-ini)/1000) + " seg.");

	}


}
