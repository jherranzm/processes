package telefonica.aaee.informes.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.MultipartFile;

public class GenericUploadFormController {

	protected static final int MAX_BUFFER = 10000000;
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Resource
	protected Environment environment;

	public GenericUploadFormController() {
		super();
	}

	protected String createUploadDirIfNotExists(String tempDir) {
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

	protected int getLinesFromFile(String fileName) {
		
		BufferedReader buffReader;
		int fila = 0;
	    try {
			buffReader = new BufferedReader(new FileReader(fileName));
	
			String line;
			line = buffReader.readLine();
	
			while (true) {
	
			    if (line == null) {
				break;
			    }
	
			    line = buffReader.readLine();
			    fila++;
			}
	
			buffReader.close();
	
	    }catch (Exception e) {
	    	
	    }
	
		return fila;
	}

	/**
	 * 
	 * @param tempDir, el directorio donde guardar el fichero subido
	 * @param multipartFile, el fichero subido
	 * 
	 * @returns fileName, el nombre del fichero guardado
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	protected String saveFileInDir(String tempDir, MultipartFile multipartFile)
			throws IOException, FileNotFoundException {
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS-");
				String prefijo = sdf.format(new Date());
				
				String fileName = tempDir + prefijo + multipartFile.getOriginalFilename();
				
				OutputStream outputStream = null;
				InputStream inputStream = null;
				inputStream = multipartFile.getInputStream();
				outputStream = new FileOutputStream(fileName);
				int readBytes = 0;
				byte[] buffer = new byte[MAX_BUFFER];
				while ((readBytes = inputStream.read(buffer, 0, MAX_BUFFER)) != -1) {
				     outputStream.write(buffer, 0, readBytes);
				}
				outputStream.close();
				inputStream.close();
				
				return fileName;
			}

}