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

	public static String createUploadDirFor977rFiles(String tempDir) {
		/**
		 * Si no está la carpeta del día creada, se crea con el formato yyyy-MM-dd
		 */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		String nowString = sdf.format(now);
		nowString += System.getProperty("file.separator");
		
		logger.info(String.format("file.separator:[%s]", System.getProperty("file.separator")));
		logger.info(String.format("nowString:[%s]", nowString));
		
		if (!tempDir.endsWith("/") || tempDir.endsWith("\\")){
			tempDir.concat(System.getProperty("file.separator"));
		}
		logger.info(String.format("tempDir:[%s]",tempDir));
		
		tempDir += nowString;
		
		File dir = new File(tempDir);
		if(!dir.exists()){
			logger.info(String.format("Creando directorio [%s] ...",tempDir));
			if(dir.mkdir()){
				logger.info(String.format("Directorio [%s] creado!",tempDir));
			}
		}
		
		sdf = new SimpleDateFormat("HHmmss-SSS");
		String prefijo = sdf.format(new Date());
		
		tempDir += "977r-" + prefijo;
		tempDir += System.getProperty("file.separator");
		
		dir = new File(tempDir);
		if(!dir.exists()){
			logger.info(String.format("Creando directorio [%s] ...",tempDir));
			if(dir.mkdir()){
				logger.info(String.format("Directorio [%s] creado!",tempDir));
			}
		}

		return tempDir;
	}
}
