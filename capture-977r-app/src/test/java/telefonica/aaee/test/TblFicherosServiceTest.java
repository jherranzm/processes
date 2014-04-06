package telefonica.aaee.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import telefonica.aaee.model.datos.TblFicheros;
import telefonica.aaee.services.TblFicherosService;

public class TblFicherosServiceTest {

	@Test
	public void testGetAllItems() {
		TblFicherosService acService = new TblFicherosService();
		List<TblFicheros> ficheros = acService.getAllItems();
		assertTrue(ficheros.size() > 0);
		System.out.println(ficheros.size());
		assertTrue(ficheros.size() == 2024);
	}

	@Test
	public void testSave() {
		String acuerdo = "TEST" + new Double(Math.random()+100).intValue();
		int idFichero = -1;
		TblFicheros elFichero = new TblFicheros();
		elFichero.setFichero(acuerdo);
		elFichero.setFechaFactura(acuerdo);
		elFichero.setCifSupraCliente(acuerdo);
		TblFicherosService acService = new TblFicherosService();
		acService.save(elFichero);
		
		TblFicheros elFicheroGuardado = acService.getByFichero(acuerdo);
		idFichero = elFicheroGuardado.getId();
		assertTrue(idFichero> 0);
	}

	@Test
	public void testGetCFByFichero() {
		TblFicherosService acService = new TblFicherosService();
		
		TblFicheros unFichero = acService.getByFicheroFechaFacturaCif("COC00208.DIC", "20101228", "L      00B82027566");

		assertTrue(unFichero != null);
		assertTrue(unFichero.getId() == 75);
		System.out.println(unFichero);
	}

}
