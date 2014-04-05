package telefonica.aaee.informes.validators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import telefonica.aaee.informes.form.InformeForm;

@Component 
public class InformeFormValidator implements Validator {

	protected final Log logger = LogFactory.getLog(getClass());

	@Override
	public boolean supports(Class<?> clazz) {
		 return InformeForm.class.isAssignableFrom(clazz); 
	}

	@Override
	public void validate(Object target, Errors errors) {

		InformeForm informe = (InformeForm) target;
		
		logger.info("InformeFormValidator:validate:" + informe.toString());
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nombre", "informe.nombre.empty"); 
	}

}
