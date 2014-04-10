package telefonica.aaee.informes.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import telefonica.aaee.app.GeneraREGFicheroExcel;
import telefonica.aaee.informes.form.GeneraInformeForm;
import telefonica.aaee.informes.helpers.FileHelper;
import telefonica.aaee.informes.model.FileInfoDTO;
import telefonica.aaee.informes.model.Informe;
import telefonica.aaee.informes.model.condiciones.Acuerdo;
import telefonica.aaee.informes.services.InformeService;
import telefonica.aaee.informes.services.condiciones.AcuerdoService;

@Controller
@RequestMapping("/generaInforme")
public class GeneraInformeController {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private static final String ENCODING = "ISO-8859-1";
	private static final String GENERA_INFORME_FORM_NEW = "genera-informe-form-new";
	private static final String GENERA_INFORME_FORM_EDIT = "genera-informe-form-edit";
	private static final String GENERA_INFORME_RESULT_PAGE = "genera-informe-result-page";

	
	@Resource
    private Environment environment;
	
	@Resource
	private MessageSource messageSource;
	
	@Autowired
	private InformeService informeService;

	@Autowired
	private AcuerdoService acuerdoService;

	@PersistenceContext(unitName = "JPAInformesWebApp")
	private EntityManager entityManager;	
	
	
	@InitBinder
	private void initBinder(WebDataBinder binder) {
	}


	@RequestMapping(value="/new")
	public ModelAndView consultaPage() {
		
		List<Informe> informes = informeService.findAll();
		List<Acuerdo> acuerdos = acuerdoService.findAll();
		
		ModelAndView model = new ModelAndView();
		model.setViewName(GENERA_INFORME_FORM_NEW);
		model.addObject("generaInformeForm", new GeneraInformeForm());
		model.addObject("informes", informes);
		model.addObject("acuerdos", acuerdos);
		
		return model;
	}
	
	@RequestMapping(value="/gen", method=RequestMethod.POST)
	public String genera(
			@ModelAttribute(value="FORM") @Valid GeneraInformeForm generaInformeForm,
			BindingResult result,
            final RedirectAttributes redirectAttributes) {
		
		logger.info("Gen!");

		Assert.notNull(generaInformeForm.getInformeId());
		Assert.notNull(generaInformeForm.getAcuerdoId());
		
		logger.info("Informe:" + generaInformeForm.getInformeId());
		logger.info("Acuerdo:" + generaInformeForm.getAcuerdoId());
		
		Informe informe = informeService.findById(generaInformeForm.getInformeId());
		Acuerdo acuerdo = acuerdoService.findById(generaInformeForm.getAcuerdoId());
		
		logger.info("Informe:" + informe.toString());
		logger.info("Acuerdo:" + acuerdo.toString());

		List<FileInfoDTO> urls = new ArrayList<FileInfoDTO>();
		
		String tempDir = environment.getProperty("uploadfiles.temporary.path");
		logger.info(environment.getProperty("uploadfiles.temporary.path"));

    	tempDir = FileHelper.createUploadDirIfNotExists(tempDir);
		
		GeneraREGFicheroExcel grfe = new GeneraREGFicheroExcel();
		
		grfe.setAcuerdo(acuerdo.getAcuerdo());
		grfe.setInforme(informe.getNombre());
		grfe.setRuta(tempDir);
		
		grfe.generaExcel();
		
		FileInfoDTO info = new FileInfoDTO();
		
		File file = new File(grfe.getExcelFile());
		
		info.setFileName(FilenameUtils.getName(grfe.getExcelFile()));
		info.setFileSize(file.length());
		info.setFileExt(FilenameUtils.getExtension(grfe.getExcelFile()));
		urls.add(info);
		

		generaInformeForm.setUrls(urls);
		
		return GENERA_INFORME_RESULT_PAGE;
	}
}
