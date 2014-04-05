package telefonica.aaee.informes.validators;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import telefonica.aaee.informes.form.GeneraInformeForm;

@Component 
public class GeneraInformeFormValidator implements Validator {

	protected final Log logger = LogFactory.getLog(getClass());
	
	@Resource
	private MessageSource messageSource;

	@Override
	public boolean supports(Class<?> clazz) {
		 return GeneraInformeForm.class.isAssignableFrom(clazz); 
	}

	@Override
	public void validate(Object target, Errors errors) {
		

		GeneraInformeForm generaInforme = (GeneraInformeForm) target;
		
		logger.info(String.format("GeneraInformeForm:validate:[%s]", generaInforme.toString()));
		
		ValidationUtils.rejectIfEmptyOrWhitespace(
				errors
				, "informeId"
				, "informe.nombre.empty"
				); 
		ValidationUtils.rejectIfEmptyOrWhitespace(
				errors
				, "acuerdoId"
				, "acuerdo.definicion.empty"
				);
		
	}

}
