package telefonica.aaee.informes.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/getFile")
public class DownloadFileController {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Resource
    private Environment environment;
	

	@RequestMapping(value="/file/{fileName}/{ext}", method = RequestMethod.GET)
	public ModelAndView handleRequest(
			HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable String fileName,
            @PathVariable String ext) throws Exception {
 
        try {
            String nombreFichero = fileName;
            String tempDir = environment.getProperty("uploadfiles.temporary.path");
    		logger.info(environment.getProperty("uploadfiles.temporary.path"));

        	tempDir = createUploadDirIfNotExists(tempDir);
        	
            logger.info(nombreFichero);
            
            logger.info(tempDir);
 
            response.setContentType("application/"+ext);
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + nombreFichero+ "\"");
 
            logger.info(tempDir+nombreFichero);

            InputStream is = new FileInputStream(tempDir+nombreFichero);
             
            IOUtils.copy(is, response.getOutputStream());
             
            response.flushBuffer();
             
        } catch (IOException ex) {
            // Sacar log de error.
            throw ex;
        }
         
        return null;
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
}
