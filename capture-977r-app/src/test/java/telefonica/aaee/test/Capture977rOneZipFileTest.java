package telefonica.aaee.test;

import static org.junit.Assert.*;

import org.junit.Test;

import telefonica.aaee.capture977.Split977;
import telefonica.aaee.capture977.Split977Config;

public class Capture977rOneZipFileTest {

	@Test
	public void testExecute() {
		try {
			Split977 sp = new Split977();
			
			Split977Config config = new Split977Config.Builder()
			.acuerdo("TEST2")
			.detalleLlamadas(false)
			.detalleLlamadasRI(false)
			.directorioZipFiles("/Users/jherranzm/dev/2014-05-04/")
			.directorioOut("/Users/jherranzm/dev/2014-05-04/")
			.build();
		
		sp.setConfig(config);
			

			assertTrue(sp.getFicheros().length == 1);
		
			if(sp.getFicheros().length > 0 ){
				sp.execute();
				System.out.println("Se ha tardado:" + sp.getTiempoEmpleado()/1000 + " segundos!");
			}else{
				System.err.println("Sin ficheros a tratar...");
			}
		}catch(Exception e){
			
		}
	}

}
