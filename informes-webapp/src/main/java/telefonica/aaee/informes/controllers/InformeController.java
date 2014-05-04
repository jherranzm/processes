package telefonica.aaee.informes.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import telefonica.aaee.informes.exceptions.InformeNotFoundException;
import telefonica.aaee.informes.form.InformeForm;
import telefonica.aaee.informes.model.Informe;
import telefonica.aaee.informes.model.InformePestanya;
import telefonica.aaee.informes.model.Pestanya;
import telefonica.aaee.informes.model.PestanyaEditor;
import telefonica.aaee.informes.services.ConsultaService;
import telefonica.aaee.informes.services.InformeService;
import telefonica.aaee.informes.services.PestanyaService;
import telefonica.aaee.informes.validators.InformeFormValidator;

@Controller
@RequestMapping("/informe")
public class InformeController {
	
	private static final String INFORME_FORM_NEW = "informe-form-new";
	private static final String INFORME_FORM_EDIT = "informe-form-edit";
	private static final String INFORME_RESULT_PAGE = "informe-result-page";

	protected final Log logger = LogFactory.getLog(getClass());
	
	private List<Pestanya> cachePestanyes = new ArrayList<Pestanya>();
	
	@Resource
    private Environment environment;
	
	@Resource
	private MessageSource messageSource;
	
	@Autowired
	private InformeService informeService;

	@Autowired
	private PestanyaService pestanyaService;

	@Autowired
	private ConsultaService consultaService;

	@Autowired
	private InformeFormValidator informeValidator;

	@InitBinder
	private void initBinder(WebDataBinder binder) {
		
		binder.setValidator(informeValidator);
		
		binder.registerCustomEditor(Pestanya.class, new PestanyaEditor(pestanyaService));
		
	}
	

	@RequestMapping(value="/new")
	public ModelAndView showFormNew() {
		
		cachePestanyes = pestanyaService.findAll();
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(INFORME_FORM_NEW);
		modelAndView.addObject("informeForm", new InformeForm());
		modelAndView.addObject("pestanyesDisponibles", cachePestanyes);
		return modelAndView;
	}
	
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public ModelAndView save(
			@ModelAttribute @Valid InformeForm informeForm,  
            BindingResult result,  
            final RedirectAttributes redirectAttributes, 
            Locale locale) {
		
		ModelAndView modelAndView = new ModelAndView();
		
		if (result.hasErrors()){
			modelAndView.setViewName(INFORME_FORM_NEW);
			StringBuffer errores = new StringBuffer();
			for(FieldError fieldError : result.getFieldErrors()){
				logger.info("fieldError:" + fieldError.getCode());
				logger.info(messageSource.getMessage(fieldError.getCode(), null, null, locale));
				
				errores.append(messageSource.getMessage(fieldError.getCode(), null, null, locale));
				errores.append("<br>");
			}
			modelAndView.addObject("mensajeError", errores.toString());
			modelAndView.addObject("informeForm", informeForm);
			modelAndView.addObject("pestanyesDisponibles", pestanyaService.findAll());
			
			logger.info("Upss! Hay errores..." + errores.toString());
            return modelAndView;  
		}
		
		List<Informe> informes = informeService.findByNombre(
				informeForm.getNombre());
		
		logger.info("informes.size():{"+ informes.size() +"}");
		if(informes.size() == 0){
			
			Informe nuevoInforme =  formToInforme(informeForm);
			
			nuevoInforme = informeService.create(nuevoInforme);
			
			logger.info("Hemos guardado el nuevo informe : " + nuevoInforme.toString());
			informeForm = informeToForm(nuevoInforme);
			
			modelAndView.setViewName(INFORME_RESULT_PAGE);
			
			//List<Informe> informes2 = informeService.findAll();
			Page<Informe> page = informeService.getPage(1);
			
			modelAndView.addObject("nuevoInforme", informeForm);
			
			fillResultPage(modelAndView, page);
			
		}else{
			logger.info("Upss! Hay errores...: Nombre repetido!");
			modelAndView.setViewName(INFORME_FORM_NEW);
			modelAndView.addObject("mensajeError", "Nombre del informe : ["+informeForm.getNombre()+"]... repetido!");
			modelAndView.addObject("informeForm", informeForm);
			modelAndView.addObject("pestanyesDisponibles", pestanyaService.findAll());
		}
		return modelAndView;
	}

	
	@RequestMapping(value="/search", 
			method=RequestMethod.POST)
	public ModelAndView search(
			@RequestParam("queBuscar") String queBuscar,
            final RedirectAttributes redirectAttributes, 
            Locale locale) {
		
		logger.info("Search!" + "[" + queBuscar + "]");
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(INFORME_RESULT_PAGE);
		
		Page<Informe> page = informeService.getPage(queBuscar);
		
		fillPage(modelAndView, page);
		
		return modelAndView;
	}


	private void fillPage(ModelAndView modelAndView, Page<Informe> page) {
		int current = page.getNumber() + 1;
	    int begin = Math.max(1, current - 5);
	    int end = Math.min(begin + 10, page.getTotalPages());
	    int pageSize = page.getNumberOfElements();
		
		logger.info("current :{"+current+"}");
		logger.info("begin :{"+begin+"}");
		logger.info("end :{"+end+"}");
	    
	    modelAndView.addObject("beginIndex", begin);
	    modelAndView.addObject("endIndex", end);
	    modelAndView.addObject("currentIndex", current);
	    modelAndView.addObject("totalPages", page.getTotalPages());
	    modelAndView.addObject("pageSize", pageSize);

	    modelAndView.addObject("informes", page.getContent());
	} 
	


	@RequestMapping(value="/list")
	public ModelAndView list() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(INFORME_RESULT_PAGE);
		
		List<Informe> informes = informeService.findAll();
		
		modelAndView.addObject("informes", informes);
		
		return modelAndView;
	}	
	
	
	@RequestMapping(value="/pages/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView listaPorPaginas(@PathVariable Integer pageNumber) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(INFORME_RESULT_PAGE);
		
		logger.info("Se ha recibido el parámetro {pageNumber} :{"+pageNumber+"}");
		
		//Seguridad
		if(pageNumber < 1) pageNumber = 1;
		
		Page<Informe> page = informeService.getPage(pageNumber);
		
		logger.info("Se ha recibido page :{"+page.toString()+"}");
		
//		for(Informe c : page){
//			logger.info(c.toString());
//		}

		fillResultPage(modelAndView, page);
		
		//modelAndView.addObject("consultas", consultas);
		
		return modelAndView;
	}


	private void fillResultPage(ModelAndView modelAndView, Page<Informe> page) {
		int current = page.getNumber() + 1;
	    int begin = Math.max(1, current - 5);
	    int end = Math.min(begin + 10, page.getTotalPages());
	    int pageSize = page.getNumberOfElements();
		
		logger.info("current :{"+current+"}");
		logger.info("begin :{"+begin+"}");
		logger.info("end :{"+end+"}");
	    
	    modelAndView.addObject("informes", page.getContent());
	    modelAndView.addObject("beginIndex", begin);
	    modelAndView.addObject("endIndex", end);
	    modelAndView.addObject("currentIndex", current);
	    modelAndView.addObject("totalPages", page.getTotalPages());
	    modelAndView.addObject("pageSize", pageSize);
	}	
	
	
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET) 
	public ModelAndView deleteById(
			@PathVariable Long id,  
            final RedirectAttributes redirectAttributes, Locale locale) { 
		
		logger.info("Se ha recibido el parámetro {id} :{"+id+"}");
          
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(INFORME_RESULT_PAGE); 
		
		Informe aBorrar = informeService.findById(id);  
		
		logger.info("Informe a Borrar Localizado!");
		logger.info(aBorrar.toString());
          
		try {
			Informe borrada = informeService.delete(id);  
			modelAndView.addObject("nuevoInforme", informeToForm(borrada));
		} catch (InformeNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
//			List<Informe> informes = informeService.findAll();
//			modelAndView.addObject("informes", informes);
			Page<Informe> page = informeService.getPage(1);
			fillResultPage(modelAndView, page);
		}
          
		
        return modelAndView;  
    } 

	@RequestMapping(value="/edit/{id}", method=RequestMethod.GET) 
	public ModelAndView edit(
			@PathVariable Long id,  
            final RedirectAttributes redirectAttributes) throws InformeNotFoundException { 
		
		logger.info("Se ha recibido el parámetro {id} :{"+id+"}");
          
		Informe modificable = informeService.findById(id);  
		InformeForm informeForm = new InformeForm();
		informeForm = informeToForm(modificable);
		
		ModelAndView modelAndView = new ModelAndView(INFORME_FORM_EDIT);
		modelAndView.addObject("pestanyesDisponibles", pestanyaService.findAll());
		modelAndView.addObject("informeForm", informeForm);
		
        return modelAndView;
    } 

	@RequestMapping(value="/edit/{id}", method=RequestMethod.POST) 
	public ModelAndView update(
			@ModelAttribute @Valid InformeForm informeForm,
			BindingResult result,
			@PathVariable Long id,  
            final RedirectAttributes redirectAttributes) throws InformeNotFoundException { 
		
		logger.info(environment.getProperty("informe.nombre.empty"));
		
		logger.info("update:Se ha recibido el parámetro {id} :{"+id+"}");
		logger.info("update:informeForm :{"+ informeForm.toString() +"}");
          
		if (result.hasErrors()){
			
			ModelAndView mav = new ModelAndView(INFORME_FORM_EDIT);
			logger.info("Upss! Hay errores..." + environment.getProperty(result.getFieldError().getCode()));
			mav.addObject("mensajeError", environment.getProperty(result.getFieldError().getCode()));
			mav.addObject("informe", informeForm);
            return mav;  
		}

		
		logger.info("Buscando nombre de informe duplicado...");
		
		List<Informe> informes = informeService.findByNombreDuplicado(
				informeForm.getNombre(),
				informeForm.getId());
		
		logger.info("informes.size():{"+ informes.size() +"}");
		if(informes.size() == 0){
			logger.info("update:{"+ informeForm.toString() +"}");
			
			Informe informe = formToInforme(informeForm);
			
			informe = informeService.update(informe);
			
			informeForm = informeToForm(informe);
			
			logger.info("guardada!:informe.id :{" + informe.getId() +"}");
			logger.info("guardada!:informe :{" + informe.toString() +"}");
		}else{
			logger.info("update:informe.nombre :{"+ informeForm.getNombre()+"}");
		}
	
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(INFORME_RESULT_PAGE);          
          
//		informes = informeService.findAll();
//		
//		modelAndView.addObject("informes", informes);

		// 2014-04-03
		Page<Informe> page = informeService.getPage(1);
		fillResultPage(modelAndView, page);
		
		modelAndView.addObject("nuevoInforme", informeForm);
		
        return modelAndView;
    }

	@RequestMapping(value="/saveSubmit", method=RequestMethod.POST) 
	public @ResponseBody String saveSubmit(
			@Valid @ModelAttribute(value = "informe") InformeForm informeForm,
			BindingResult result){
		
		String returnText = "Ha habido un error...";

		logger.info("\n\n\nsaveSubmit!!!!....");
		logger.info("\n\n\nsaveSubmit!!!!...." + informeForm.toString());
		
		if(result.hasErrors()){
			returnText = "Ha habido un error...";
		}else{
			returnText = "Todo bien!!";
			
			
		}
		
		return returnText;
		
	}


	private Informe formToInforme(InformeForm informeForm) {
		
		logger.info("Recibimos: " + informeForm.toString());
		
		Informe informe = new Informe();
		
		informe.setId(informeForm.getId());
		informe.setNombre(informeForm.getNombre());
		
		Set<InformePestanya> pestanyes = new HashSet<InformePestanya>();
		int orden = 0;
		for(Pestanya p : informeForm.getPestanyes()){
			
			InformePestanya ip = new InformePestanya();
			ip.setInforme(informe);
			ip.setPestanya(p);
			ip.setOrden(orden++);
			pestanyes.add(ip);
		}
		informe.setPestanyes(pestanyes);
		
		logger.info("Obtenemos: " + informe.toString());
		
		return informe;
	}	

	private InformeForm informeToForm(Informe informe) {
		
		InformeForm informeForm = new InformeForm();
		
		logger.info(informe.toString());
		
		informeForm.setId(informe.getId());
		informeForm.setNombre(informe.getNombre());
		
		Pestanya[] pestanyes = new Pestanya[informe.getPestanyes().size()];
		int k=0;
		for(InformePestanya p : informe.getPestanyes()){
			pestanyes[k++] = p.getPestanya();
			logger.info("Cargando pestaña:" + p.getPestanya().toString());
		}
		informeForm.setPestanyes(pestanyes);
		
		logger.info(informeForm.toString());
		
		return informeForm;
	}	

}
