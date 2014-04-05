package telefonica.aaee.informes.validators;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import telefonica.aaee.informes.form.PestanyaForm;
import telefonica.aaee.informes.model.Pestanya;
import telefonica.aaee.informes.services.PestanyaService;

@Component 
public class PestanyaFormValidator implements Validator {

	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private PestanyaService pestanyaService;

	@Override
	public boolean supports(Class<?> clazz) {
		 return PestanyaForm.class.isAssignableFrom(clazz); 
	}

	@Override
	public void validate(Object target, Errors errors) {

		PestanyaForm object = (PestanyaForm) target;
		
		logger.info("PestanyaFormValidator:validate:" + object.toString());
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nombre", "pestanya.nombre.empty"); 
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "rango", "pestanya.rango.empty");
		
		
		/**
		 * Comprobación nombre de consulta duplicado...
		 * 
		 */
		List<Pestanya> pestanyes = 
				pestanyaService.findByNombreDuplicado(object.getNombre(), object.getId());
		if (pestanyes.size() > 0){
			errors.rejectValue("nombre", "pestanya.nombre.duplicated");
		}
		
	}

}