package telefonica.aaee.informes.helpers;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileHelper {

	private static final Log logger = LogFactory.getLog(FileHelper.class.getCanonicalName());
	
	public static String createUploadDirIfNotExists(String tempDir) {
		/**
		 * Si no está la carpeta del día creada, se crea con el formato yyyy-MM-dd
		 */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		String nowString = sdf.format(now);
		nowString += System.getProperty("file.separator");
		logger.info("file.separator:**" + System.getProperty("file.separator") + "**");
		logger.info("nowString:**" + nowString + "**");
		
		
		if (!tempDir.endsWith("/") || tempDir.endsWith("\\")){
			tempDir.concat(System.getProperty("file.separator"));
		}
		logger.info("tempDir:**" + tempDir + "**");
		
		tempDir += nowString;
		
		File dir = new File(tempDir);
		if(!dir.exists()){
			logger.info("Creando directorio..." + tempDir);
			dir.mkdir();
		}
		return tempDir;
	}
}
