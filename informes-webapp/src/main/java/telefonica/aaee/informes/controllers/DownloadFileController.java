package telefonica.aaee.informes.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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

import telefonica.aaee.informes.helpers.FileHelper;

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
		
		logger.info(fileName);
		logger.info(ext);
 
        try {
            String nombreFichero = fileName;
            String tempDir = environment.getProperty("uploadfiles.temporary.path");
    		logger.info(environment.getProperty("uploadfiles.temporary.path"));

        	tempDir = FileHelper.createUploadDirIfNotExists(tempDir);
        	
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


}
