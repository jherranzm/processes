package telefonica.aaee.informes.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class ReadHzFile {
	protected final Log logger = LogFactory.getLog(getClass());

	private File HZFile = null;
	private String[][] datos;
	private int totalFilas = 0;
	private int totalColumnas = 30;

	private String nombreFicheroExcel = null;
	private long tamanyFicheroExcel = 0;
	private String realPath = null;
	private FileOutputStream fileOut = null;
	private final int maxFilasExcel = 30000; // 2006/08/23:Para solucionar
	// problemas al leer ficheros muy
	// grandes

	// Partimos el docidlargo en sus partes
	// en función del largo del mismo,
	// interpretamos si viene de DIRECTA o bien de UNIFICADA.
	// UNIFICADA 22
	// DIRECTA 10
	// aUnificada = {"*", "FECHA", "CIF", "NumComercial", "Factura", "AGF",
	// "Importe", "*", "Titular", "Tipo_DOC", "CUC", "*", "*", "AGFOrigen", "*",
	// "*", "*", "*", "*", "*", "*", "*", "*"}
	// aDirecta = {"*", "*", "FECHA", "CIF", "NumComercial", "Factura",
	// "Referencia", "BaseImponible", "ImporteTotal", "Titular", "Facturador"}
	private final String[] aUnificada = { "*", "FECHA", "CIF", "NumComercial",
			"Factura", "AGF", "Importe", "*", "Titular", "Tipo_DOC", "CUC",
			"*", "*", "AGFOrigen", "*", "*", "*", "*", "*", "*", "*", "*", "*" };
	private final String[] aDirecta = { "*", "*", "FECHA", "CIF",
			"NumComercial", "Factura", "Referencia", "BaseImponible",
			"ImporteTotal", "Titular", "Facturador", "servicio" };
	// 2008-09-22: Modificacion para que funcione la carpeta de CIRCUITOS
	// Problema: DIRECTA y CIRCUITOS tienen el mismo número de columnas en
	// docidlargo
	private final String[] aCircuitos = { "*", "*", "CIF", "NumComercial",
			"Factura", "Referencia", "*", "ImporteTotal", "*", "Titular", "*",
			"servicio" };

	/**
	 * Constructor
	 * 
	 */
	public ReadHzFile() {

		datos = new String[maxFilasExcel][totalColumnas];

	}

	public ReadHzFile(String rp) {
		realPath = rp;
		datos = new String[maxFilasExcel][totalColumnas];
	}

	/**
	 * @return true or false
	 */
	public boolean execute() {

		boolean ret = false;
		if (readHZFile(HZFile)) {
			logger.info("[INFO]ReadNewHZFile.readHZFile() OK");

			ret = true;

		} else {
			logger.fatal("[ERROR]ReadNewHZFile.readHZFile() error");
		}

		return ret;
	}

	/**
	 * Llegeix les macros d'un determinat fitxer
	 * 
	 * @param filename
	 *            - Fitxer d'on es llegeixen les Macros
	 * @return boolean - S'ha pogut llegir o no
	 */
	private boolean readHZFile(File f) {

		boolean ret = false;
		int numColumnasExcel = 0;
		logger.info("[INFO]ReadNewHZFile.readHZFile().HZFile:" + HZFile);

		BufferedReader in;
		if (!f.exists()) {
			logger.info("[ERROR]ReadNewHZFile: No existeix... " + HZFile
					+ " !!!");
		} else {

			logger.info("[INFO]ReadNewHZFile.readHZFile().f.exists():" + HZFile);

			try {
				in = new BufferedReader(new FileReader(HZFile));
				String line;
				int fila = 0;
				line = in.readLine();

				/**
				 * Es llegeix el fitxer línia per línia
				 */

				while (true) {

					if (line == null) {
						break;
					}

					logger.info("[INFO]ReadNewHZFile.readHZFile().line:" + line);

					final Pattern pattern1 = Pattern
							.compile("<input value=\"(.*)\" name=\"docidlargo\" type=\"hidden\"/>");
					final Matcher matcher1 = pattern1.matcher(line);
					final boolean matchFound1 = matcher1.matches();

					// Busquem "listaFacturasParaBotones"
					if (matchFound1) {
						logger.info("[INFO]ReadNewHZFile.readHZFile().matchFound!");

						final String strValue = new String(matcher1.group(1));

						logger.info("value:" + strValue);
						// datos[fila][0]="docidlargo";
						// datos[fila][1]=strValue;
						String[] columnas;
						String[] result = strValue.split("&#1;");
						totalColumnas = result.length - 1;
						logger.info("totalColumnas:" + totalColumnas);
						if (totalColumnas < 20) {
							// 2008-09-22: Tenemos que decidir que estructura
							// aplicar,
							// Lo hacemos en funcion del valor de la 3a columna,
							// si empieza por L o D
							// sera un CIF/NIF, y aplicaremos aCircuitos
							logger.info("result[2].substring(0, 1)="
									+ result[2].substring(0, 1));
							if (result[2].substring(0, 1).equals("L")
									|| result[2].substring(0, 1).equals("D")) {
								logger.info("Aplicado aCircuitos");
								columnas = (String[]) aCircuitos.clone();
							} else {
								logger.info("Aplicada aDirecta");
								columnas = (String[]) aDirecta.clone();
							}

						} else {
							columnas = (String[]) aUnificada.clone();
							logger.info("columnas:" + columnas.length);
						}// if(totalColumnas <20)

						int idxColumns = 0;
						if (fila == 0) {
							for (int idx = 0; idx < columnas.length; idx++) {
								if (columnas[idx] != "*") {
									logger.info("columnas:" + columnas[idx]);
									datos[fila][idxColumns] = columnas[idx];
									idxColumns++;
								}
							}
							fila++;
							if (idxColumns > numColumnasExcel)
								numColumnasExcel = idxColumns;
						} else {
							logger.info("fila != 0;");
						}// if(fila == 0)

						logger.info("idxColumns=" + idxColumns);
						idxColumns = 0;

						for (int x = 0; x < totalColumnas; x++) {
							// Rellenamos las columnas
							logger.info(x + "/" + totalColumnas + "\tresult["
									+ x + "]=**" + result[x] + "**");
							// Tratamiento de las fechas
							if (columnas[x] == "FECHA") {

								String date_out;
								Calendar cal = Calendar.getInstance();
								SimpleDateFormat fSDateFormat = new SimpleDateFormat(
										"dd/MM/yyyy");
								long l = Long.parseLong(result[x].trim());
								l = (l - 1) * 24 * 3600 * 1000; // paso a
								// millisegundos
								cal.setTimeInMillis(l);
								date_out = fSDateFormat.format(cal.getTime());
								datos[fila][idxColumns] = date_out;
								idxColumns++;
							} else if (columnas[x] != "*") {

								datos[fila][idxColumns] = result[x];
								idxColumns++;
							}// if(columnas[x]=="FECHA")

						}// for (int x=0; x<totalColumnas; x++)

						logger.info("totalFilas = fila;" + fila);
						fila++;
						totalFilas = fila;
					}// if(matchFound1)

					line = in.readLine();
				}// while(true)

				ret = true;

				in.close();
				logger.info("totalFilas:" + totalFilas);
				logger.info("totalColumnas:" + totalColumnas);

				crearFicheroExcel(numColumnasExcel);

			} catch (final IOException ioe) {
				logger.fatal("[ERROR]ReadNewHZFile:IOException: " + ioe);
			}

		}// if

		return ret;

	}

	/**
	 * @param numColumnasExcel
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void crearFicheroExcel(int numColumnasExcel)
			throws FileNotFoundException, IOException {
		final HSSFWorkbook wbOutput = new HSSFWorkbook();

		final SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyyMMddhhmmssSSS");

		final HSSFSheet sheet1 = wbOutput.createSheet();
		sheet1.setZoom(3, 4); // 75% Zoom

		final HSSFCellStyle headerStyle = wbOutput.createCellStyle();
		final HSSFCellStyle cellStyle = wbOutput.createCellStyle();
		final HSSFCellStyle cellStyle_Borrar = wbOutput.createCellStyle();

		cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));

		headerStyle.setFillBackgroundColor(HSSFColor.BLUE.index);
		headerStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		headerStyle.setWrapText(true);
		headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);

		cellStyle_Borrar.setFillBackgroundColor(HSSFColor.BRIGHT_GREEN.index);
		cellStyle_Borrar
				.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		cellStyle_Borrar.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		final HSSFFont font = wbOutput.createFont();
		font.setColor(HSSFColor.WHITE.index);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerStyle.setFont(font);

		sheet1.createFreezePane(0, 1, 0, 1);

		String cif = "";

		short _fila = 0;
		for (int _f = 0; _f < totalFilas; _f++) {
			boolean borrarFila = false;
			final HSSFRow row = sheet1.createRow((short) _fila);

			for (int _c = 0; _c < numColumnasExcel; _c++) {
				logger.info(_c + ":" + _f + ":" + datos[_f][_c]);

				final HSSFCell cell = row.createCell((int) _c);

				if (_f == 0) {
					cell.setCellStyle(headerStyle);
				} else {
					cell.setCellStyle(cellStyle);
				}// if(_f == 0)

				final Pattern pattern_importe = Pattern.compile("Importe");
				final Matcher matcher_importe = pattern_importe
						.matcher(datos[0][_c]);

				final Pattern pattern_tipo_doc = Pattern.compile("Tipo_DOC");
				final Matcher matcher_tipo_doc = pattern_tipo_doc
						.matcher(datos[0][_c]);

				final Pattern pattern_cif = Pattern.compile("CIF");
				final Matcher matcher_cif = pattern_cif.matcher(datos[0][_c]);

				if (matcher_importe.lookingAt() && (_f > 0)) {

					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					final DecimalFormat df = new DecimalFormat("##0.00");
					String _t = datos[_f][_c].trim();
					_t = _t.replace(',', '.');
					logger.info(_t); // no funciona
					final float fl = Float.valueOf(_t).floatValue();
					logger.info(df.format(fl)); // no funciona
					logger.info("float fl = " + fl);
					// cell.setCellValue(Float.parseFloat(datos[_f][_c]));
					cell.setCellValue(fl);
					// cell.setCellValue(df.format(fl));
				} else if (matcher_tipo_doc.lookingAt() && (_f > 0)) {

					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					String _t = datos[_f][_c].trim();
					cell.setCellValue(_t);
					if (_t.equals("RESUMEN-NUM-ADMIN-TD")
							|| _t.equals("RESUMEN-LINEA")
							|| _t.equals("AVISO-PAGO")
							|| _t.equals("CARTA-ENSOBRADO-CONJUNTO")
							|| _t.equals("RESUMEN-CONSUMO")
							|| _t.equals("DETALLE-LINEA")
							|| _t.equals("ANEXO-CONC-FIJO")
							|| _t.equals("ANEXO")
							|| _t.equals("DETALLE-DIRECTA-TDE")
							|| _t.equals("DETALLE-NO-EDITABLE")) {
						borrarFila = true;
						cell.setCellStyle(cellStyle_Borrar);
					}
				} else if (matcher_cif.lookingAt() && (_f > 0)
						&& (cif.equals(""))) {

					cif = datos[_f][_c].trim();
					cell.setCellValue(cif);

				} else {
					logger.info("datos[" + _f + "][" + _c + "]="
							+ datos[_f][_c]);
					if (datos[_f][_c].equals("&#2;"))
						datos[_f][_c] = "";
					cell.setCellValue(datos[_f][_c]);
				}// if(matcher3.lookingAt() && (_f > 0))

			}// for(int _c = 0; _c<numColumnasExcel; _c++)

			if (borrarFila) {
				// Borramos la fila si es un tipo de documento que no aporta
				// info...
				sheet1.removeRow(row);
				// sheet1.shiftRows(row.getRowNum(), row.getRowNum(), -1); // la
				// línea y la anterior
				// sheet1.shiftRows(row.getRowNum()+1, row.getRowNum()+1, -1);
				// // borra la línea
				// row.setRowStyle(cellStyle_Borrar);
			} else {
				_fila++;
			}
		}// for(int _f = 0; _f < totalFilas; _f++)

		for (int _c = 0; _c < numColumnasExcel; _c++) {
			sheet1.autoSizeColumn((short) _c); // adjust width
		}

		final String s = cif + "_" + formatter.format(new Date());
		final String temp_file = getRealPath() + s + ".xls";

		logger.info("Fichero temporal:" + temp_file);

		fileOut = new FileOutputStream(temp_file);
		wbOutput.write(fileOut);
		fileOut.close();
		File file = new File(temp_file);

		setTamanyFicheroExcel(file.length());
		setNombreFicheroExcel(s + ".xls");
	}

	/**
	 * @return Returns the HZFile.
	 */
	public File getHZFile() {
		return HZFile;
	}

	/**
	 * @param HZFile
	 *            The HZFile to set.
	 */
	public void setHZFile(File HZFile) {
		this.HZFile = HZFile;
	}

	/**
	 * @return Returns the realPath.
	 */
	public String getRealPath() {
		return realPath;
	}

	/**
	 * @param realPath
	 *            The realPath to set.
	 */
	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}

	public String getNombreFicheroExcel() {
		return nombreFicheroExcel;
	}

	public void setNombreFicheroExcel(String nombreFicheroExcel) {
		this.nombreFicheroExcel = nombreFicheroExcel;
	}

	public long getTamanyFicheroExcel() {
		return tamanyFicheroExcel;
	}

	public void setTamanyFicheroExcel(long tamanyFicheroExcel) {
		this.tamanyFicheroExcel = tamanyFicheroExcel;
	}
}
