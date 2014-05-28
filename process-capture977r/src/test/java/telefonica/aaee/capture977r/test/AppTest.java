package telefonica.aaee.capture977r.test;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import telefonica.aaee.capture977r.config.Capture977rConfig;
import telefonica.aaee.capture977r.process.Capture977rProcessor;
import telefonica.aaee.capture977r.process.Split977Config;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	public void testCapturaFicheros977r() {
		System.out.println("CapturaFicheros977r!");
		long ini = System.currentTimeMillis();

		@SuppressWarnings("resource")
		ApplicationContext ctx = new AnnotationConfigApplicationContext(
				Capture977rConfig.class);

		Capture977rProcessor processor2 = (Capture977rProcessor) ctx
				.getBean("processor");

		Split977Config config = new Split977Config.Builder()
				.acuerdo("RACC_2013").detalleLlamadas(false)
				.detalleLlamadasRI(false)
				.directorioZipFiles("/Users/jherranzm/dev/testFiles/RACC/")
				.directorioOut("/Users/jherranzm/dev/testFiles/RACC/")
				.borrarAcuerdo(true)
				.recargaFicheros(true)
				.build();

		processor2.setConfig(config);

		assertTrue(processor2.getFicheros().length == 6);

		if (processor2.getFicheros().length > 0) {
			processor2.execute();
			System.out.println("Se ha tardado:" + processor2.getTiempoEmpleado() / 1000
					+ " segundos!");
		} else {
			System.err.println("Sin ficheros a tratar...");
		}

		long fin = System.currentTimeMillis();

		System.out
				.println("Tiempo invertido:" + ((fin - ini) / 1000) + " seg.");
		System.out
				.println("Tiempo invertido:" + (((fin - ini) / 1000)/60) + " min.");
		assertTrue(true);
	}
}
