package telefonica.aaee.informes.controllers;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import telefonica.aaee.informes.exceptions.ConsultaNotFoundException;
import telefonica.aaee.informes.form.ConsultaForm;
import telefonica.aaee.informes.model.Consulta;
import telefonica.aaee.informes.services.ConsultaService;
import telefonica.aaee.informes.services.InformeService;
import telefonica.aaee.informes.validators.ConsultaFormValidator;
import telefonica.aaee.informes.validators.SearchFormValidator;

@Controller
@RequestMapping("/consulta")
public class ConsultaController {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private static final String ENCODING = "ISO-8859-1";
	private static final String CONSULTA_FORM_NEW = "consulta-form-new";
	private static final String CONSULTA_FORM_EDIT = "consulta-form-edit";
	private static final String CONSULTA_RESULT_PAGE = "consulta-result-page";

	
	@Resource
    private Environment environment;
	
	@Resource
	private MessageSource messageSource;
	
	@Autowired
	private InformeService informeService;

	@Autowired
	private ConsultaService consultaService;
	
	@PersistenceContext(unitName = "JPAInformesWebApp")
	private EntityManager entityManager;	
	
	@Autowired
	private ConsultaFormValidator consultaFormValidator;
	
	@Autowired
	private SearchFormValidator searchFormValidator;

	@InitBinder
	private void initBinder(WebDataBinder binder) {
		//binder.setValidator(consultaFormValidator);
		//binder.setValidator(searchFormValidator);
		binder.addValidators(consultaFormValidator, searchFormValidator);
	}


	@RequestMapping(value="/new")
	public ModelAndView consultaPage() {
		return new ModelAndView(CONSULTA_FORM_NEW, "consultaForm", new ConsultaForm());
	}
	
	@RequestMapping(value="/list.html")
	public ModelAndView listaConsultas() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(CONSULTA_RESULT_PAGE);
		
//		List<Consulta> consultas = consultaService.findAll();
		
		List<Consulta> consultas = new ArrayList<Consulta>();
		Iterable<Consulta> lista = consultaService.findAll(new PageRequest(0,10));
		
		for(Consulta consulta : lista){
			consultas.add(consulta);
		}
		
		
		
		modelAndView.addObject("consultas", consultas);
		
		return modelAndView;
	}	
	
	@RequestMapping(value="/pages/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView listaPorPaginas(@PathVariable Integer pageNumber) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(CONSULTA_RESULT_PAGE);
		
		
		
		logger.info("Se ha recibido el parámetro {pageNumber} :{"+pageNumber+"}");
		
		//Seguridad
		if(pageNumber < 1) pageNumber = 1;
		
		Page<Consulta> page = consultaService.getPage(pageNumber);
		
		logger.info("Se ha recibido page :{"+page.toString()+"}");
		
		for(Consulta c : page){
			logger.info(c.toString());
		}

		int current = page.getNumber() + 1;
	    int begin = Math.max(1, current - 5);
	    int end = Math.min(begin + 10, page.getTotalPages());
	    int pageSize = page.getNumberOfElements();
		
		logger.info("current :{"+current+"}");
		logger.info("begin :{"+begin+"}");
		logger.info("end :{"+end+"}");
	    
	    modelAndView.addObject("consultas", page.getContent());
	    modelAndView.addObject("beginIndex", begin);
	    modelAndView.addObject("endIndex", end);
	    modelAndView.addObject("currentIndex", current);
	    modelAndView.addObject("totalPages", page.getTotalPages());
	    modelAndView.addObject("pageSize", pageSize);
		
		return modelAndView;
	}	
	
	
	
	@RequestMapping(value="/edit/{id}", method=RequestMethod.GET) 
	public ModelAndView editConsulta(
			//@PathVariable Integer id,
			@PathVariable Long id,
            final RedirectAttributes redirectAttributes) throws ConsultaNotFoundException { 
		
		logger.info("Se ha recibido el parámetro {id} :{"+id+"}");
          
		Consulta consultaAModificar = consultaService.findById(id);
		
		ConsultaForm consultaForm = consultaToForm(consultaAModificar);
		
		ModelAndView modelAndView = new ModelAndView(CONSULTA_FORM_EDIT);
		modelAndView.addObject("consulta", consultaForm);
		
        return modelAndView;
    } 
	
	
	@RequestMapping(value="/edit/{id}", method=RequestMethod.POST) 
	public ModelAndView updateConsulta(
			@ModelAttribute @Valid ConsultaForm consultaForm,
			BindingResult result,
			@PathVariable Integer id,  
            final RedirectAttributes redirectAttributes) throws ConsultaNotFoundException { 
		
		Assert.notNull(consultaForm.getId());
		Assert.notNull(consultaForm.getNombre());
		Assert.notNull(consultaForm.getDefinicion());
		
		
		Consulta mod = new Consulta();
		ModelAndView modelAndView = new ModelAndView();
		
		logger.info(environment.getProperty("consulta.nombre.empty"));
		logger.info(environment.getProperty("consulta.definicion.empty"));
		
		logger.info(String.format("Consulta.Nombre : [%s]", consultaForm.getNombre()));
		logger.info(String.format("Consulta.Definicion : [%s]",  consultaForm.getDefinicion()));
		
		logger.info("updateConsulta:Se ha recibido el parámetro {id} :{"+id+"}");
		logger.info("updateConsulta:consulta.id :{"+ consultaForm.getId()+"}");
		logger.info("updateConsulta:consulta.nombre :{"+ consultaForm.getNombre()+"}");
		logger.info("updateConsulta:consulta.definicion :{"+ consultaForm.getDefinicion()+"}");
          
		if (result.hasErrors()){
			
			modelAndView.setViewName(CONSULTA_FORM_EDIT);
			logger.info("Upss! Hay errores..." + environment.getProperty(result.getFieldError().getCode()));
			modelAndView.addObject("mensajeError", environment.getProperty(result.getFieldError().getCode()));
			modelAndView.addObject("consulta", consultaForm);
            return modelAndView;  
		}

		
		List<Consulta> consultas = consultaService.findByNombreDuplicado(
				consultaForm.getNombre(),
				consultaForm.getId());
		
		logger.info("consultas.size():{"+ consultas.size() +"}");
		if(consultas.size() == 0){
			logger.info("update:{"+ consultaForm.toString() +"}");
			
			mod = formToConsulta(consultaForm);
			logger.info("mod:{"+ mod.toString() +"}");
			
			Consulta consulta = consultaService.update(mod);
			
			logger.info("guardada!:consulta :{"+ consulta.toString() +"}");
			
			modelAndView.setViewName(CONSULTA_RESULT_PAGE);          
			logger.info("viewName set...!");
	          
			consultas = consultaService.findAll();
			logger.info("Localizadas " + consultas.size() + " consultas!");
			
			modelAndView.addObject("consultas", consultas);
			logger.info("Añadido el objeto consultas!");
			modelAndView.addObject("nuevaConsulta", consultaToForm(consulta));
			logger.info("Añadido el objeto " + consultaToForm(consulta).toString());
		}else{
			logger.info("updateConsulta:consulta.nombre :{"+ consultaForm.getNombre()+"}");
		}
	
        return modelAndView;
    }

	
	
	@RequestMapping(value="/save", 
			method=RequestMethod.POST)
	public ModelAndView save(
			@ModelAttribute @Valid ConsultaForm consultasqlForm,  
            BindingResult result,  
            final RedirectAttributes redirectAttributes, 
            Locale locale) {
		
//		Assert.notNull(consultasqlForm.getNombre());
//		Assert.hasText(consultasqlForm.getNombre());
//		Assert.notNull(consultasqlForm.getDefinicion());
//		Assert.hasText(consultasqlForm.getDefinicion());
		
		logger.info(String.format("Consulta.Nombre : [%s]", consultasqlForm.getNombre()));
		logger.info(String.format("Consulta.Definicion : [%s]", consultasqlForm.getDefinicion()));
		
		try {
			logger.info(String.format("Consulta.Nombre : [%s]", new String (consultasqlForm.getNombre().getBytes(ENCODING))));
			logger.info(String.format("Consulta.Definicion : [%s]",  new String (consultasqlForm.getDefinicion().getBytes(ENCODING))));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ModelAndView modelAndView = new ModelAndView();
		
		if (result.hasErrors()){
			logger.info("Upss! Hay errores...");
			modelAndView.setViewName(CONSULTA_FORM_NEW);
			StringBuffer errores = new StringBuffer();
			for(FieldError fieldError : result.getFieldErrors()){
				logger.info("fieldError:" + fieldError.getCode());
				logger.info(messageSource.getMessage(fieldError.getCode(), null, null, locale));
				
				errores.append(messageSource.getMessage(fieldError.getCode(), null, null, locale));
				errores.append("<br>");
			}
			modelAndView.addObject("mensajeError", errores.toString());
			modelAndView.addObject("consultasqlForm", consultasqlForm);
			logger.info("Upss! Hay errores..." + errores.toString());
            return modelAndView;  
		}else{
			logger.info("NO hay errores!!");
		}
		
		List<Consulta> consultas = consultaService.findByNombre(
				consultasqlForm.getNombre());
		
		logger.info("consultas.size():{"+ consultas.size() +"}");
		if(consultas.size() == 0){
			
			Consulta nuevaConsulta = new Consulta();
			try {
				nuevaConsulta.setNombre(new String(consultasqlForm.getNombre().getBytes(ENCODING)));
				nuevaConsulta.setDefinicion(new String(consultasqlForm.getDefinicion().getBytes(ENCODING)));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			nuevaConsulta = consultaService.create(nuevaConsulta);
			
			logger.info("nuevaConsulta:" + nuevaConsulta.toString());
			
			modelAndView.setViewName(CONSULTA_RESULT_PAGE);
			
			modelAndView.addObject("nuevaConsulta", consultaToForm(nuevaConsulta));
			modelAndView.addObject("consultas", consultaService.findAll());
			
		}else{
			logger.info("Upss! Hay errores...: Nombre repetido!");
			modelAndView.setViewName(CONSULTA_FORM_NEW);
			modelAndView.addObject("mensajeError", "Nombre de consulta ["+consultasqlForm.getNombre()+"]... repetido!");
			modelAndView.addObject("consultasqlForm", consultasqlForm);
		}
		return modelAndView;
	}	


	private Consulta formToConsulta(ConsultaForm consultaForm) {
		Consulta mod = new Consulta();
		
		mod.setId(consultaForm.getId());
		mod.setNombre(consultaForm.getNombre());
		mod.setDefinicion(consultaForm.getDefinicion());
		
		return mod;
		
		
	} 

	private ConsultaForm consultaToForm(Consulta consulta) {
		ConsultaForm mod = new ConsultaForm();
		
		mod.setId(consulta.getId());
		mod.setNombre(consulta.getNombre());
		mod.setDefinicion(consulta.getDefinicion());
		
		return mod;
		
		
	} 
}
