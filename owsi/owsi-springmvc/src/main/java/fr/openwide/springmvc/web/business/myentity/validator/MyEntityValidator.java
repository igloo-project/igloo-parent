package fr.openwide.springmvc.web.business.myentity.validator;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;

import fr.openwide.springmvc.web.business.common.validator.AbstractValidator;
import fr.openwide.springmvc.web.business.myentity.model.MyEntity;
import fr.openwide.springmvc.web.business.myentity.model.MyEntityForm;

public class MyEntityValidator extends AbstractValidator {

	private static final Log LOGGER = LogFactory.getLog(MyEntityValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(MyEntity.class) || clazz.equals(MyEntityForm.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		LOGGER.info("Validation");
		MyEntityForm myEntityForm = (MyEntityForm) target;
		Date date = myEntityForm.getDate();

		ensureStringHasText("descr", errors);
		ensureDateIsPassed(date, errors);
	}

	/*
	 * Les méthodes "ensure" de validation qui sont spécifiques peuvent être
	 * définies directmement dans le validateur concernés au lieu d'être
	 * définies dans AbstractValidator
	 */
	private void ensureDateIsPassed(Date date, Errors errors) {
		if (date.after(new Date())) {
			/*
			 * En cas d'erreur lors de la validation on fait remonté un message
			 * qui pourrat être utilisé dans une vue pour afficher une erreur.
			 */
			errors.rejectValue("date", "myEntity.validation.dateNotPassed");
		}
	}
}
