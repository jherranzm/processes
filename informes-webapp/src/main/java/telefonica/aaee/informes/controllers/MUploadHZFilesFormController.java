package telefonica.aaee.informes.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
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
import telefonica.aaee.informes.process.ReadHzFile;

@Controller
@RequestMapping(value="/hz")
public class MUploadHZFilesFormController 
	extends GenericUploadFormController 
	implements HandlerExceptionResolver{
	
	
	private static final String MUPLOADHZFILES_FORM_SUCCESS = "muploadhzfiles-form-success";
	private static final String MUPLOADHZFILES_FORM_PAGE = "muploadhzfiles-form-page";

	@RequestMapping(value="/muploadhzfiles-form")
	public ModelAndView muploadhzfiles() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(MUPLOADHZFILES_FORM_PAGE);
		return modelAndView;
	}
	
    @RequestMapping(method=RequestMethod.GET)
    public String showForm(ModelMap model){
        MUploadForm form = new MUploadForm();
        model.addAttribute("FORM", form);
        return MUPLOADHZFILES_FORM_PAGE;
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
            List<FileInfoDTO> urls = new ArrayList<FileInfoDTO>();
            
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
                    		ReadHzFile reader = new ReadHzFile();
                    		reader.setHZFile(new File(absFileName));
                    		reader.setRealPath(tempDir);
                    		if(reader.execute()){
                                        
                        		uploadedFiles.add(fileInfo);
                        		
                        		FileInfoDTO info = new FileInfoDTO();
                        		
                        		info.setFileExt(FilenameUtils.getExtension(reader.getNombreFicheroExcel()));
                        		info.setFileName(reader.getNombreFicheroExcel());
                        		
                        		urls.add(info);
                    		}
                            
                            
                        }
                    } catch (Exception e) {
                        logger.info("Error while saving file");
                        return MUPLOADHZFILES_FORM_PAGE;
                    }
                    
                }
            }
            
            
            form.setUploadedFiles(uploadedFiles);
            form.setUrls(urls);
            return MUPLOADHZFILES_FORM_SUCCESS;
        }else{
            return MUPLOADHZFILES_FORM_PAGE;
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
        return new ModelAndView(MUPLOADHZFILES_FORM_PAGE, (Map) model);
    }
}
