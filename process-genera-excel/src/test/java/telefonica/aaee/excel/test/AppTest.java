package telefonica.aaee.excel.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import telefonica.aaee.excel.config.GeneraExcelConfig;
import telefonica.aaee.excel.export.GeneraREGFicheroExcel;

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

		@SuppressWarnings("resource")
		ApplicationContext ctx = new AnnotationConfigApplicationContext(
				GeneraExcelConfig.class);

		GeneraREGFicheroExcel processor2 = (GeneraREGFicheroExcel) ctx
				.getBean("generaInformeExcel");

		String informe = "acuerdoAplicado";
		String acuerdo = "LHOSP_2013_1T";
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
    
    
	public void testGetRango() {
		int _f = 33;
		int _c = 26;
		String res = getRango(_f, _c);
		assertTrue(res.equals("Z2:Z33"));
	}

	public void testGetRangoA2A33() {
		int _f = 33;
		int _c = 1;
		String res = getRango(_f, _c);
		assertTrue(res.equals("A2:A33"));
	}

	private String getRango(int _f, int _c) {
		System.out.println(_f);
		final String nomColumnes = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		System.out.println(nomColumnes.length());
		final StringBuffer nomColumna = new StringBuffer();
		long maxFila = _f;
		int indexCol = _c;
		if(indexCol > nomColumnes.length()){
			int pos1 = indexCol % nomColumnes.length();
			System.out.println(pos1);
			int fl = (int) (indexCol / nomColumnes.length());
			System.out.println(fl);
			if (fl > 0) nomColumna.append(nomColumnes.charAt(fl));
			System.out.println(nomColumna);
			if (pos1 > 0) nomColumna.append(nomColumnes.charAt(pos1 - 1));
			System.out.println(nomColumna);
		}else if (indexCol > 0) {
			nomColumna.append(nomColumnes.charAt(_c -1));
			System.out.println(nomColumna);
		} else {
			nomColumna.append("A");
		}
	
	
		String res = "" + nomColumna + "2:"  +  nomColumna  + "" +  maxFila;
		System.out.println(res);
		return res;
	}

}
