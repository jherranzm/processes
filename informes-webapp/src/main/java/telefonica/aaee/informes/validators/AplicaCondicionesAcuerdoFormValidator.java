package telefonica.aaee.informes.validators;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import telefonica.aaee.informes.form.AplicaCondicionesAcuerdoForm;

@Component 
public class AplicaCondicionesAcuerdoFormValidator implements Validator {

	protected final Log logger = LogFactory.getLog(getClass());
	
	@Resource
	private MessageSource messageSource;

	@Override
	public boolean supports(Class<?> clazz) {
		 return AplicaCondicionesAcuerdoForm.class.isAssignableFrom(clazz); 
	}

	@Override
	public void validate(Object target, Errors errors) {
		

		AplicaCondicionesAcuerdoForm form = (AplicaCondicionesAcuerdoForm) target;
		
		logger.info(String.format("AplicaCondicionesAcuerdoForm:validate:[%s]", form.toString()));
		
		ValidationUtils.rejectIfEmptyOrWhitespace(
				errors
				, "acuerdoId"
				, "acuerdo.definicion.empty"
				);
		
	}

}
