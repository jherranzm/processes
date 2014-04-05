/**
 *
 */
package telefonica.aaee.export;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.sql.RowSetMetaData;
import javax.sql.rowset.CachedRowSet;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * @author José Luis Herranz Martín
 * 
 *         05/02/2009
 * 
 */
public class ExportToExcel {

	final private boolean DEBUG = false;
	// private String[][] datos;
	private String realPath = null;
	private String fullFile = null;
	private String file = null;
	private String fileIn = null;
	private String nombreHoja = "Hoja1";

	private FileOutputStream fileOut = null;
	private Logger logger = Logger.getLogger(this.getClass()
			.getCanonicalName());

	private String xlsFileOrig = "PeticionesPorDia.xls";
	private String prefijoFichero = "PeticionesPorDia_";

	// private int totalColumnas = 30;
	// private final int maxFilasExcel = 30000; // 2006/08/23:Para solucionar
	// problemas al leer ficheros muy grandes

	/**
	 * Constructor
	 * 
	 */
	public ExportToExcel() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param path
	 * 
	 */
	public ExportToExcel(String path) {
		super();
		setRealPath(path);
		setFileIn(getRealPath() + xlsFileOrig);
	}

	/**
	 * Constructor
	 * 
	 * @param path
	 * @param template
	 * 
	 */
	public ExportToExcel(String path, String template) {
		super();
		setRealPath(path);
		setFileIn(getRealPath() + template);
	}

	/**
	 * @param rs
	 * @return true or false
	 */
	// public boolean export( CachedRowSetImpl crs){
	public boolean export(CachedRowSet crs) {

		boolean ret = false;

		try {
			final SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyyMMdd-HHmmss-SSS");
			final String sDate = formatter.format(new Date());
			setFile(getPrefijoFichero() + sDate + ".xls");
			setFullFile(getRealPath() + getFile());

			logger.info("Fichero temporal:" + getFullFile());

			fileOut = new FileOutputStream(getFullFile());

			POIFSFileSystem doc = new POIFSFileSystem(new FileInputStream(
					getFileIn()));
			final HSSFWorkbook wbOutput = new HSSFWorkbook(doc);
			// final HSSFSheet sheet1 = wbOutput.getSheet("PeticionesPorDia");
			final HSSFSheet sheet1 = wbOutput.getSheetAt(0);
			sheet1.setZoom(3, 4); // Zoom del 75%

			wbOutput.setSheetName(0, getNombreHoja());

			final HSSFCellStyle headerStyle = wbOutput.createCellStyle();
			final HSSFCellStyle cellStyle = wbOutput.createCellStyle();

			final HSSFFont font = wbOutput.createFont();
			font.setColor(HSSFColor.WHITE.index);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			headerStyle.setFillBackgroundColor(HSSFColor.BLUE.index);
			headerStyle.setFillForegroundColor(HSSFColor.BLUE.index);
			headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			headerStyle.setWrapText(true);
			headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			headerStyle.setFont(font);

			if (DEBUG)
				logger.info("Fichero y cabeceras creadas!");

			sheet1.createFreezePane(0, 1, 0, 1);

			if (crs != null) {

				ret = fillExcelSheet(crs, sheet1, headerStyle, cellStyle);

			} else {

				logger.severe("El ResultSet está vacío!");

			}

			wbOutput.write(fileOut);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fileOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return ret;

	}

	/**
	 * @param crs
	 * @param sheet1
	 * @param headerStyle
	 * @param cellStyle
	 * @return true or false
	 * @throws SQLException
	 */
	private boolean fillExcelSheet(CachedRowSet crs, final HSSFSheet sheet1,
			final HSSFCellStyle headerStyle, final HSSFCellStyle cellStyle)
			throws SQLException {

		boolean ret;
		ResultSetMetaData rsmd = (RowSetMetaData) crs.getMetaData(); //

		int numColumnas = rsmd.getColumnCount();

		int _f = 0; // fila inicial
		HSSFRow row = sheet1.createRow(_f);
		for (int _c = 1; _c <= numColumnas; _c++) {
			// HSSFCell cell = row.createCell((_c-1)); // poi.3.6
			HSSFCell cell = row.createCell(_c - 1);
			cell.setCellStyle(headerStyle);
			cell.setCellValue(rsmd.getColumnName(_c));
			if (DEBUG)
				logger.info("Column: " + _c + ":" + rsmd.getColumnName(_c));
			if (DEBUG)
				logger.info("" + cell.getStringCellValue());
			if (DEBUG)
				logger.info("Row:" + row.getRowNum() + "\tCell:"
						+ cell.getColumnIndex());
		}
		_f++;

		HSSFCellStyle cellStyleInteger = cellStyle;
		HSSFCellStyle cellStyleFloat = cellStyle;
		HSSFCellStyle cellStyleOther = cellStyle;
		HSSFCellStyle cellStyleDate = cellStyle;

		cellStyleInteger.setDataFormat(HSSFDataFormat.getBuiltinFormat("#0"));
		cellStyleFloat.setDataFormat(HSSFDataFormat
				.getBuiltinFormat("#.##0,0000"));
		cellStyleOther.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
		cellStyleDate.setDataFormat(HSSFDataFormat
				.getBuiltinFormat("yyyy/MM/dd"));

		// Recorremos las filas
		while (crs.next()) {
			row = sheet1.createRow(_f);

			// Recorremos las columnas
			for (int _c = 1; _c <= numColumnas; _c++) {
				// final HSSFCell cell = row.createCell((short)(_c-1));
				HSSFCell cell = row.createCell((_c - 1)); // poi.3.6

				if (DEBUG)
					logger.info("Tipo de Columna:" + rsmd.getColumnType(_c)
							+ ":" + rsmd.getColumnName(_c));

				if (rsmd.getColumnType(_c) == java.sql.Types.INTEGER) {
					int value = crs.getInt(_c);
					// cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#0"));
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(value);
					cell.setCellStyle(cellStyleInteger);

				} else if (rsmd.getColumnType(_c) == java.sql.Types.BIGINT) {
					long value = crs.getLong(_c);
					// cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.##0,00"));
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(value);
					cell.setCellStyle(cellStyleFloat);

				} else if (rsmd.getColumnType(_c) == java.sql.Types.FLOAT) {
					float value = crs.getFloat(_c);
					// cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.##0,00"));
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(value);
					cell.setCellStyle(cellStyleFloat);

				} else if (rsmd.getColumnType(_c) == java.sql.Types.DOUBLE) {
					float value = crs.getFloat(_c);
					// cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.##0,00"));
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(value);
					cell.setCellStyle(cellStyleFloat);

				} else if (rsmd.getColumnType(_c) == java.sql.Types.VARCHAR) {
					String value = crs.getString(_c);
					// cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					if (value != null)
						cell.setCellValue(value);
					cell.setCellStyle(cellStyleOther);

				} else if (rsmd.getColumnType(_c) == java.sql.Types.CHAR) {
					String value = crs.getString(_c);
					// cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					if (value != null)
						cell.setCellValue(value);
					cell.setCellStyle(cellStyleOther);

				} else if (rsmd.getColumnName(_c) == "TEXT") {
					String value = crs.getString(_c);
					// cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					if (value != null)
						cell.setCellValue(value);
					cell.setCellStyle(cellStyleOther);

				} else if ((rsmd.getColumnType(_c) == java.sql.Types.DATE)
						|| (rsmd.getColumnName(_c) == "DATETIME")
						|| (rsmd.getColumnType(_c) == java.sql.Types.TIMESTAMP) // 93
				) {

					Date value = crs.getDate(_c);
					// cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("dd-MM-yyyy"));
					cellStyle.setDataFormat(HSSFDataFormat
							.getBuiltinFormat("yyyy/MM/dd"));
					final SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy/MM/dd");
					if (value != null)
						cell.setCellValue(sdf.format(value));
					cell.setCellStyle(cellStyleDate);

				} else {
					String value = crs.getString(_c);
					cellStyle.setDataFormat(HSSFDataFormat
							.getBuiltinFormat("@"));
					if (value != null)
						cell.setCellValue(value);
					cell.setCellStyle(cellStyleOther);

					if (DEBUG)
						logger.info("OJO!:" + rsmd.getColumnType(_c));

				}
				// cell.setCellStyle(cellStyle);
			}
			_f++;
		}

		ret = true;
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

}
