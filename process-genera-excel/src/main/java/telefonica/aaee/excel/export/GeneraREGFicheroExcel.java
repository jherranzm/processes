package telefonica.aaee.excel.export;

import java.util.ArrayList;
import java.util.Set;

import javax.sql.rowset.CachedRowSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telefonica.aaee.dao.model.Consulta;
import telefonica.aaee.dao.model.Informe;
import telefonica.aaee.dao.model.InformePestanya;
import telefonica.aaee.dao.service.AcuerdoService;
import telefonica.aaee.dao.service.ConsultaService;
import telefonica.aaee.dao.service.InformeService;
import telefonica.aaee.excel.model.ContenidoHojaExcel;

@Service
public class GeneraREGFicheroExcel {
	
	private static final long MAX_REGS = 65000;

	protected final Log logger = LogFactory.getLog(getClass());
	
	private String informe = "";
	private String acuerdo = "";
	private String ruta = "";
	private String excelFile = "";
	
	@Autowired
	private InformeService infService;

	@Autowired
	private ConsultaService consultaService;
	
	@Autowired
	private AcuerdoService acuerdoService;
	
	
	public GeneraREGFicheroExcel() {
		super();
		
	}

	public void execute(){

		ArrayList<ContenidoHojaExcel> ache = new ArrayList<ContenidoHojaExcel>();
		
		Informe acuerdoAplicado = infService.findByNombre(informe).get(0);
				//.getInformeByNombre(informe);
		
		//List<Pestanya> pestanyes = (List<Pestanya>) acuerdoAplicado.getPestanyes();
		Set<InformePestanya> pestanyes = acuerdoAplicado.getPestanyes();
		
		for(InformePestanya pestanya : pestanyes){
			ContenidoHojaExcel che = new ContenidoHojaExcel(
					pestanya.getPestanya().getConsulta().getNombre(), 
					pestanya.getPestanya().getRango(), 
					pestanya.getPestanya().getNombre(),
					pestanya.getPestanya().getNumFilaInicial());
			ache.add(che);	
			
		}

		String[] params = { acuerdo };
		try {

		
			int k = 0;
			while (k < ache.size()){
				ContenidoHojaExcel temp_che = ache.get(k);
				
				logger.debug("SQL:" + temp_che.getConsulta());
				Consulta consulta = consultaService.findByNombre(temp_che.getConsulta()).get(0);
						//.getConsultaSQLByNombre(temp_che.getConsulta());
				String sql = consulta.getDefinicion();
				if(sql == null){
					
				}else{
					
					CachedRowSet crs = null;
					
					if(sql.toUpperCase().indexOf("SELECT") > 0){
						
						String temp_sql = sql;
						temp_sql = temp_sql.toUpperCase();
						int pos = temp_sql.indexOf("FROM ");
						temp_sql = temp_sql.substring(pos);
						temp_sql = "SELECT COUNT(*) " + temp_sql;
						
						
						long numRegs = acuerdoService.getNumRegistros(
								temp_sql.toLowerCase(), 
								params);
						
						if(numRegs > 0 && numRegs < MAX_REGS){
							crs = acuerdoService.getCachedRowSetFromSQL(
									sql.toLowerCase(), 
									params);
							
							logger.info("Registros recuperados:" + crs.size() + " : " + consulta.getNombre());
							temp_che.setCrs(crs);
							temp_che.setNumRegs(crs.size());
							
							ache.set(k, temp_che);
							
						}else{
							logger.info("Registros recuperados:" + numRegs + " : " + consulta.getNombre());
							logger.warn("SQL eliminada:" + sql);
						}
					}else{
						crs = acuerdoService.getCachedRowSetFromSQL(
								sql.toLowerCase(), 
								params);
						
						logger.info("Registros recuperados:" + crs.size() + " : " + consulta.getNombre());
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
