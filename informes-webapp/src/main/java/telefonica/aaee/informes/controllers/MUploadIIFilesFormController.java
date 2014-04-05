package telefonica.aaee.informes.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import telefonica.aaee.informes.form.MUploadForm;
import telefonica.aaee.informes.model.FileInfoDTO;
import telefonica.aaee.informes.process.ReadIIFile;

@Controller
@RequestMapping(value="/ii")
public class MUploadIIFilesFormController 
	extends GenericUploadFormController 
	implements HandlerExceptionResolver{
	
	
	@RequestMapping(value="/muploadiifiles-form")
	public ModelAndView muploadiifiles() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("muploadiifiles-form-page");
		return modelAndView;
	}
	
    @RequestMapping(method=RequestMethod.GET)
    public String showForm(ModelMap model){
        MUploadForm form = new MUploadForm();
        model.addAttribute("FORM", form);
        return "muploadiifiles-form-page";
    }

    @RequestMapping(method=RequestMethod.POST)
    public String processForm(
    		@ModelAttribute(value="FORM") MUploadForm form
    		,BindingResult result
    		){
    	
        if(!result.hasErrors()){
        	
            // uploadfiles.temporary.path, este valor se encuentra en application.properties
            String tempDir = environment.getProperty("uploadfiles.temporary.path");
    		logger.info(environment.getProperty("uploadfiles.temporary.path"));

        	tempDir = createUploadDirIfNotExists(tempDir);
            
            
            List<MultipartFile> files = form.getFiles();
            List<FileInfoDTO> uploadedFiles = new ArrayList<FileInfoDTO>();
            
            if(null != files && files.size() > 0) {
                for (MultipartFile multipartFile : files) {
                    try {
                        if (multipartFile.getSize() > 0) {
                        	
                        	FileInfoDTO fileInfo = new FileInfoDTO();
                            
                        	fileInfo.setFileName(multipartFile.getOriginalFilename());
                    		logger.info("fileName:**" + multipartFile.getOriginalFilename() + "**");

                        	fileInfo.setFileSize(multipartFile.getSize());
                            logger.info("fileSize:" + multipartFile.getSize());

                            String absFileName = saveFileInDir(tempDir, multipartFile);
                    		logger.info("absFileName:**" + absFileName + "**");
                            
                    		
                    		/**
                    		 * Procesado del fichero... 
                    		 */
                    		ReadIIFile reader = new ReadIIFile();
                    		reader.setFileName(absFileName);
                    		if(reader.execute()){
                                List<FileInfoDTO> urls = reader.getvEnlaces();
                                        
                        		uploadedFiles.addAll(urls);
                    		}
                            
                            
                        }
                    } catch (Exception e) {
                        logger.info("Error while saving file");
                        return "muploadiifiles-form-page";
                    }
                    
                }
            }
            
            /**
             * Test
             */
            
            Properties props = System.getProperties();
            for(Object key : props.keySet()){
            	logger.info("PROP:[" + key + "]:[" + (String)props.getProperty((String)key) + "]");
            	
            }
            
            List<String> keys = new ArrayList<String>(System.getenv().keySet());
            Collections.sort(keys);
            for (String key : keys) {
            	logger.info("ENV:[" + key + "]:[" + System.getenv().get(key) + "]");
            }
            
            form.setUploadedFiles(uploadedFiles);
            return "muploadiifiles-form-success";
        }else{
            return "muploadiifiles-form-page";
        }
    }

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public ModelAndView resolveException(
    		HttpServletRequest arg0,
    		HttpServletResponse arg1, 
    		Object arg2, 
    		Exception exception) {
    	
        Map<Object, Object> model = new HashMap<Object, Object>();
        if (exception instanceof MaxUploadSizeExceededException){
            model.put("errors", "File size should be less than "+
            ((MaxUploadSizeExceededException)exception).getMaxUploadSize()+" byte.");
        } else{
            model.put("errors", "Unexpected error: " + exception.getMessage());
        }
        model.put("FORM", new MUploadForm());
        return new ModelAndView("/MFileUploadForm", (Map) model);
    }
}
