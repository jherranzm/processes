/**
 *
 */
package telefonica.aaee.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.RowSetMetaData;
import javax.sql.rowset.CachedRowSet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import telefonica.aaee.exceptions.GeneradorExcelException;
import telefonica.aaee.utils.ConstantsREG;
import telefonica.aaee.utils.ContenidoHojaExcel;

/**
 * @author José Luis Herranz Martín
 * 
 *         05/02/2009
 * 
 */
public class GeneradorExcelXlsx {
	
	private static final String CRLF = "\n";
	final private static Logger LOGGER = Logger.getLogger(GeneradorExcelXlsx.class.getCanonicalName());
	final private static long MAX_FILAS_EXCEL = 65530;

	private static Map<String, CellStyle> estilos = new HashMap<String, CellStyle>();

	
	private String realPath = null;
	private String fullFile = null;
	private String file = null;
	private String fileIn = null;

	private String prefijoFichero = "";

	private long fileSize = 0;
	
	
	
	/**
	 * Constructor
	 * 
	 */
	public GeneradorExcelXlsx() {
		super();
		LOGGER.setLevel(Level.INFO);
	}


	private String getRango(CachedRowSet crs) {
		final String nomColumnes = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final StringBuffer nomColumna = new StringBuffer();
		long maxFila = 1;
		try {
			int indexCol = crs.getMetaData().getColumnCount();
			//LOGGER.debug("indexCol:[" + indexCol + "]");
			//LOGGER.debug("nomColumnes.length():[" + nomColumnes.length() + "]");
			if (indexCol > 0) {
				int pos1 = indexCol % nomColumnes.length();
	
				//LOGGER.debug("pos1:[" + pos1 + "]");
				int fl = (int) (indexCol / nomColumnes.length());
				//LOGGER.debug("fl:[" + fl + "]");
	
//				if (fl > 0) nomColumna.append(nomColumnes.charAt(fl - 1));
				if (fl > 0) nomColumna.append(nomColumnes.charAt(fl));
				if (pos1 > 0) nomColumna.append(nomColumnes.charAt(pos1 - 1));
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
	private void crearNombreRangoEnLibro(Workbook theWorkbook, ContenidoHojaExcel che) {
		if(che.getNumRegs() > 0){
			final String strName = che.getNombre();
			//LOGGER.debug("Nombre de la pestaña:[" + strName + "]");
			final String nomRango = getRango(che.getCrs());
			//LOGGER.debug("Nombre del rango:"  +  nomRango);
			String n11 = "'"  +  che.getNombrePestanya()  +  "'!"  +  nomRango;

			Name name = theWorkbook.createName();
			name.setNameName(strName);
			name.setRefersToFormula(n11);
			LOGGER.info("Rango:"  +  n11);
			//LOGGER.debug("Número de nombres en el libro:" + theWorkbook.getNumberOfNames());
			
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
	
	public boolean export_v2(ArrayList<ContenidoHojaExcel> ache) 
	
		throws GeneradorExcelException {
	
		boolean ret = false;
	
		final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS");
		final String sDate = formatter.format(new Date());
	
		setFile(getPrefijoFichero()  +  sDate  +  ".xlsx");
		setFullFile(getRealPath()  +  getFile());
	
		LOGGER.info("Fichero temporal:"  +  getFullFile());
	
		try {
	
			Workbook wbOutput = new SXSSFWorkbook(100);
			estilos = UtilStyles.creaEstilos(wbOutput);
			
			LOGGER.debug("Fichero y cabeceras creadas!");
				
	
			// Se crean las hojas necesarias
			
			for (ContenidoHojaExcel che : ache) {
				if (che.getNumRegs() != 0) {
					wbOutput.createSheet();
				}
			}
	
			int index = 0;
			LOGGER.info("Número de pestañas:  ache.size() = "  +  ache.size());
			for (ContenidoHojaExcel che : ache) {
				
				StringBuffer sb = new StringBuffer();
					sb.append(ConstantsREG.SEP_VERTICAL);
					sb.append(CRLF).append("Procesando la pestaña...").append(CRLF);
					sb.append("Nombre:").append(che.getNombre()).append(CRLF);
					sb.append("Nombre Pestaña:").append(che.getNombrePestanya()).append(CRLF);
					sb.append("Número de registros:").append(che.getNumRegs()).append(CRLF);
					
					LOGGER.info(sb.toString());
	
				if (che.getNumRegs() == 0) {
					
					//Esta hoja no se añade, ya que no hay contenido
					
				} else if (che.getNumRegs() <= MAX_FILAS_EXCEL) {
					Sheet hojaActual = wbOutput.getSheetAt(index);
	
					if(index > 0 ) {
						 hojaActual.setZoom(3, 4); // Zoom del 75%
					}
					hojaActual.createFreezePane(
							0, (int)(che.getNumFilaInicial()  +  1), 
							0, (int)(che.getNumFilaInicial()  +  1));

					// Ponemos el nombre de la pestaña
					wbOutput.setSheetName(index, che.getNombrePestanya());
					
					// Creamos el nombre en el libro..
					crearNombreRangoEnLibro(wbOutput, che);

					// rellenamos las cabeceras y los datos..
					ret = fillExcelSheet_especial_filaInicial(
							hojaActual,
							che.getNumFilaInicial(), 
							che.getCrs());
					
					index ++ ;
					
				} else {

					LOGGER.warn("En la pestaña "  + che.getNombre() + " se ha producido un error, ya que hay "  +  che.getNumRegs()  +  " registros.");
					
				} //if (che.getNumRegs() == 0) 
			} //for (ContenidoHojaExcel che : ache)
			
			
			FileOutputStream fileOut = new FileOutputStream(getFullFile());
			wbOutput.write(fileOut);
			LOGGER.info("Guardado el fichero : " + getFullFile());
			fileOut.close();
			LOGGER.info("Cerrado el fichero : " + getFullFile());
			
			File file = new File(getFullFile());
			setFileSize(file.length());
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			LOGGER.warn("Se ha producido una excepción de SQL");
			LOGGER.info(ConstantsREG.SEP_VERTICAL);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.warn("Se ha producido una excepción Genérica...");
			LOGGER.info(ConstantsREG.SEP_VERTICAL);
			System.exit(1);
		} finally {
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
	 * @throws GeneradorExcelException 
		 */
		private boolean fillExcelSheet_especial_filaInicial(
				Sheet laPestanyaActual,
				final long filaInicial, 
				final CachedRowSet crs) 
		
			throws SQLException, GeneradorExcelException {
			
			int columnaPorcentaje = 999;
		
			ResultSetMetaData rsmd = (RowSetMetaData) crs.getMetaData();
			
			int numColumnas = rsmd.getColumnCount();
		
			int _f = (int)filaInicial; // fila inicial
			
			columnaPorcentaje = filaCabeceras(laPestanyaActual, columnaPorcentaje, rsmd, numColumnas, _f);
			
			corrigeAnchuraColumnas(laPestanyaActual, rsmd, numColumnas);
			
			_f ++ ;
			
			LOGGER.info("Se van a tratar "  +  crs.size()  +  " registros...");
		
			_f = filasDatos(laPestanyaActual, crs, columnaPorcentaje, rsmd, numColumnas, _f);
			
			corrigeAnchuraColumnasFin(laPestanyaActual, rsmd, numColumnas);

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
	private int filaCabeceras( 
				final Sheet pestanya
				, int columnaPorcentaje
				, final ResultSetMetaData rsmd
				, final int numColumnas
				, final int _f)
			throws SQLException {
		
		
		Row row = pestanya.createRow(_f);
		for (int _c = 1; _c <= numColumnas; _c ++ ) {
			
			Cell cell = row.createCell(_c - 1);
			cell.setCellStyle(estilos.get(UtilStyles.GENERIC_HEADER));
			
			String tmpCabecera = rsmd.getColumnName(_c).replaceAll("_", " ").toUpperCase();
			LOGGER.debug(rsmd.getColumnName(_c)  +  " -> "  +  tmpCabecera);
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
				cell.setCellStyle(estilos.get(UtilStyles.SPECIAL_HEADER));
				//LOGGER.debug("Columna especial:"  +  rsmd.getColumnName(_c));
			}
			if(rsmd.getColumnName(_c).equalsIgnoreCase("PORCENTAJE")) {
				columnaPorcentaje = _c;
			}else if(rsmd.getColumnName(_c).equalsIgnoreCase("PORCENTAJE_DESCUENTO")) {
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
	 * @throws GeneradorExcelException 
	 */
	private int filasDatos(
			Sheet laPestanyaActual,
			final CachedRowSet crs, 
			final int columnaPorcentaje, 
			final ResultSetMetaData rsmd, 
			final int numColumnas, 
			int _f)
			throws SQLException {
		
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		final SimpleDateFormat hora = new SimpleDateFormat("hh:mm:ss");
		
		Row row;

		while (crs.next()) {
			row = laPestanyaActual.createRow(_f);
			boolean rowPrecioEspecial = false;

			// Recorremos las columnas
			for (int _c = 1; _c <= numColumnas; _c ++ ) {
				
				//LOGGER.debug("Columna: " + _c);

				Cell cell = row.createCell((_c - 1)); // poi.3.6
				
				//LOGGER.debug("rsmd.getColumnType(_c):" + rsmd.getColumnType(_c));
				
				if (rsmd.getColumnType(_c) == java.sql.Types.INTEGER) {
					cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(crs.getInt(_c));
					cell.setCellStyle(estilos.get(UtilStyles.GENERIC_INTEGER));

				} else if (rsmd.getColumnType(_c) == java.sql.Types.BIGINT) {
					cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(crs.getLong(_c));
					cell.setCellStyle(estilos.get(UtilStyles.GENERIC_INTEGER));
					//LOGGER.debug("BIGINT: rsmd.getColumnType(_c):" + rsmd.getColumnName(_c));

				} else if (rsmd.getColumnType(_c) == java.sql.Types.FLOAT
						|| rsmd.getColumnType(_c) == java.sql.Types.DOUBLE
						|| rsmd.getColumnTypeName(_c).equals("DECIMAL")
						|| rsmd.getColumnTypeName(_c).equals("FLOAT")
						|| rsmd.getColumnTypeName(_c).equals("FLOAT UNSIGNED")
						) {
					cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(crs.getFloat(_c));
					cell.setCellStyle(estilos.get(UtilStyles.GENERIC_FLOAT));
					if(_c == columnaPorcentaje) {
						cell.setCellStyle(estilos.get(UtilStyles.GENERIC_PERCENTAGE));
						//LOGGER.debug("Porcentaje: rsmd.getColumnType(_c):" + rsmd.getColumnName(_c));
					}

				} else if ((rsmd.getColumnType(_c) == java.sql.Types.VARCHAR)
						|| (rsmd.getColumnType(_c) == java.sql.Types.CHAR)
						|| (rsmd.getColumnName(_c) == "TEXT")) {
					//LOGGER.debug("Fila-Col:[" + _f + ":" + _c + "]["+ rsmd.getColumnName(_c) +"] es VARCHAR...");
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					//LOGGER.debug("Vamos a por el getString...");
					String value = crs.getString(_c);
					//LOGGER.debug("Pasamos el getString...");
					if (value != null) {
						cell.setCellValue(value);
						if ((rsmd.getColumnName(_c)
								.equalsIgnoreCase("PRECIO_ESPECIAL"))
								&& (value.equalsIgnoreCase("SI"))) {
							rowPrecioEspecial = true;
						}
					}else{
						cell.setCellValue("");
						//LOGGER.debug("Fila-Col:[" + _f + ":" + _c + "]["+ rsmd.getColumnName(_c) +"]= Valor nulo!");
					}
					cell.setCellStyle(estilos.get(UtilStyles.GENERIC_OTHER));
				

				} else if ((rsmd.getColumnType(_c) == java.sql.Types.DATE)
						|| (rsmd.getColumnTypeName(_c).equals("DATETIME"))
						|| (rsmd.getColumnType(_c) == java.sql.Types.TIMESTAMP) // 93
				) {

					Date value = crs.getDate(_c);
					if (value != null) {
						cell.setCellValue(sdf.format(value));
					}else{
						cell.setCellValue("");
						//LOGGER.debug("Fila-Col:[" + _f + ":" + _c + "]["+ rsmd.getColumnName(_c) +"]= Valor nulo!");
					}
					cell.setCellStyle(estilos.get(UtilStyles.GENERIC_DATE));

				} else if (
							(rsmd.getColumnTypeName(_c).equals("TIME"))
				) {

					Date value = crs.getTime(_c);
					if (value != null) {
						cell.setCellValue(hora.format(value));
					}else{
						cell.setCellValue("");
						//LOGGER.debug("Fila-Col:[" + _f + ":" + _c + "]["+ rsmd.getColumnName(_c) +"]= Valor nulo!");
					}
					cell.setCellStyle(estilos.get(UtilStyles.GENERIC_DATE));

				} else if ( rsmd.getColumnType(_c) == java.sql.Types.VARBINARY 
				) {
					byte[] unosBytes = crs.getBytes(_c);
					
					//String value = obj.toString();
					String value = new String(unosBytes);
					if (value != null){
						cell.setCellValue(value);
						//LOGGER.debug("java.sql.Types.VARBINARY:Fila-Col:[" + _f + ":" + _c + "]= "+ value +"!");
					}
					
						
				} else if ((rsmd.getColumnTypeName(_c).equals("VARCHAR"))) {
					//LOGGER.debug(ConstantsREG.SEP_VERTICAL  +  rsmd.getColumnTypeName(_c));
					String value = crs.getString(_c);
					// cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					if (value == null) {
						//LOGGER.debug("OJO!:En la columna "  + _c  +  ":"  +  rsmd.getColumnType(_c)  +  ":"  +  rsmd.getColumnTypeName(_c)  +  ":[**NULL**]");
						cell.setCellValue("");
					} else if(value.equals("")) {
						//LOGGER.debug("OJO!:En la columna "  + _c  +  ":"  +  rsmd.getColumnType(_c)  +  ":"  +  rsmd.getColumnTypeName(_c)  +  ":[****]");
						cell.setCellValue("");
					} else {
						//LOGGER.debug("OJO!:"  +  _c  +  ":"  +  rsmd.getColumnType(_c)  +  ":[" +  value  + "]");
						cell.setCellValue(value);
					}
					//LOGGER.debug("Colocamos el estilo a la celda...");
					//cell.setCellStyle(estilos.get("cellStyleOther"));
				} else {
					//LOGGER.debug("Tratamiento por defecto. Tipo Columna:#" + rsmd.getColumnTypeName(_c) + "#");
					String value = crs.getString(_c);
					if (value != null) {
						cell.setCellValue(value);
					}
						
					cell.setCellStyle(estilos.get(UtilStyles.GENERIC_OTHER));

					//LOGGER.debug("OJO!:"  +  rsmd.getColumnType(_c));

				}
			}// for (int _c = 1; _c <= numColumnas; _c ++ )

			if (rowPrecioEspecial) {

				for (int _c = 1; _c <= numColumnas; _c ++ ) {
					Cell cell = row.getCell((_c - 1));
					
					if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
						if(cell.getCellStyle().equals(estilos.get(UtilStyles.GENERIC_INTEGER))) {
							cell.setCellStyle(estilos.get(UtilStyles.SPECIAL_INTEGER));
							
						} else if(cell.getCellStyle().equals(estilos.get(UtilStyles.GENERIC_FLOAT))) {
							cell.setCellStyle(estilos.get(UtilStyles.SPECIAL_FLOAT));
							
						}
						
					} else {
						
						cell.setCellStyle(estilos.get(UtilStyles.SPECIAL_PRICE));
					}
					
					if(_c == columnaPorcentaje) {
						cell.setCellStyle(estilos.get(UtilStyles.SPECIAL_PERCENTAGE));
					}
					
				}//for (int _c = 1; _c <= numColumnas; _c ++ ) {
			}//if (rowPrecioEspecial) {

			_f ++ ;
			
			// Liberamos memoria...
			crs.deleteRow();

		}//while (crs.next()) {
		return _f;
	}

	/**
	 * @param rsmd
	 * @param numColumnas
	 * @throws SQLException
	 */
	private void corrigeAnchuraColumnas( 
			final Sheet pestanya, 
			final ResultSetMetaData rsmd, 
			final int numColumnas) throws SQLException {
		
		int pixelsWitdh = 12;
	
		LOGGER.info("Actualizamos la anchura de las columnas en la pestaña " + pestanya.getSheetName());
		
		for (int _c = 1; _c <= numColumnas; _c ++ ) {
				
			LOGGER.debug("Anchura de las columnas: ["  +  _c  +  "] de ["  +  numColumnas  +  "] " +  rsmd.getColumnType(_c)  +  ":"  +  rsmd.getColumnName(_c));
			
			try {
				LOGGER.debug("Nombre del campo:" + rsmd.getColumnName(_c));
				pestanya.autoSizeColumn(_c);
				
				LOGGER.debug("Anchura de la columna:" + pestanya.getColumnWidth(_c));
				// Las palabras del nombre de la columna
				String[] words = rsmd.getColumnName(_c).split("[_ ]");
				int max = 0;
				for(String word : words){
					max = (word.length() > max ? word.length() : max);
					LOGGER.debug(word + ":" + max);
				}
				LOGGER.debug(max + "->" + Math.floor(((max * pixelsWitdh + 15) /  pixelsWitdh * 256)) + "->" + (pestanya.getColumnWidth(_c)));
				LOGGER.debug(max + "->" + Math.floor(((max * pixelsWitdh + 15) /  pixelsWitdh * 256)/256) + "->" + (pestanya.getColumnWidth(_c)/256));
				LOGGER.debug(max + "->" + Math.floor(( (max + 3) * 256 )) + "->" + (pestanya.getColumnWidth(_c)));
				//if( Math.floor(((max * pixelsWitdh + 15) /  pixelsWitdh * 256) / 256)  > (pestanya.getColumnWidth(_c)/256) ){
				if( ( (max + 4) * 256 )  > pestanya.getColumnWidth(_c) ){
					LOGGER.debug("Cambio de anchura!");
					pestanya.setColumnWidth(_c,    (max + 4) * 256  );
				}
				LOGGER.debug("Anchura de la columna:" + pestanya.getColumnWidth(_c));
			} catch (Exception e) {
				LOGGER.warn("[ERROR] Al cambiar el ancho de la columna ["+ rsmd.getColumnName(_c) +"] en la pestaña " + pestanya.getSheetName()+ ": " + e.getMessage());
			}
			
		}
	}

	/**
	 * @param rsmd
	 * @param numColumnas
	 * @throws SQLException
	 */
	private void corrigeAnchuraColumnasFin( 
			final Sheet pestanya, 
			final ResultSetMetaData rsmd, 
			final int numColumnas) throws SQLException {
		
		LOGGER.info("Actualizamos la anchura de las columnas en la pestaña " + pestanya.getSheetName());
		
		for (int _c = 1; _c <= numColumnas; _c ++ ) {
				
			LOGGER.debug("Anchura de las columnas: ["  +  _c  +  "] de ["  +  numColumnas  +  "] " +  rsmd.getColumnType(_c)  +  ":"  +  rsmd.getColumnName(_c));
			
			int ancho = pestanya.getColumnWidth(_c);
			
			try {
				pestanya.autoSizeColumn(_c);
				
				if( ancho  > pestanya.getColumnWidth(_c) ){
					LOGGER.debug("Cambio de anchura!");
					pestanya.setColumnWidth(_c,    ancho  );
				}
				LOGGER.debug("Anchura de la columna:" + pestanya.getColumnWidth(_c));
			} catch (Exception e) {
				LOGGER.warn("[ERROR] Al cambiar el ancho de la columna ["+ rsmd.getColumnName(_c) +"] en la pestaña " + pestanya.getSheetName()+ ": " + e.getMessage());
			}
			
		}
	}

}
