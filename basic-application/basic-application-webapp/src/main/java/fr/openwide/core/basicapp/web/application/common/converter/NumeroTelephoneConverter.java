package fr.openwide.core.basicapp.web.application.common.converter;

import java.util.Locale;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.converter.AbstractConverter;
import org.springframework.util.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import fr.openwide.core.basicapp.core.business.common.model.NumeroTelephone;
import fr.openwide.core.commons.util.validator.PermissivePhoneNumberValidator;

public class NumeroTelephoneConverter extends AbstractConverter<NumeroTelephone> {

	private static final long serialVersionUID = 7575610087030468757L;

	@Override
	public NumeroTelephone convertToObject(String value, Locale locale) throws ConversionException {
		String trimmedValue = StringUtils.trimAllWhitespace(value);
		if (StringUtils.hasText(trimmedValue)) {
			if (!PermissivePhoneNumberValidator.getInstance().isValid(trimmedValue)) {
				throw newConversionException("Invalid phone number format", value, locale)
						.setResourceKey("common.validator.numeroTelephone");
			}
			return NumeroTelephone.buildClean(trimmedValue);
		}
		return null;
	}

	@Override
	public String convertToString(NumeroTelephone value, Locale locale) {
		if (value == null) {
			return null;
		}
		
		if (value.isLocalFrench()) {
			// Si le numéro est français, on ajoute un espace tous les 2 caractères
			return Joiner.on(" ").join(Splitter.fixedLength(2).split(value.getValue()));
		} else {
			// Sinon on le renvoie brut
			return value.getValue();
		}
	}
	
	@Override
	protected Class<NumeroTelephone> getTargetType() {
		return NumeroTelephone.class;
	}

}
