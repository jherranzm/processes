package telefonica.aaee.excel;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import telefonica.aaee.excel.config.GeneraExcelConfig;
import telefonica.aaee.excel.export.GeneraREGFicheroExcel;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
		System.out.println("Genera Excel App!");
		long ini = System.currentTimeMillis();

		ApplicationContext ctx = new AnnotationConfigApplicationContext(
				GeneraExcelConfig.class);

		GeneraREGFicheroExcel processor2 = (GeneraREGFicheroExcel) ctx
				.getBean("processor2");

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
        assertTrue( true );
    }
}
