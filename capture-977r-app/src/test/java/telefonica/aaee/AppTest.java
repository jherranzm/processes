package telefonica.aaee;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import telefonica.aaee.capture977.Split977;
import telefonica.aaee.capture977.Split977Config;

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
		boolean bDetalleLlamadas = false;
		boolean bDetalleLlamadasRI = false;
		String acuerdo = "TEST";
		String path = "/Users/jherranzm/dev/977r_files/SCO/2013/";
    	try {
			Split977 sp = new Split977();
			
			Split977Config config = new Split977Config.Builder()
				.acuerdo(acuerdo)
				.detalleLlamadas(bDetalleLlamadas)
				.detalleLlamadasRI(bDetalleLlamadasRI)
				.directorioZipFiles(path)
				.directorioOut(path)
				.build();
			
			sp.setConfig(config);
			
			if(sp.getFicheros().length > 0 ){
				sp.execute();
				System.out.println("Se ha tardado:" + sp.getTiempoEmpleado()/1000 + " segundos!");
			}else{
				System.err.println("Sin ficheros a tratar...");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    	
    	
    }
}
