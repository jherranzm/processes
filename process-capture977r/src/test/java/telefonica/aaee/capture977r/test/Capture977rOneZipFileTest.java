package telefonica.aaee.capture977r.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import telefonica.aaee.capture977r.process.Capture977rProcessor;
import telefonica.aaee.capture977r.process.Split977Config;

public class Capture977rOneZipFileTest {

	@Test
	public void testExecute() {
		try {
			Capture977rProcessor sp = new Capture977rProcessor();
			
			Split977Config config = new Split977Config.Builder()
			.acuerdo("RACC_2013")
			.detalleLlamadas(false)
			.detalleLlamadasRI(false)
			.directorioZipFiles("/Users/jherranzm/dev/testFiles/RACC/")
			.directorioOut("/Users/jherranzm/dev/testFiles/RACC/")
			.borrarAcuerdo(true)
			.build();
		
		sp.setConfig(config);
			

			assertTrue(sp.getFicheros().length == 6);
		
			if(sp.getFicheros().length > 0 ){
				sp.execute();
				System.out.println("Se ha tardado:" + sp.getTiempoEmpleado()/1000 + " segundos!");
			}else{
				System.err.println("Sin ficheros a tratar...");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
