package telefonica.aaee.app;

import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import telefonica.aaee.export.GeneradorExcelXlsx;
import telefonica.aaee.model.model977r.ConsultaSQL;
import telefonica.aaee.model.model977r.Informe;
import telefonica.aaee.model.model977r.Pestanya;
import telefonica.aaee.services.services977r.ConsultaSQLService;
import telefonica.aaee.services.services977r.IDB977RDAO;
import telefonica.aaee.services.services977r.InformeService;
import telefonica.aaee.services.services977r.JPAMySQLDB977RDAO;
import telefonica.aaee.utils.ContenidoHojaExcel;


public class GeneraREGFicheroExcel {
	
	private static final long MAX_REGS = 65000;

	protected final Log logger = LogFactory.getLog(getClass());
	
	private String informe = "";
	private String acuerdo = "";
	private String ruta = "";
	private String excelFile = "";
	
	
	private InformeService infService = null;
	private ConsultaSQLService consultaSQLService = null;
	
	
	
	public GeneraREGFicheroExcel() {
		super();
		
		infService = new InformeService();
		consultaSQLService = new ConsultaSQLService();

		

	}

	public static void main (String[] args){
		
		long startTime = System.currentTimeMillis();
		
		GeneraREGFicheroExcel generador = new GeneraREGFicheroExcel();
		
		if(args == null || args.length == 0){
			System.err.println("No se ha informado del INFORME.");
			System.err.println("No se ha informado del ACUERDO.");
			System.err.println("No se ha informado de la RUTA del fichero FINAL.");
			System.exit(1);
		}
		if(args[0] != null && !("".equals(args[0]))){
			generador.setInforme(args[0]);
		}else{
			System.err.println("No se ha informado del INFORME.");
			System.exit(1);
		}
		
		if(!generador.existeElInforme(generador.getInforme())){
			System.err.println("El INFORME." + args[0] + " NO existe en la Base de Datos!");
			System.exit(1);
		}

		if(args[1] != null && !("".equals(args[1]))){
			generador.setAcuerdo(args[1]);
		}else{
			System.err.println("No se ha informado del ACUERDO.");
			System.exit(1);
		}
		if(args[2] != null && !("".equals(args[2]))){
			generador.setRuta(args[2]);
		}else{
			System.err.println("No se ha informado de la RUTA del fichero FINAL.");
			System.exit(1);
		}
		generador.generaExcel();
		
		long endTime = System.currentTimeMillis();
		
		System.out.println("El proceso ha tardado: " + ((endTime - startTime)/1000) + " segundos!");
	}
	
	public void generaExcel(){

		ArrayList<ContenidoHojaExcel> ache = new ArrayList<ContenidoHojaExcel>();
		
		Informe acuerdoAplicado = infService.getInformeByNombre(informe);
		
		List<Pestanya> pestanyes = (List<Pestanya>) acuerdoAplicado.getPestanyes();
		
		for(Pestanya pestanya : pestanyes){
			ContenidoHojaExcel che = new ContenidoHojaExcel(
					pestanya.getConsulta().getNombre(), 
					pestanya.getRango(), 
					pestanya.getNombre(),
					pestanya.getNumFilaInicial());
			ache.add(che);	
			
		}

		IDB977RDAO db = new JPAMySQLDB977RDAO();
		
		String[] params = { acuerdo };
		try {

		
			int k = 0;
			while (k < ache.size()){
				ContenidoHojaExcel temp_che = ache.get(k);
				
				logger.debug("SQL:" + temp_che.getConsulta());
				ConsultaSQL cSQL = consultaSQLService.getConsultaSQLByNombre(temp_che.getConsulta());
				String sql = cSQL.getDefinicion();
				if(sql == null){
					
				}else{
					
					CachedRowSet crs = null;
					
					if(sql.toUpperCase().indexOf("SELECT") > 0){
						
						String temp_sql = sql;
						temp_sql = temp_sql.toUpperCase();
						int pos = temp_sql.indexOf("FROM ");
						temp_sql = temp_sql.substring(pos);
						temp_sql = "SELECT COUNT(*) " + temp_sql;
						
						
						long numRegs = db.getNumRegistros(
								temp_sql.toLowerCase(), 
								params);
						
						if(numRegs > 0 && numRegs < MAX_REGS){
							crs = db.getCachedRowSetFromSQL(
									sql.toLowerCase(), 
									params);
							
							logger.info("Registros recuperados:" + crs.size() + " : " + cSQL.getNombre());
							temp_che.setCrs(crs);
							temp_che.setNumRegs(crs.size());
							
							ache.set(k, temp_che);
							
						}else{
							logger.info("Registros recuperados:" + numRegs + " : " + cSQL.getNombre());
							logger.warn("SQL eliminada:" + sql);
						}
					}else{
						crs = db.getCachedRowSetFromSQL(
								sql.toLowerCase(), 
								params);
						
						logger.info("Registros recuperados:" + crs.size() + " : " + cSQL.getNombre());
						temp_che.setCrs(crs);
						temp_che.setNumRegs(crs.size());
						
						ache.set(k, temp_che);
						
					}
				}
				
				
				k++;
			}
		
			/**
			 * Se genera el libro Excel con los parámetros proporcionados
			 */
			GeneradorExcelXlsx excelGen = new GeneradorExcelXlsx();
			excelGen.setPrefijoFichero(informe + "_" + params[0] + "_");
			excelGen.setRealPath(ruta);

			if (excelGen.export_v2(ache)) {
				
				StringBuffer sb = new StringBuffer();
				sb.append("Fin!").append("\n");
				sb.append("El fichero generado es :[").append(excelGen.getFile()).append("]").append("\n");
				sb.append("El tamaño del fichero es de :[").append(excelGen.getFileSize()).append("] bytes.\n");
				logger.info(sb.toString());
				
				this.setExcelFile(excelGen.getFullFile());
				
			} else {
				logger.fatal("Algún error...");
				// EnvioCorreoError.envioCorreo("sqlToXLS Error!");
				logger.warn("Error en ReadNewHZFile.execute()");
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			logger.fatal("Se ha producido una excepción...");
			logger.warn(e.getMessage());

		}finally{
			
		}
	
	}
	
	private boolean existeElInforme(String elInforme){
		return (infService.getInformeByNombre(elInforme) != null);
	}
	
	
	
	
	
	

	/**
	 * @return the informe
	 */
	public String getInforme() {
		return informe;
	}

	/**
	 * @param informe the informe to set
	 */
	public void setInforme(String informe) {
		this.informe = informe;
	}

	/**
	 * @return the acuerdo
	 */
	public String getAcuerdo() {
		return acuerdo;
	}

	/**
	 * @param acuerdo the acuerdo to set
	 */
	public void setAcuerdo(String acuerdo) {
		this.acuerdo = acuerdo;
	}

	/**
	 * @return the ruta
	 */
	public String getRuta() {
		return ruta;
	}

	/**
	 * @param ruta the ruta to set
	 */
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public String getExcelFile() {
		return excelFile;
	}

	public void setExcelFile(String excelFile) {
		this.excelFile = excelFile;
	}
}
