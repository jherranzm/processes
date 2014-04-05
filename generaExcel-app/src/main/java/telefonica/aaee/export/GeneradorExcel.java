/**
 *
 */
package telefonica.aaee.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.apache.log4j.Logger;

import javax.sql.RowSetMetaData;
import javax.sql.rowset.CachedRowSet;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFName;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.DataFormat;

import telefonica.aaee.exceptions.GeneradorExcelException;
import telefonica.aaee.utils.ConstantsREG;
import telefonica.aaee.utils.ContenidoHojaExcel;

/**
 * @author José Luis Herranz Martín
 * 
 *         05/02/2009
 * 
 */
public class GeneradorExcel {
	
	final private static Logger LOGGER = Logger.getLogger(GeneradorExcel.class.getCanonicalName());
	final private static long MAX_FILAS_EXCEL = 65530;


	final private String formatoFloat = "#,###,##0.0000;[RED]-#,###,##0.0000;[GREEN]0.0000";
	final private String formatoEntero = "#0;[RED]-#0";
	final private String formatoPorcentaje = "#0.00%;[RED]-#0.00%";

	private String realPath = null;
	private String fullFile = null;
	private String file = null;
	private String fileIn = null;
	private String nombreHoja = "Hoja1";

	private FileOutputStream fileOut = null;
	private String xlsFileOrig = "PeticionesPorDia.xls";
	private String prefijoFichero = "PeticionesPorDia_";

	private long fileSize = 0;
	
	private HSSFWorkbook wbOutput;
	private HSSFSheet hojaActual;
	private HSSFFont fontBlanca;
	
	private HSSFCellStyle 
		cabeceraGenerica, 
		precioEspecialStyle, 
		precioEspecialIntegerStyle, 
		precioEspecialFloatStyle, 
		precioEspecialPercentageStyle, 
		cabeceraPrecioEspecial,

		//cellStyle, 
		cellStyleInteger,
		cellStyleFloat,
		cellStylePercentage,
		cellStyleOther,
		cellStyleDate;
	
	/**
	 * Constructor
	 * 
	 */
	public GeneradorExcel() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param path
	 * 
	 */
	public GeneradorExcel(String path) {
		super();
		setRealPath(path);
		setFileIn(getRealPath()  +  xlsFileOrig);
	}

	/**
	 * Constructor
	 * 
	 * @param path la ruta completa
	 * @param template plantilla a utilizar
	 * 
	 */
	public GeneradorExcel(String path, String template) {
		super();
		setRealPath(path);
		setFileIn(getRealPath()  +  template);
	}
	
	
	private void creaEstilos() {
		DataFormat format = wbOutput.createDataFormat();

		cabeceraGenerica = wbOutput.createCellStyle();
		precioEspecialStyle = wbOutput.createCellStyle();
		precioEspecialFloatStyle = wbOutput.createCellStyle();
		precioEspecialPercentageStyle = wbOutput.createCellStyle();
		precioEspecialIntegerStyle = wbOutput.createCellStyle();
		//cellStyle = wbOutput.createCellStyle();
		cabeceraPrecioEspecial = wbOutput.createCellStyle();

		fontBlanca = wbOutput.createFont();
		fontBlanca.setColor(HSSFColor.WHITE.index);
		fontBlanca.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	
		cabeceraGenerica.setFillBackgroundColor(HSSFColor.GREEN.index);
		cabeceraGenerica.setFillForegroundColor(HSSFColor.GREEN.index);
		cabeceraGenerica.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cabeceraGenerica.setWrapText(true);
		cabeceraGenerica.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cabeceraGenerica.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cabeceraGenerica.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cabeceraGenerica.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cabeceraGenerica.setFont(fontBlanca);

		cabeceraPrecioEspecial.setFillBackgroundColor(HSSFColor.DARK_BLUE.index);
		cabeceraPrecioEspecial.setFillForegroundColor(HSSFColor.DARK_BLUE.index);
		cabeceraPrecioEspecial.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cabeceraPrecioEspecial.setWrapText(true);
		cabeceraPrecioEspecial.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cabeceraPrecioEspecial.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cabeceraPrecioEspecial.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cabeceraPrecioEspecial.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cabeceraPrecioEspecial.setFont(fontBlanca);
		
		
		cellStyleInteger = wbOutput.createCellStyle();
		cellStyleFloat = wbOutput.createCellStyle();
		cellStylePercentage = wbOutput.createCellStyle();
		cellStyleOther = wbOutput.createCellStyle();
		cellStyleDate = wbOutput.createCellStyle();

		cellStyleInteger.setDataFormat(HSSFDataFormat.getBuiltinFormat(formatoEntero));
		cellStyleFloat.setDataFormat(HSSFDataFormat.getBuiltinFormat(formatoFloat));
		cellStyleOther.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
		cellStyleDate.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy/MM/dd"));
		
		//
		cellStyleFloat.setDataFormat(format.getFormat(formatoFloat)); // "#.0000" 4 decimales 
		cellStylePercentage.setDataFormat(format.getFormat(formatoPorcentaje)); // "#.0000" 4 decimales 

			
		precioEspecialStyle.setFillBackgroundColor(HSSFColor.LIGHT_GREEN.index);
		precioEspecialStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
		precioEspecialStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		precioEspecialFloatStyle.setFillBackgroundColor(HSSFColor.LIGHT_GREEN.index);
		precioEspecialFloatStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
		precioEspecialFloatStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		precioEspecialFloatStyle.setDataFormat(format.getFormat(formatoFloat)); // "#.0000" 4 decimales 
		
		precioEspecialIntegerStyle.setFillBackgroundColor(HSSFColor.LIGHT_GREEN.index);
		precioEspecialIntegerStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
		precioEspecialIntegerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		precioEspecialIntegerStyle.setDataFormat(format.getFormat(formatoEntero)); // "#.0000" 4 decimales 

		precioEspecialPercentageStyle.setFillBackgroundColor(HSSFColor.LIGHT_GREEN.index);
		precioEspecialPercentageStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
		precioEspecialPercentageStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		precioEspecialPercentageStyle.setDataFormat(format.getFormat(formatoPorcentaje)); // "#.0000" 4 decimales 
	}

	private String getRango(CachedRowSet crs) {
		String nomColumnes = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuffer nomColumna = new StringBuffer();
		long maxFila = 1;
		try {
			int indexCol = crs.getMetaData().getColumnCount();
			LOGGER.info("indexCol:[" + indexCol + "]");
			LOGGER.info("nomColumnes.length():[" + nomColumnes.length() + "]");
			if (indexCol > 0) {
				int pos1 = indexCol % nomColumnes.length();
	
				LOGGER.info("pos1:[" + pos1 + "]");
				int fl = (int) (indexCol / nomColumnes.length());
				LOGGER.info("fl:[" + fl + "]");
	
//				if (fl > 0) nomColumna.append(nomColumnes.charAt(fl - 1));
				if (fl > 0) nomColumna.append(nomColumnes.charAt(fl));
				nomColumna.append(nomColumnes.charAt(pos1 - 1));
			} else {
				nomColumna.append("A");
			}
	
			if (crs.size() > maxFila)
				maxFila = crs.size()  +  1;
	
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return "$A$1:$"  +  nomColumna  + "$" +  maxFila;
	}

	/**
	 * @param che
	 */
	private void crearNombreRangoEnLibro(ContenidoHojaExcel che) {
		if(che.getNumRegs() > 0){
			final String strName = che.getNombre();
			LOGGER.info("Nombre de la pestaña:[" + strName + "]");
			final String nomRango = getRango(che.getCrs());
			LOGGER.info("Nombre del rango:"  +  nomRango);
			String n11 = "'"  +  che.getNombrePestanya()  +  "'!"  +  nomRango;
//			String n11 = ""  +  che.getNombre()  +  "!"  +  nomRango;
			HSSFName name = wbOutput.createName();
			name.setNameName(strName);
			name.setRefersToFormula(n11);
			LOGGER.info("Aplicado el nombre del rango:"  +  n11);
			LOGGER.info("Número de nombres en el libro:" + wbOutput.getNumberOfNames());
			for(int k = 0; k< wbOutput.getNumberOfNames(); k++){
				LOGGER.info("Número de nombres en el libro:" + wbOutput.getNameAt(k).getRefersToFormula());
			}
			
		}
	}

	/**
	 * 
	 * Crea un libro nuevo y lo rellena con los datos que hay en
	 * la lista de ContenidoHojaExcel pasada por parámetro
	 * 
	 * 
	 * @param ArrayList<ContenidoHojaExcel>
	 * 
	 * @return true or false
	 * @throws GeneradorExcelException 
	 */

	public boolean export(ArrayList<ContenidoHojaExcel> ache) throws GeneradorExcelException {
	
		boolean ret = false;
	
		final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS");
		final String sDate = formatter.format(new Date());
	
		setFile(getPrefijoFichero()  +  sDate  +  ".xls");
		setFullFile(getRealPath()  +  getFile());
	
		LOGGER.info("Fichero temporal:"  +  getFullFile());
	
		try {
	
			fileOut = new FileOutputStream(getFullFile());
	
			wbOutput = new HSSFWorkbook();
			creaEstilos();
	
				LOGGER.info("Fichero y cabeceras creadas!");
				
	
			// Se crean las hojas necesarias
			
			for (ContenidoHojaExcel che : ache) {
				if (che.getNumRegs() != 0) {
					wbOutput.createSheet();
				}
			}
	
			int index = 0;
			LOGGER.info("Número de pestañas:  ache.size() = "  +  ache.size());
			for (ContenidoHojaExcel che : ache) {
				
				LOGGER.info("Procesando la pestaña...");
				LOGGER.info("Nombre:"  +  che.getNombre());
				LOGGER.info("Nombre Pestaña:"  +  che.getNombrePestanya());
				LOGGER.info("Número de registros:"  +  che.getNumRegs());
				LOGGER.info(ConstantsREG.SEP_VERTICAL);

				if (che.getNumRegs() == 0) {
					
					//Esta hoja no se añade
					
					//HSSFSheet sheet1 = wbOutput.getSheetAt(index);
					
					
				} else if (che.getNumRegs() <= MAX_FILAS_EXCEL) {
					hojaActual = null;
					//HSSFSheet sheet1 = wbOutput.getSheetAt(index);
					hojaActual = wbOutput.getSheetAt(index);
	
					hojaActual.setZoom(3, 4); // Zoom del 75%
					hojaActual.createFreezePane(0, 1, 0, 1);
					// Ponemos el nombre del rango
					wbOutput.setSheetName(index, che.getNombrePestanya());
	
					crearNombreRangoEnLibro(che);
					//ret = fillExcelSheet_especial(che.getCrs(), sheet1);
					ret = fillExcelSheet_especial(hojaActual, che.getCrs());
					
					index ++ ;
					
				} else {
					//throw new GeneradorExcelException("En la pestaña "  + che.getNombre() + " se ha producido un error, ya que hay "  +  che.getNumRegs()  +  " registros.");
					// No hacemos nada...
					LOGGER.warn("En la pestaña "  + che.getNombre() + " se ha producido un error, ya que hay "  +  che.getNumRegs()  +  " registros.");
				}
				//index ++ ;
			}
	
			//wbOutput.write(fileOut);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.warn("Se ha producido una excepción de SQL");
			LOGGER.info(ConstantsREG.SEP_VERTICAL);
			throw new GeneradorExcelException("Se ha producido un error de SQL:"  +  e.getErrorCode()  +  ":"  +  e.getSQLState());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.warn("Se ha producido una excepción Genérica...");
			LOGGER.info(ConstantsREG.SEP_VERTICAL);
			throw new GeneradorExcelException("Se ha producido un error genérico:"  +  e.getMessage());
		} finally {
			try {
				//fileOut.
				LOGGER.info("Llegamos a escribir el fichero...");
				//logger.info("Longitud del fichero:"  +  wbOutput.getBytes().length);
				
				wbOutput.write(fileOut);
				fileOut.close();
				File file = new File(getFullFile());
				fileSize = file.length();
	
			} catch (IOException e) {
				e.printStackTrace();
				LOGGER.warn("Se ha producido una excepción IO...");
				LOGGER.info(ConstantsREG.SEP_VERTICAL);
				throw new GeneradorExcelException("Se ha producido una excepción IO:"  +  e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.warn("Se ha producido una excepción Genérica...");
				LOGGER.info(ConstantsREG.SEP_VERTICAL);
				throw new GeneradorExcelException("Se ha producido un error genérico:"  +  e.getMessage());
			}
		}
	
		return ret;
	
	}

	/**
	 * 
	 * Crea un libro nuevo y lo rellena con los datos que hay en
	 * la lista de ContenidoHojaExcel pasada por parámetro
	 * 
	 * 
	 * @param ArrayList<ContenidoHojaExcel>
	 * 
	 * @return true or false
	 * @throws GeneradorExcelException 
	 */
	
	public boolean export_v2(ArrayList<ContenidoHojaExcel> ache) 
	
		throws GeneradorExcelException {
	
		boolean ret = false;
	
		final SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyyMMdd-HHmmss-SSS");
		final String sDate = formatter.format(new Date());
	
		setFile(getPrefijoFichero()  +  sDate  +  ".xls");
		setFullFile(getRealPath()  +  getFile());
	
		LOGGER.info("Fichero temporal:"  +  getFullFile());
	
		try {
	
			fileOut = new FileOutputStream(getFullFile());
	
			wbOutput = new HSSFWorkbook();
			creaEstilos();
	
				LOGGER.info("Fichero y cabeceras creadas!");
		
	
			// Se crean las hojas necesarias
			
			for (ContenidoHojaExcel che : ache) {
				if (che.getNumRegs() != 0) {
					wbOutput.createSheet();
				}
			}
	
			int index = 0;
			LOGGER.info("Número de pestañas:  ache.size() = "  +  ache.size());
			for (ContenidoHojaExcel che : ache) {
				
				LOGGER.info("Procesando la pestaña...");
				LOGGER.info("Nombre:"  +  che.getNombre());
				LOGGER.info("Nombre Pestaña:"  +  che.getNombrePestanya());
				LOGGER.info("Número de registros:"  +  che.getNumRegs());
				LOGGER.info(ConstantsREG.SEP_VERTICAL);
	
				if (che.getNumRegs() == 0) {
					
					//Esta hoja no se añade
					
					//HSSFSheet sheet1 = wbOutput.getSheetAt(index);
					
					
				} else if (che.getNumRegs() <= MAX_FILAS_EXCEL) {
					hojaActual = null;
					//HSSFSheet sheet1 = wbOutput.getSheetAt(index);
					hojaActual = wbOutput.getSheetAt(index);
	
					if(index > 0 ) {
						 hojaActual.setZoom(3, 4); // Zoom del 75%
					}
					hojaActual.createFreezePane(
							0, (int)(che.getNumFilaInicial()  +  1), 
							0, (int)(che.getNumFilaInicial()  +  1));
					// Ponemos el nombre de la pestaña
					wbOutput.setSheetName(index, che.getNombrePestanya());
					LOGGER.info("La pestaña se llama:[" + wbOutput.getSheetName(index) + "]");
					
					
	
					crearNombreRangoEnLibro(che);
					//ret = fillExcelSheet_especial(che.getCrs(), sheet1);
					ret = fillExcelSheet_especial_filaInicial(hojaActual, che.getNumFilaInicial(), che.getCrs());
					
					index ++ ;
					
				} else {
					//throw new GeneradorExcelException("En la pestaña "  + che.getNombre() + " se ha producido un error, ya que hay "  +  che.getNumRegs()  +  " registros.");
					// No hacemos nada...
					LOGGER.warn("En la pestaña "  + che.getNombre() + " se ha producido un error, ya que hay "  +  che.getNumRegs()  +  " registros.");
				}
				//index ++ ;
			}
	
			//wbOutput.write(fileOut);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.warn("Se ha producido una excepción de SQL");
			LOGGER.info(ConstantsREG.SEP_VERTICAL);
			throw new GeneradorExcelException("Se ha producido un error de SQL:"  +  e.getErrorCode()  +  ":"  +  e.getSQLState());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.warn("Se ha producido una excepción Genérica...");
			LOGGER.info(ConstantsREG.SEP_VERTICAL);
			throw new GeneradorExcelException("Se ha producido un error genérico:"  +  e.getMessage());
		} finally {
			try {
				//fileOut.
				LOGGER.info("Llegamos a escribir el fichero...");
				//logger.info("Longitud del fichero:"  +  wbOutput.getBytes().length);
				
				wbOutput.write(fileOut);
				fileOut.close();
				File file = new File(getFullFile());
				fileSize = file.length();
	
			} catch (IOException e) {
				e.printStackTrace();
				LOGGER.warn("Se ha producido una excepción IO...");
				LOGGER.info(ConstantsREG.SEP_VERTICAL);
				throw new GeneradorExcelException("Se ha producido una excepción IO:"  +  e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.warn("Se ha producido una excepción Genérica...");
				LOGGER.info(ConstantsREG.SEP_VERTICAL);
				throw new GeneradorExcelException("Se ha producido un error genérico:"  +  e.getMessage());
			}
		}
	
		return ret;
	
	}

	/**
	 * 
	 * Crea un libro nuevo y lo rellena con los datos que hay en
	 * la lista de ContenidoHojaExcel pasada por parámetro
	 * 
	 * 
	 * @param ArrayList<ContenidoHojaExcel>
	 * 
	 * @return true or false
	 * @throws GeneradorExcelException 
	 */

	public boolean export_CargaMOFA(
			ArrayList<ContenidoHojaExcel> ache) 
	
		throws GeneradorExcelException 
	
		 {
	
		boolean ret = false;
	
		final SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyyMMdd-HHmmss-SSS");
		final String sDate = formatter.format(new Date());
	
		setFile(getPrefijoFichero()  +  sDate  +  ".xls");
		setFullFile(getRealPath()  +  getFile());
	
		LOGGER.info("Fichero temporal:"  +  getFullFile());
	
		try {
	
			fileOut = new FileOutputStream(getFullFile());
	
			POIFSFileSystem doc = new POIFSFileSystem(new FileInputStream(
					getFileIn()));
	
			wbOutput = new HSSFWorkbook(doc);

			int index = 0;
			LOGGER.info("ache.size() = "  +  ache.size());
			for (ContenidoHojaExcel che : ache) {
				if (che.getNumRegs() == 0) {
					
					//Esta hoja no se añade
					
					//HSSFSheet sheet1 = wbOutput.getSheetAt(index);
					
					LOGGER.info("Número de registros a tratar 0!");
					
					
				} else if (che.getNumRegs() <= MAX_FILAS_EXCEL) {
					
					LOGGER.info("Número de registros a tratar:"  +  che.getNumRegs());

					// localizamos la primera hoja
					HSSFSheet sheet1 = wbOutput.getSheetAt(index);

					// Ponemos el nombre de la pestaña que ha de ser fija "Estandar"
					wbOutput.setSheetName(index, "Estandar");
	
					//crearNombreRangoEnLibro(che);

					ret = fillExcelSheet_CargaMOFA(che.getCrs(), sheet1);
					
					index ++ ;
					
				} else {
					throw new GeneradorExcelException("En la pestaña "  + che.getNombre() + " se ha producido un error, ya que hay "  +  che.getNumRegs()  +  " registros.");
				}
				//index ++ ;
			}
	
			//wbOutput.write(fileOut);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new GeneradorExcelException("Se ha producido un error de Formato de número.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new GeneradorExcelException("Se ha producido un error: no se ha encontrado el fichero.");
		} catch (IOException e) {
			e.printStackTrace();
			throw new GeneradorExcelException("Se ha producido un error de entrada/salida.");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new GeneradorExcelException("Se ha producido un error de SQL:"  +  e.getErrorCode()  +  ":"  +  e.getSQLState());
		} catch (Exception e) {
			e.printStackTrace();
			throw new GeneradorExcelException("Se ha producido un error genérico:"  +  e.getMessage());
		} finally {
			try {
				wbOutput.write(fileOut);
				fileOut.close();
				File file = new File(getFullFile());
				fileSize = file.length();
	
			} catch (IOException e) {
				e.printStackTrace();
				throw new GeneradorExcelException("Se ha producido un error de entrada/salida.");
			} catch (Exception e) {
				e.printStackTrace();
				throw new GeneradorExcelException("Se ha producido un error genérico:"  +  e.getMessage());
			}
		}
	
		return ret;
	
	}

	
	
	/**
	 * 
	 * Crea un libro nuevo y lo rellena con los datos que hay en
	 * la lista de ContenidoHojaExcel pasada por parámetro
	 * 
	 * 
	 * @param ArrayList<ContenidoHojaExcel>
	 * 
	 * @return true or false
	 * @throws GeneradorExcelException 
	 */
	
	public boolean export_CIFsAParalelo(
			ArrayList<ContenidoHojaExcel> ache) 
	
		throws GeneradorExcelException 
	
		 {
	
		boolean ret = false;
	
		final SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyyMMdd-HHmmss-SSS");
		final String sDate = formatter.format(new Date());
	
		setFile(getPrefijoFichero()  +  sDate  +  ".xls");
		setFullFile(getRealPath()  +  getFile());
	
		LOGGER.info("Fichero temporal:"  +  getFullFile());
	
		try {
	
			fileOut = new FileOutputStream(getFullFile());
	
			POIFSFileSystem doc = new POIFSFileSystem(new FileInputStream(
					getFileIn()));
	
			wbOutput = new HSSFWorkbook(doc);
	
			int index = 0;
			LOGGER.info("ache.size() = "  +  ache.size());
			for (ContenidoHojaExcel che : ache) {
				if (che.getNumRegs() == 0) {
					
					//Esta hoja no se añade
					
					//HSSFSheet sheet1 = wbOutput.getSheetAt(index);
					
					LOGGER.info("Número de registros a tratar 0!");
					
					
				} else if (che.getNumRegs() <= MAX_FILAS_EXCEL) {
					
					LOGGER.info("Número de registros a tratar:"  +  che.getNumRegs());
	
					// localizamos la primera hoja
					HSSFSheet sheet1 = wbOutput.getSheetAt(index);
	
					// Ponemos el nombre de la pestaña que ha de ser fija "Estandar"
					wbOutput.setSheetName(index, "Estandar");
	
					crearNombreRangoEnLibro(che);
	
					ret = fillExcelSheet_CIFsAParalelo(che.getCrs(), sheet1);
					
					index ++ ;
					
				} else {
					throw new GeneradorExcelException("En la pestaña "  + che.getNombre() + " se ha producido un error, ya que hay "  +  che.getNumRegs()  +  " registros.");
				}
				//index ++ ;
			}
	
			//wbOutput.write(fileOut);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new GeneradorExcelException("Se ha producido un error de Formato de número.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new GeneradorExcelException("Se ha producido un error: no se ha encontrado el fichero.");
		} catch (IOException e) {
			e.printStackTrace();
			throw new GeneradorExcelException("Se ha producido un error de entrada/salida.");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new GeneradorExcelException("Se ha producido un error de SQL:"  +  e.getErrorCode()  +  ":"  +  e.getSQLState());
		} catch (Exception e) {
			e.printStackTrace();
			throw new GeneradorExcelException("Se ha producido un error genérico:"  +  e.getMessage());
		} finally {
			try {
				wbOutput.write(fileOut);
				fileOut.close();
				File file = new File(getFullFile());
				fileSize = file.length();
	
			} catch (IOException e) {
				e.printStackTrace();
				throw new GeneradorExcelException("Se ha producido un error de entrada/salida.");
			} catch (Exception e) {
				e.printStackTrace();
				throw new GeneradorExcelException("Se ha producido un error genérico:"  +  e.getMessage());
			}
		}
	
		return ret;
	
	}

	/**
	 * @return the realPath
	 */
	public String getRealPath() {
		return realPath;
	}

	/**
	 * @param realPath
	 *            the realPath to set
	 */
	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}

	/**
	 * @return the file
	 */
	public String getFile() {
		return file;
	}

	/**
	 * @return the file
	 */
	public String getFullFile() {
		return fullFile;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFullFile(String file) {
		this.fullFile = file;
	}

	/**
	 * @return the fileIn
	 */
	public String getFileIn() {
		return fileIn;
	}

	/**
	 * @param fileIn
	 *            the fileIn to set
	 */
	public void setFileIn(String fileIn) {
		this.fileIn = fileIn;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(String file) {
		this.file = file;
	}

	public void setPrefijoFichero(String prefijoFichero) {
		this.prefijoFichero = prefijoFichero;
	}

	public String getPrefijoFichero() {
		return prefijoFichero;
	}

	public String getNombreHoja() {
		return nombreHoja;
	}

	public void setNombreHoja(String nombreHoja) {
		this.nombreHoja = nombreHoja;
	}

	/**
	 * @param fileSize
	 *            the fileSize to set
	 */
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * @return the fileSize
	 */
	public long getFileSize() {
		return fileSize;
	}

	/**
	 * 
	 * Rellena la pestaña <param>sheet1</param> pasada por parámetro
	 * con los datos de la que se hallan 
	 * en el CachedRowSet <param>crs</param>.
	 * 
	 * En el caso de que haya alguna columna con nombre especial,
	 * se le aplica un tipo de estilo diferente.
	 * 
	 * 
	 * @param crs
	 * @param sheet1
	
	 * @return true or false
	 * @throws SQLException
	 */
	private boolean fillExcelSheet_CargaMOFA(
			final CachedRowSet crs,
			final HSSFSheet sheet1) 
	
		throws SQLException {
	
		return fillExcelSheet(crs, sheet1, 2);
	}

	/**
	 * 
	 * Rellena la pestaña <param>sheet1</param> pasada por parámetro
	 * con los datos de la que se hallan 
	 * en el CachedRowSet <param>crs</param>.
	 * 
	 * En el caso de que haya alguna columna con nombre especial,
	 * se le aplica un tipo de estilo diferente.
	 * 
	 * 
	 * @param crs CachedRowSet, lista de datos
	 * @param sheet1 pestaña a rellenar
	
	 * @return true or false
	 * @throws SQLException
	 */
	private boolean fillExcelSheet_CIFsAParalelo(
			final CachedRowSet crs,
			final HSSFSheet sheet1) 
	
		throws SQLException {
	
		return fillExcelSheet(crs, sheet1, 1);
	}

	/**
	 * 
	 * Rellena la pestaña <param>sheet1</param> pasada por parámetro
	 * con los datos de la que se hallan 
	 * en el CachedRowSet <param>crs</param>.
	 * 
	 * En el caso de que haya alguna columna con nombre especial,
	 * se le aplica un tipo de estilo diferente.
	 * 
	 * 
	 * @param crs CachedRowSet, lista de datos
	 * @param sheet1 pestaña a rellenar
	 * @param _f fila inicial
	
	 * @return true or false
	 * @throws SQLException
	 */
	private boolean fillExcelSheet(
			final CachedRowSet crs,
			final HSSFSheet sheet1,
			int _f) 
	
		throws SQLException {
	
		final ResultSetMetaData rsmd = (RowSetMetaData) crs.getMetaData();
		final int numColumnas = rsmd.getColumnCount();
	
		filasDatos(sheet1, crs, 999, rsmd, numColumnas, _f);

		return true;
	}

	/**
	 * 
	 * Rellena la pestaña hojaActual
	 * con los datos de la que se hallan 
	 * en el CachedRowSet <param>crs</param>.
	 * <br/>
	 * En el caso de que haya alguna columna con nombre especial,
	 * se le aplica un tipo de estilo diferente.<br/>
	 * 
	 * La pestaña será hojaActual,<br/>
	 * y la primera fila, será la fila 0
	 * 
	 * 
	 * @param crs
	
	 * @return true or false
	 * @throws SQLException
	 */
	private boolean fillExcelSheet_especial(final HSSFSheet pestanya, CachedRowSet crs ) throws SQLException {
		
		return fillExcelSheet_especial_filaInicial(pestanya, 0, crs);

	}

	/**
		 * 
		 * Rellena la pestaña hojaActual
		 * con los datos de la que se hallan 
		 * en el CachedRowSet <param>crs</param>.
		 * 
		 * Empieza en la fila <param>filaInicial</param>
		 * 
		 * En el caso de que haya alguna columna con nombre especial,
		 * se le aplica un tipo de estilo diferente.
		 * 
		 * 
		 * @param crs
		
		 * @return true or false
		 * @throws SQLException
		 */
		private boolean fillExcelSheet_especial_filaInicial(
				final HSSFSheet pestanya, 
				final long filaInicial, 
				final CachedRowSet crs) 
		
			throws SQLException {
			
			int columnaPorcentaje = 999;
		
			ResultSetMetaData rsmd = (RowSetMetaData) crs.getMetaData();
			
			int numColumnas = rsmd.getColumnCount();
		
			int _f = (int)filaInicial; // fila inicial
			
			columnaPorcentaje = filaCabeceras(hojaActual, columnaPorcentaje, rsmd, numColumnas, _f);
			
			_f ++ ;
			
			LOGGER.info("Se van a tratar "  +  crs.size()  +  " registros...");
		
			_f = filasDatos(hojaActual, crs, columnaPorcentaje, rsmd, numColumnas, _f);
			
			corrigeAnchuraColumnas(hojaActual, rsmd, numColumnas);
		
			return true;
		}

	/**
	 * @param columnaPorcentaje
	 * @param rsmd
	 * @param numColumnas
	 * @param _f
	 * 
	 * 
	 * @return columna porcentaje
	 * @throws SQLException
	 */
	private int filaCabeceras( final HSSFSheet pestanya, int columnaPorcentaje, final ResultSetMetaData rsmd, final int numColumnas, final int _f)
			throws SQLException {
		
		
		HSSFRow row = pestanya.createRow(_f);
		for (int _c = 1; _c <= numColumnas; _c ++ ) {
			
			HSSFCell cell = row.createCell(_c - 1);
			cell.setCellStyle(cabeceraGenerica);
			
			String tmpCabecera = rsmd.getColumnName(_c).replaceAll("_", " ").toUpperCase();
			LOGGER.info(rsmd.getColumnName(_c)  +  " -> "  +  tmpCabecera);
			cell.setCellValue(tmpCabecera);
			
			if(
					rsmd.getColumnName(_c).equalsIgnoreCase("PRECIO_ESPECIAL")
					|| rsmd.getColumnName(_c).equalsIgnoreCase("IMPORTE_ACUERDO")
					|| rsmd.getColumnName(_c).equalsIgnoreCase("IMPORTE ACUERDO")
					|| rsmd.getColumnName(_c).equalsIgnoreCase("TIPO_DESCUENTO")
					|| rsmd.getColumnName(_c).equalsIgnoreCase("EST_LLAMADA")
					|| rsmd.getColumnName(_c).equalsIgnoreCase("tipo_precio_especial")
					|| rsmd.getColumnName(_c).equalsIgnoreCase("PORCENTAJE_DESCUENTO")
					|| rsmd.getColumnName(_c).equalsIgnoreCase("PRECIO_POR_MINUTO")
					|| rsmd.getColumnName(_c).equalsIgnoreCase("PRECIO ACUERDO")
					//Descuento por acuerdo
					| rsmd.getColumnName(_c).equalsIgnoreCase("Descuento por acuerdo")
	
				) {
				cell.setCellStyle(cabeceraPrecioEspecial);
				LOGGER.info("Columna especial:"  +  rsmd.getColumnName(_c));
			}
			if(rsmd.getColumnName(_c).equalsIgnoreCase("PORCENTAJE")) {
				columnaPorcentaje = _c;
			}
			
		}
		return columnaPorcentaje;
	}

	/**
	 * @param crs
	 * @param columnaPorcentaje
	 * @param rsmd
	 * @param numColumnas
	 * @param _f
	 * @return
	 * @throws SQLException
	 */
	private int filasDatos(
			final HSSFSheet pestanya, 
			final CachedRowSet crs, 
			final int columnaPorcentaje, 
			final ResultSetMetaData rsmd, 
			final int numColumnas, 
			int _f)
			throws SQLException {
		
		
		HSSFRow row;
		// Recorremos las filas
		while (crs.next()) {
			row = pestanya.createRow(_f);
			boolean rowPrecioEspecial = false;

			// Recorremos las columnas
			for (int _c = 1; _c <= numColumnas; _c ++ ) {
				
				LOGGER.info("Columna: " + _c);

				HSSFCell cell = row.createCell((_c - 1)); // poi.3.6
				
				LOGGER.info("rsmd.getColumnType(_c):" + rsmd.getColumnType(_c));
				
				if (rsmd.getColumnType(_c) == java.sql.Types.INTEGER) {
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(crs.getInt(_c));
					cell.setCellStyle(cellStyleInteger);

				} else if (rsmd.getColumnType(_c) == java.sql.Types.BIGINT) {
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(crs.getLong(_c));
					cell.setCellStyle(cellStyleFloat);

				} else if (rsmd.getColumnType(_c) == java.sql.Types.FLOAT
						|| rsmd.getColumnType(_c) == java.sql.Types.DOUBLE
						|| rsmd.getColumnTypeName(_c).equals("DECIMAL")
						|| rsmd.getColumnTypeName(_c).equals("FLOAT")
						|| rsmd.getColumnTypeName(_c).equals("FLOAT UNSIGNED")
						) {
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(crs.getFloat(_c));
					cell.setCellStyle(cellStyleFloat);
					if(_c == columnaPorcentaje) {
						cell.setCellStyle(cellStylePercentage);
					}

				} else if ((rsmd.getColumnType(_c) == java.sql.Types.VARCHAR)
						|| (rsmd.getColumnType(_c) == java.sql.Types.CHAR)
						|| (rsmd.getColumnName(_c) == "TEXT")) {
					LOGGER.warn("Fila-Col:[" + _f + ":" + _c + "]["+ rsmd.getColumnName(_c) +"] es VARCHAR...");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					LOGGER.warn("Vamos a por el getString...");
					String value = crs.getString(_c);
					LOGGER.warn("Pasamos el getString...");
					if (value != null) {
						cell.setCellValue(value);
						if ((rsmd.getColumnName(_c)
								.equalsIgnoreCase("PRECIO_ESPECIAL"))
								&& (value.equalsIgnoreCase("SI"))) {
							rowPrecioEspecial = true;
						}
					}else{
						cell.setCellValue("");
						LOGGER.warn("Fila-Col:[" + _f + ":" + _c + "]["+ rsmd.getColumnName(_c) +"]= Valor nulo!");
					}
					cell.setCellStyle(cellStyleOther);
				

				} else if ((rsmd.getColumnType(_c) == java.sql.Types.DATE)
						|| (rsmd.getColumnTypeName(_c).equals("DATETIME"))
						|| (rsmd.getColumnType(_c) == java.sql.Types.TIMESTAMP) // 93
				) {

					Date value = crs.getDate(_c);
					final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
					if (value != null) {
						cell.setCellValue(sdf.format(value));
					}else{
						cell.setCellValue("");
						LOGGER.warn("Fila-Col:[" + _f + ":" + _c + "]["+ rsmd.getColumnName(_c) +"]= Valor nulo!");
					}
					cell.setCellStyle(cellStyleDate);

				} else if (
							(rsmd.getColumnTypeName(_c).equals("TIME"))
				) {

					Date value = crs.getTime(_c);
					final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
					if (value != null) {
						cell.setCellValue(sdf.format(value));
					}else{
						cell.setCellValue("");
						LOGGER.warn("Fila-Col:[" + _f + ":" + _c + "]["+ rsmd.getColumnName(_c) +"]= Valor nulo!");
					}
					cell.setCellStyle(cellStyleDate);

				} else if ( rsmd.getColumnType(_c) == java.sql.Types.VARBINARY 
				) {
					byte[] unosBytes = crs.getBytes(_c);
					
					//String value = obj.toString();
					String value = new String(unosBytes);
					if (value != null){
						cell.setCellValue(value);
						LOGGER.warn("java.sql.Types.VARBINARY:Fila-Col:[" + _f + ":" + _c + "]= "+ value +"!");
					}
					
						
				} else if ((rsmd.getColumnTypeName(_c).equals("VARCHAR"))) {
					LOGGER.info(ConstantsREG.SEP_VERTICAL  +  rsmd.getColumnTypeName(_c));
					String value = crs.getString(_c);
					// cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					if (value == null) {
						LOGGER.info("OJO!:En la columna "  + _c  +  ":"  +  rsmd.getColumnType(_c)  +  ":"  +  rsmd.getColumnTypeName(_c)  +  ":[**NULL**]");
						cell.setCellValue("");
					} else if(value.equals("")) {
						LOGGER.info("OJO!:En la columna "  + _c  +  ":"  +  rsmd.getColumnType(_c)  +  ":"  +  rsmd.getColumnTypeName(_c)  +  ":[****]");
						cell.setCellValue("");
					} else {
						LOGGER.info("OJO!:"  +  _c  +  ":"  +  rsmd.getColumnType(_c)  +  ":[" +  value  + "]");
						cell.setCellValue(value);
					}
					LOGGER.info("Colocamos el estilo a la celda...");
					//cell.setCellStyle(cellStyleOther);
				} else {
					LOGGER.warn("Tratamiento por defecto. Tipo Columna:#" + rsmd.getColumnTypeName(_c) + "#");
					String value = crs.getString(_c);
					if (value != null) {
						cell.setCellValue(value);
					}
						
					cell.setCellStyle(cellStyleOther);

						LOGGER.info("OJO!:"  +  rsmd.getColumnType(_c));

				}
			}// for (int _c = 1; _c <= numColumnas; _c ++ )

			if (rowPrecioEspecial) {
				// row.setRowStyle(precioEspecialStyle);
				for (int _c = 1; _c <= numColumnas; _c ++ ) {
					HSSFCell cell = row.getCell((_c - 1));
					
					if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						if(cell.getCellStyle().equals(cellStyleInteger)) {
							cell.setCellStyle(precioEspecialIntegerStyle);
							
						} else if(cell.getCellStyle().equals(cellStyleFloat)) {
							cell.setCellStyle(precioEspecialFloatStyle);
							
						}
						
					} else {
						
						cell.setCellStyle(precioEspecialStyle);
					}
					
					if(_c == columnaPorcentaje) {
						cell.setCellStyle(precioEspecialPercentageStyle);
					}
					
				}//for (int _c = 1; _c <= numColumnas; _c ++ ) {
			}//if (rowPrecioEspecial) {

			_f ++ ;
		}//while (crs.next()) {
		return _f;
	}

	/**
	 * @param rsmd
	 * @param numColumnas
	 * @throws SQLException
	 */
	private void corrigeAnchuraColumnas( 
			final HSSFSheet pestanya, 
			final ResultSetMetaData rsmd, 
			final int numColumnas) throws SQLException {
	
		LOGGER.info("Actualizamos la anchura de las columnas en la pestaña " + pestanya.getSheetName());
		
		for (int _c = 1; _c <= numColumnas; _c ++ ) {
				LOGGER.info("Anchura de las columnas: ["  +  _c  +  "] de ["  +  numColumnas  +  "] " +  rsmd.getColumnType(_c)  +  ":"  +  rsmd.getColumnName(_c));
			
			try {
				pestanya.autoSizeColumn(_c);
			} catch (Exception e) {
				LOGGER.warn("[ERROR] Al cambiar el ancho de la columna ["+ rsmd.getColumnName(_c) +"] en la pestaña " + pestanya.getSheetName()+ ": " + e.getMessage());
			}
			
		}
	}

}
