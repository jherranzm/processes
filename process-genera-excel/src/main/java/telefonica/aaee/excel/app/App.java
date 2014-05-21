package telefonica.aaee.excel.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import telefonica.aaee.excel.config.GeneraExcelConfig;
import telefonica.aaee.excel.export.GeneraREGFicheroExcel;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Genera Excel App!");
		long ini = System.currentTimeMillis();

		@SuppressWarnings("resource")
		ApplicationContext ctx = new AnnotationConfigApplicationContext(
				GeneraExcelConfig.class);

		GeneraREGFicheroExcel processor2 = (GeneraREGFicheroExcel) ctx
				.getBean("generaInformeExcel");

		String informe = "acuerdoAplicado";
		String acuerdo = "LHOSP_2012_1T";
		String ruta = "/Users/jherranzm/dev/";
		
		processor2.setAcuerdo(acuerdo);
		processor2.setInforme(informe);
		processor2.setRuta(ruta);
		
		processor2.execute();

		long fin = System.currentTimeMillis();

		System.out
				.println("Tiempo invertido:" + ((fin - ini) / 1000) + " seg.");
	}
}
