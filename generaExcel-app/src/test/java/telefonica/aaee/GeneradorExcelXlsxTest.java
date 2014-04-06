package telefonica.aaee;

import org.junit.Test;

import telefonica.aaee.app.GeneraREGFicheroExcel;

public class GeneradorExcelXlsxTest {

	@Test
	public void testGeneradorExcelXlsx() {
		GeneraREGFicheroExcel generador = new GeneraREGFicheroExcel();
		//String informe = "SoloEscenario";
		String informe = "acuerdoAplicado";
		String acuerdo = "TEST";
		String ruta = "/Users/jherranzm/dev/";
		
		generador.setAcuerdo(acuerdo);
		generador.setInforme(informe);
		generador.setRuta(ruta);
		
		generador.generaExcel();
		
		System.out.println(generador.getExcelFile());
	}

	@Test
	public void testGeneradorExcelXlsxString() {
		GeneraREGFicheroExcel generador = new GeneraREGFicheroExcel();
		String informe = "AJREUS";
		String acuerdo = "AJREUS_2013_1S";
		String ruta = "V:/Clientes/";
		
		generador.setAcuerdo(acuerdo);
		generador.setInforme(informe);
		generador.setRuta(ruta);
		
		generador.generaExcel();
	}

	@Test
	public void testGeneradorExcelXlsxStringString() {
		long startTime = System.currentTimeMillis();
		
		GeneraREGFicheroExcel generador = new GeneraREGFicheroExcel();
		String informe = "AcuerdoAPB";
		String acuerdo = "APB_2013-1S_VOZ";
		String ruta = "V:/Clientes/";
		
		generador.setAcuerdo(acuerdo);
		generador.setInforme(informe);
		generador.setRuta(ruta);
		
		generador.generaExcel();
		
		long endTime = System.currentTimeMillis();
		
		System.out.println("El proceso ha tardado: " + ((endTime - startTime)/1000) + " segundos!");
	}

}
