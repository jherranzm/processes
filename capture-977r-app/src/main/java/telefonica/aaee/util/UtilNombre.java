package telefonica.aaee.util;

import java.text.SimpleDateFormat;
import java.util.Date;


public class UtilNombre {

	/**
	 * 
	 * Retorna un nombre aleatorio basado en la fecha de tipo "yyyyMMdd_HHmmss_a_zzz"
	 * 
	 * @return str
	 */
	public static String nombreAleatorio(){

		final SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMdd_HHmmss_a_zzz");
		final Date day = new Date();

		return formatter.format( day );

	}
	
	/**
	 * 
	 * Retorna un nombre aleatorio basado en la fecha de tipo "yyyyMMdd_HHmmss_a_zzz"
	 * 
	 * @return str
	 */
	public static String nombreAcuerdoAleatorio(){

		final SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmss");
		final Date day = new Date();

		return "TEST" + formatter.format( day );

	}	
}
