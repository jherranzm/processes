package telefonica.aaee.informes.validators;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import telefonica.aaee.informes.form.ConsultaForm;
import telefonica.aaee.informes.model.Consulta;
import telefonica.aaee.informes.services.ConsultaService;

@Component 
public class ConsultaFormValidator implements Validator {

	protected final Log logger = LogFactory.getLog(getClass());
	
	@Resource
	private ConsultaService consultaService;

	@Resource
	private MessageSource messageSource;

	@Override
	public boolean supports(Class<?> clazz) {
		 return ConsultaForm.class.isAssignableFrom(clazz); 
	}

	@Override
	public void validate(Object target, Errors errors) {
		

		ConsultaForm consulta = (ConsultaForm) target;
		
		logger.info(String.format("ConsultaSQLValidator:validate:[%s]", consulta.toString()));
		
		ValidationUtils.rejectIfEmptyOrWhitespace(
				errors
				, "nombre"
				, "consulta.nombre.empty"
				); 
		ValidationUtils.rejectIfEmptyOrWhitespace(
				errors
				, "definicion"
				, "consulta.definicion.empty"
				);
		
		
		/**
		 * Comprobación nombre de consulta duplicado...
		 * 
		 */
		List<Consulta> consultas = 
				consultaService.findByNombreDuplicado(consulta.getNombre(), consulta.getId());
		if (consultas.size() > 0){
			errors.rejectValue("nombre", "consulta.nombre.duplicated");
		}
	}

}
