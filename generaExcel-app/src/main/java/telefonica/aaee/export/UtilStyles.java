package telefonica.aaee.export;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;

public class UtilStyles {

	final private static Logger LOGGER = Logger.getLogger(UtilStyles.class.getCanonicalName());

	public static final String SPECIAL_PRICE = "precioEspecialStyle";
	public static final String SPECIAL_HEADER = "cabeceraPrecioEspecial";
	public static final String SPECIAL_INTEGER = "precioEspecialIntegerStyle";
	public static final String SPECIAL_PERCENTAGE = "precioEspecialPercentageStyle";
	public static final String SPECIAL_FLOAT = "precioEspecialFloatStyle";
	public static final String GENERIC_HEADER = "cabeceraGenerica";
	public static final String GENERIC_PERCENTAGE = "cellStylePercentage";
	public static final String GENERIC_DATE = "cellStyleDate";
	public static final String GENERIC_OTHER = "cellStyleOther";
	public static final String GENERIC_FLOAT = "cellStyleFloat";
	public static final String GENERIC_INTEGER = "cellStyleInteger";
	public static final String FORMAT_FLOAT = "#,###,##0.0000;[RED]-#,###,##0.0000;[GREEN]0.0000";
	public static final String FORMAT_INTEGER = "#0;[RED]-#0";
	public static final String FORMAT_PERCENTAGE = "#0.00%;[RED]-#0.00%";

	private static Map<String, CellStyle> estilos = new HashMap<String, CellStyle>();

	
	public static Map<String, CellStyle> creaEstilos(Workbook theWorkbook) {
		
		LOGGER.setLevel(Level.INFO);
		
		DataFormat format = theWorkbook.createDataFormat();
		Font fontBlanca;
		
		estilos.put(UtilStyles.GENERIC_HEADER, theWorkbook.createCellStyle());
		estilos.put(UtilStyles.SPECIAL_PRICE, theWorkbook.createCellStyle());
		estilos.put(UtilStyles.SPECIAL_FLOAT, theWorkbook.createCellStyle());
		estilos.put(UtilStyles.SPECIAL_PERCENTAGE, theWorkbook.createCellStyle());
		estilos.put(UtilStyles.SPECIAL_INTEGER, theWorkbook.createCellStyle());
		estilos.put(UtilStyles.SPECIAL_HEADER, theWorkbook.createCellStyle());
		
		estilos.put(UtilStyles.GENERIC_INTEGER, theWorkbook.createCellStyle());
		estilos.put(UtilStyles.GENERIC_FLOAT, theWorkbook.createCellStyle());
		estilos.put(UtilStyles.GENERIC_PERCENTAGE, theWorkbook.createCellStyle());
		estilos.put(UtilStyles.GENERIC_OTHER, theWorkbook.createCellStyle());
		estilos.put(UtilStyles.GENERIC_DATE, theWorkbook.createCellStyle());

		fontBlanca = theWorkbook.createFont();
		fontBlanca.setColor(HSSFColor.WHITE.index);
		fontBlanca.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
	
		estilos.get(UtilStyles.GENERIC_HEADER).setFillBackgroundColor(HSSFColor.GREEN.index);
		estilos.get(UtilStyles.GENERIC_HEADER).setFillForegroundColor(HSSFColor.GREEN.index);
		estilos.get(UtilStyles.GENERIC_HEADER).setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		estilos.get(UtilStyles.GENERIC_HEADER).setWrapText(true);
		estilos.get(UtilStyles.GENERIC_HEADER).setBorderBottom(XSSFCellStyle.BORDER_THIN);
		estilos.get(UtilStyles.GENERIC_HEADER).setBorderLeft(XSSFCellStyle.BORDER_THIN);
		estilos.get(UtilStyles.GENERIC_HEADER).setBorderTop(XSSFCellStyle.BORDER_THIN);
		estilos.get(UtilStyles.GENERIC_HEADER).setBorderRight(XSSFCellStyle.BORDER_THIN);
		estilos.get(UtilStyles.GENERIC_HEADER).setFont(fontBlanca);
		
		
		estilos.get(UtilStyles.SPECIAL_HEADER).setFillBackgroundColor(HSSFColor.DARK_BLUE.index);
		estilos.get(UtilStyles.SPECIAL_HEADER).setFillForegroundColor(HSSFColor.DARK_BLUE.index);
		estilos.get(UtilStyles.SPECIAL_HEADER).setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		estilos.get(UtilStyles.SPECIAL_HEADER).setWrapText(true);
		estilos.get(UtilStyles.SPECIAL_HEADER).setBorderBottom(XSSFCellStyle.BORDER_THIN);
		estilos.get(UtilStyles.SPECIAL_HEADER).setBorderLeft(XSSFCellStyle.BORDER_THIN);
		estilos.get(UtilStyles.SPECIAL_HEADER).setBorderTop(XSSFCellStyle.BORDER_THIN);
		estilos.get(UtilStyles.SPECIAL_HEADER).setBorderRight(XSSFCellStyle.BORDER_THIN);
		estilos.get(UtilStyles.SPECIAL_HEADER).setFont(fontBlanca);
		
		
		estilos.get(UtilStyles.GENERIC_INTEGER).setDataFormat(HSSFDataFormat.getBuiltinFormat(UtilStyles.FORMAT_INTEGER));
		estilos.get(UtilStyles.GENERIC_FLOAT).setDataFormat(HSSFDataFormat.getBuiltinFormat(UtilStyles.FORMAT_FLOAT));
		estilos.get(UtilStyles.GENERIC_OTHER).setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
		estilos.get(UtilStyles.GENERIC_DATE).setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy/MM/dd"));
		
		//
		estilos.get(UtilStyles.GENERIC_FLOAT).setDataFormat(format.getFormat(UtilStyles.FORMAT_FLOAT)); // "#.0000" 4 decimales 
		estilos.get(UtilStyles.GENERIC_PERCENTAGE).setDataFormat(format.getFormat(UtilStyles.FORMAT_PERCENTAGE)); // "#.0000" 4 decimales 

			
		estilos.get(UtilStyles.SPECIAL_PRICE).setFillBackgroundColor(HSSFColor.LIGHT_GREEN.index);
		estilos.get(UtilStyles.SPECIAL_PRICE).setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
		estilos.get(UtilStyles.SPECIAL_PRICE).setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

		estilos.get(UtilStyles.SPECIAL_FLOAT).setFillBackgroundColor(HSSFColor.LIGHT_GREEN.index);
		estilos.get(UtilStyles.SPECIAL_FLOAT).setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
		estilos.get(UtilStyles.SPECIAL_FLOAT).setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		estilos.get(UtilStyles.SPECIAL_FLOAT).setDataFormat(format.getFormat(UtilStyles.FORMAT_FLOAT)); // "#.0000" 4 decimales 
		
		estilos.get(UtilStyles.SPECIAL_INTEGER).setFillBackgroundColor(HSSFColor.LIGHT_GREEN.index);
		estilos.get(UtilStyles.SPECIAL_INTEGER).setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
		estilos.get(UtilStyles.SPECIAL_INTEGER).setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		estilos.get(UtilStyles.SPECIAL_INTEGER).setDataFormat(format.getFormat(UtilStyles.FORMAT_INTEGER)); // "#.0000" 4 decimales 

		estilos.get(UtilStyles.SPECIAL_PERCENTAGE).setFillBackgroundColor(HSSFColor.LIGHT_GREEN.index);
		estilos.get(UtilStyles.SPECIAL_PERCENTAGE).setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
		estilos.get(UtilStyles.SPECIAL_PERCENTAGE).setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		estilos.get(UtilStyles.SPECIAL_PERCENTAGE).setDataFormat(format.getFormat(UtilStyles.FORMAT_PERCENTAGE)); // "#.0000" 4 decimales 
		
		LOGGER.debug("Estilos creados!");
		
		return estilos;
	}

}
