package telefonica.aaee.test;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import telefonica.aaee.model.datos.TblAcuerdos;
import telefonica.aaee.services.TblAcuerdosService;

public class TblAcuerdosServiceTest {

	@Test
	public void testGetAllItems() {
		TblAcuerdosService acService = new TblAcuerdosService();
		List<TblAcuerdos> acuerdos = acService.getAllItems();
		assert(acuerdos.size() > 0);
	}

	@Test
	public void testSave() {
		String acuerdo = "AJ_SABA_2012";
		int idAcuerdo = -1;
		TblAcuerdos elAcuerdo = new TblAcuerdos();
		elAcuerdo.setAcuerdo(acuerdo);
		TblAcuerdosService acService = new TblAcuerdosService();
		acService.save(elAcuerdo);
		
		TblAcuerdos elAcuerdoGuardado = acService.getByAcuerdo(acuerdo);
		idAcuerdo = elAcuerdoGuardado.getId();
		assert(idAcuerdo> 0);
	}

	@Test
	public void testGetCFByAcuerdo() {
		fail("Not yet implemented");
	}

}
