package telefonica.aaee.informes.controllers;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import telefonica.aaee.capture977.Split977;
import telefonica.aaee.capture977.Split977Config;
import telefonica.aaee.informes.form.MUploadCapture977rForm;
import telefonica.aaee.informes.form.MUploadForm;
import telefonica.aaee.informes.model.FileInfoDTO;

@Controller
@RequestMapping(value="/977r")
public class MUpload977rFilesFormController 
	extends GenericUploadFormController 
	implements HandlerExceptionResolver{
	
	
	private static final String MUPLOAD977RFILES_FORM_PAGE = "mupload977rfiles-form-page";

	@RequestMapping(value="/mupload977rfiles-form")
	public ModelAndView muploadiifiles() {
		ModelAndView modelAndView = new ModelAndView();
		MUploadCapture977rForm form = new MUploadCapture977rForm();
		modelAndView.addObject("uploadForm", form);
		modelAndView.setViewName(MUPLOAD977RFILES_FORM_PAGE);
		return modelAndView;
	}
	
    @RequestMapping(method=RequestMethod.GET)
    public String showForm(ModelMap model){
    	MUploadCapture977rForm form = new MUploadCapture977rForm();
        model.addAttribute("uploadForm", form);
        return MUPLOAD977RFILES_FORM_PAGE;
    }

    @RequestMapping(method=RequestMethod.POST)
    public String processForm(
    		@ModelAttribute(value="FORM") MUploadCapture977rForm form
    		,BindingResult result
    		){
    	
        if(!result.hasErrors()){
        	
            // uploadfiles.temporary.path, este valor se encuentra en application.properties
            String tempDir = environment.getProperty("uploadfiles.temporary.path");
    		logger.info(environment.getProperty("uploadfiles.temporary.path"));

        	tempDir = createUploadDirFor977rFiles(tempDir);
        	
        	String acuerdo = form.getAcuerdo();
        	logger.info(String.format("Acuerdo:[%s]", acuerdo));
        	boolean detalleLlamadas = form.isDetalleLlamadas();
        	logger.info(String.format("detalleLlamadas:[%s]", detalleLlamadas));
        	boolean detalleLlamadasRI = form.isDetalleLlamadasRI();
        	logger.info(String.format("detalleLlamadasRI:[%s]", detalleLlamadasRI));
            
            
            List<MultipartFile> files = form.getFiles();
            List<FileInfoDTO> uploadedFiles = new ArrayList<FileInfoDTO>();
            
            if(null != files && files.size() > 0) {
                for (MultipartFile multipartFile : files) {
                    try {
                        if (multipartFile.getSize() > 0) {
                        	
                        	FileInfoDTO fileInfo = new FileInfoDTO();
                            
                        	fileInfo.setFileName(multipartFile.getOriginalFilename());
                        	logger.info(String.format("fileName:[%s]", multipartFile.getOriginalFilename()));

                        	fileInfo.setFileSize(multipartFile.getSize());
                        	logger.info(String.format("fileSize:[%s]", multipartFile.getSize()));

                            String absFileName = saveFileInDir(tempDir, multipartFile);
                            logger.info(String.format("absFileName:[%s]", absFileName));
                        }
                    } catch (Exception e) {
                        logger.info("Error while saving file");
                        return MUPLOAD977RFILES_FORM_PAGE;
                    } //try
                }// for
                
                Split977 sp = new Split977();
    			
    			Split977Config config = new Split977Config.Builder()
    			.acuerdo(acuerdo)
    			.detalleLlamadas(detalleLlamadas)
    			.detalleLlamadasRI(detalleLlamadasRI)
    			.directorioZipFiles(tempDir)
    			.directorioOut(tempDir)
    			.build();
    		
    		sp.setConfig(config);
    			

//    			assertTrue(sp.getFicheros().length == 1);
    		
    			if(sp.getFicheros().length > 0 ){
    				sp.execute();
    				logger.info(String.format("Se ha tardado:[%s] segundos!", (sp.getTiempoEmpleado()/1000)));
    			}else{
    				logger.fatal(String.format("Sin ficheros a tratar en [%s]", tempDir));
    			}
            }
            
            form.setUploadedFiles(uploadedFiles);
            return "mupload977rfiles-form-success";
        }else{
            return MUPLOAD977RFILES_FORM_PAGE;
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
