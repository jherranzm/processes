package telefonica.aaee.informes.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import telefonica.aaee.informes.exceptions.PestanyaNotFoundException;
import telefonica.aaee.informes.form.PestanyaForm;
import telefonica.aaee.informes.model.Consulta;
import telefonica.aaee.informes.model.Pestanya;
import telefonica.aaee.informes.services.ConsultaService;
import telefonica.aaee.informes.services.PestanyaService;
import telefonica.aaee.informes.validators.PestanyaFormValidator;

@Controller
@RequestMapping("/pestanya")
public class PestanyaController {
	
	private static final String PESTANYA_FORM_EDIT = "pestanya-form-edit";
	private static final String PESTANYA_RESULT_PAGE = "pestanya-result-page";
	private static final String PESTANYA_FORM_NEW = "pestanya-form-new";

	protected final Log logger = LogFactory.getLog(getClass());

	
	@Resource
    private Environment environment;
	
	@Resource
	private MessageSource messageSource;
	
	@Autowired
	private PestanyaService pestanyaService;

	@Autowired
	private ConsultaService consultaService;

	@Autowired
	private PestanyaFormValidator pestanyaFormValidator;
	
	@InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(pestanyaFormValidator);
	}



	@RequestMapping(value="/new")
	public ModelAndView pestanyasqlPage() {
		List<Integer> numsFilaInicial = new ArrayList<Integer>();
		for(int k=0; k<12;k++){
			numsFilaInicial.add(new Integer(k));
		}
		
		List<Consulta> consultas = consultaService.findAll();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(PESTANYA_FORM_NEW);
		modelAndView.addObject("pestanyaForm", new PestanyaForm());
		modelAndView.addObject("consultas", consultas);
		modelAndView.addObject("numsFilaInicial", numsFilaInicial);
		return modelAndView;
	}
	
	@RequestMapping(value="/list.html")
	public ModelAndView listaConsultasql() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(PESTANYA_RESULT_PAGE);
		
		List<Pestanya> pestanyes = pestanyaService.findAll();
		
		modelAndView.addObject("pestanyes", pestanyes);
		
		return modelAndView;
	}	
	
	@RequestMapping(value="/pages/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView listaPorPaginas(@PathVariable Integer pageNumber) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(PESTANYA_RESULT_PAGE);
		
		logger.info("Se ha recibido el parámetro {pageNumber} :{"+pageNumber+"}");
		
		//Seguridad
		if(pageNumber < 1) pageNumber = 1;
		
		Page<Pestanya> page = pestanyaService.getPage(pageNumber);
		
		logger.info("Se ha recibido page :{"+page.toString()+"}");
		
		for(Pestanya c : page){
			logger.info(c.toString());
		}

		int current = page.getNumber() + 1;
	    int begin = Math.max(1, current - 5);
	    int end = Math.min(begin + 10, page.getTotalPages());
	    int pageSize = page.getNumberOfElements();
		
		logger.info("current :{"+current+"}");
		logger.info("begin :{"+begin+"}");
		logger.info("end :{"+end+"}");
	    
	    modelAndView.addObject("pestanyes", page.getContent());
	    modelAndView.addObject("beginIndex", begin);
	    modelAndView.addObject("endIndex", end);
	    modelAndView.addObject("currentIndex", current);
	    modelAndView.addObject("totalPages", page.getTotalPages());
	    modelAndView.addObject("pageSize", pageSize);
		
		//modelAndView.addObject("consultas", consultas);
		
		return modelAndView;
	}	
	
	
	
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public ModelAndView save(
			@ModelAttribute @Valid PestanyaForm pestanyaForm,  
            BindingResult result,  
            final RedirectAttributes redirectAttributes, 
            Locale locale) {
		
		Assert.notNull(pestanyaForm.getNombre());

		ModelAndView modelAndView = new ModelAndView();
		
		if (result.hasErrors()){
			modelAndView.setViewName(PESTANYA_FORM_NEW);
			StringBuffer errores = new StringBuffer();
			for(FieldError fieldError : result.getFieldErrors()){
				logger.info(messageSource.getMessage(fieldError.getCode(), null, null, locale));
				errores.append(messageSource.getMessage(fieldError.getCode(), null, null, locale)).append("<br>");
			}
			modelAndView.addObject("mensajeError", errores.toString());
			modelAndView.addObject("pestanyaForm", pestanyaForm);
			
			List<Consulta> consultas = consultaService.findAll();
			modelAndView.addObject("consultas", consultas);
			List<Integer> numsFilaInicial = new ArrayList<Integer>();
			for(int k=0; k<12;k++){
				numsFilaInicial.add(new Integer(k));
			}
			modelAndView.addObject("numsFilaInicial", numsFilaInicial);
			
			
			logger.info("Upss! Hay errores..." + errores.toString());
            return modelAndView;  
		}
		
		List<Pestanya> pestanyes = pestanyaService.findByNombre(
				pestanyaForm.getNombre());
		
		logger.info("pestanyes.size():{"+ pestanyes.size() +"}");
		if(pestanyes.size() == 0){
			
			Pestanya nuevaPestanya =  formToPestanya(pestanyaForm);
			
			nuevaPestanya = pestanyaService.create(nuevaPestanya);
			
			logger.info("Hemos guardado la nueva pestaña : " + nuevaPestanya.toString());
			pestanyaForm = pestanyaToForm(nuevaPestanya);
			
			modelAndView.setViewName(PESTANYA_RESULT_PAGE);
			
			List<Pestanya> pestanyes2 = pestanyaService.findAll();
			
			modelAndView.addObject("nuevaPestanya", pestanyaForm);
			modelAndView.addObject("pestanyes", pestanyes2);
			
		}else{
			logger.info("Upss! Hay errores...: Nombre repetido!");
			modelAndView.setViewName(PESTANYA_FORM_NEW);
			modelAndView.addObject("mensajeError", "Nombre de pestaña ["+pestanyaForm.getNombre()+"]... repetido!");
			modelAndView.addObject("pestanyaForm", pestanyaForm);
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
		modelAndView.setViewName(PESTANYA_RESULT_PAGE);
		
		Page<Pestanya> page = pestanyaService.getPage(queBuscar);
		
		fillPage(modelAndView, page);
		
		return modelAndView;
	}


	private void fillPage(ModelAndView modelAndView, Page<Pestanya> page) {
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

	    modelAndView.addObject("pestanyes", page.getContent());
	} 
	
	
	@RequestMapping(value="/edit/{id}", method=RequestMethod.GET) 
	public ModelAndView edit(
			@PathVariable Long id,
            final RedirectAttributes redirectAttributes) throws PestanyaNotFoundException { 
		
		logger.info("Se ha recibido el parámetro {id} :{"+id+"}");
          
		Pestanya modificable = pestanyaService.findById(id);  
		List<Consulta> consultas = consultaService.findAll();
		PestanyaForm pestanyaForm = new PestanyaForm();
		pestanyaForm = pestanyaToForm(modificable);
		
		List<Integer> numsFilaInicial = new ArrayList<Integer>();
		for(int k=1; k<12;k++){
			numsFilaInicial.add(new Integer(k));
		}
		
		ModelAndView modelAndView = new ModelAndView(PESTANYA_FORM_EDIT);
		modelAndView.addObject("pestanyaForm", pestanyaForm);
		modelAndView.addObject("consultas", consultas);
		modelAndView.addObject("numsFilaInicial", numsFilaInicial);
		
        return modelAndView;
    } 

	@RequestMapping(value="/edit/{id}", method=RequestMethod.POST) 
	public ModelAndView update(
			@ModelAttribute @Valid PestanyaForm pestanyaForm,
			BindingResult result,
			@PathVariable Integer id,  
            final RedirectAttributes redirectAttributes) throws PestanyaNotFoundException { 
		
		logger.info(environment.getProperty("pestanya.nombre.empty"));
		logger.info(environment.getProperty("pestanya.definicion.empty"));
		
		logger.info("updatePestanya:Se ha recibido el parámetro {id} :{"+id+"}");
		logger.info("updatePestanya:pestanyaForm :{"+ pestanyaForm.toString() +"}");
          
		if (result.hasErrors()){
			
			ModelAndView mav = new ModelAndView(PESTANYA_FORM_EDIT);
			logger.info("Upss! Hay errores..." + environment.getProperty(result.getFieldError().getCode()));
			mav.addObject("mensajeError", environment.getProperty(result.getFieldError().getCode()));
			mav.addObject("pestanya", pestanyaForm);
            return mav;  
		}

		
		List<Pestanya> pestanyes = pestanyaService.findByNombreDuplicado(
				pestanyaForm.getNombre(),
				pestanyaForm.getId());
		
		logger.info("pestanyes.size():{"+ pestanyes.size() +"}");
		if(pestanyes.size() == 0){
			logger.info("update:{"+ pestanyaForm.toString() +"}");
			
			Pestanya pestanya = formToPestanya(pestanyaForm);
			
			pestanya = pestanyaService.update(pestanya);
			
			pestanyaForm = pestanyaToForm(pestanya);
			
			logger.info("guardada!:pestanya.id :{" + pestanya.getId() +"}");
			logger.info("guardada!:pestanya :{" + pestanya.toString() +"}");
		}else{
			logger.info("updatePestanya:pestanya.nombre :{"+ pestanyaForm.getNombre()+"}");
		}
	
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(PESTANYA_RESULT_PAGE);          
          
		pestanyes = pestanyaService.findAll();
		
		modelAndView.addObject("pestanyes", pestanyes);
		modelAndView.addObject("nuevaPestanya", pestanyaForm);
		
        return modelAndView;
    }


	private Pestanya formToPestanya(PestanyaForm pestanyaForm) {
		
		logger.info("Recibimos: " + pestanyaForm.toString());
		
		Pestanya pestanya = new Pestanya();
		
		pestanya.setId(pestanyaForm.getId());
		pestanya.setNombre(pestanyaForm.getNombre());
		pestanya.setRango(pestanyaForm.getRango());
		pestanya.setNumFilaInicial(pestanyaForm.getNumFilaInicial());
			pestanya.setConsulta(consultaService.findById(pestanyaForm.getConsultaSQL_id()));
		
		logger.info("Obtenemos: " + pestanya.toString());
		
		return pestanya;
	}	

	private PestanyaForm pestanyaToForm(Pestanya pestanya) {
		
		PestanyaForm pestanyaForm = new PestanyaForm();
		
		logger.info(pestanya.toString());
		
		pestanyaForm.setId(pestanya.getId());
		pestanyaForm.setNombre(pestanya.getNombre());
		pestanyaForm.setRango(pestanya.getRango());
		pestanyaForm.setNumFilaInicial(pestanya.getNumFilaInicial());
		pestanyaForm.setConsultaSQL_id((int)pestanya.getConsulta().getId());
		pestanyaForm.setConsulta(pestanya.getConsulta());
		
		logger.info(pestanyaForm.toString());
		
		return pestanyaForm;
	}	

}
