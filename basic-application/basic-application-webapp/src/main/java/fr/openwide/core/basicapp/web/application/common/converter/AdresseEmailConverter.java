package fr.openwide.core.basicapp.web.application.common.converter;

import java.util.Locale;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.converter.AbstractConverter;
import org.springframework.util.StringUtils;

import fr.openwide.core.basicapp.core.business.common.model.AdresseEmail;
import fr.openwide.core.commons.util.validator.AnyTldEmailAddressValidator;

public final class AdresseEmailConverter extends AbstractConverter<AdresseEmail> {

	private static final long serialVersionUID = -2929883038769154563L;

	@Override
	public AdresseEmail convertToObject(String value, Locale locale) throws ConversionException {
		String trimmedValue = StringUtils.trimAllWhitespace(value);
		if (StringUtils.hasText(trimmedValue)) {
			if (!AnyTldEmailAddressValidator.getInstance().isValid(trimmedValue)) {
				throw newConversionException("Invalid email format", value, locale)
						.setResourceKey("common.validator.email");
			}
			return new AdresseEmail(trimmedValue);
		}
		return null;
	}

	@Override
	public String convertToString(AdresseEmail value, Locale locale) {
		if (value == null) {
			return null;
		}
		return value.getValue();
	}
	
	@Override
	protected Class<AdresseEmail> getTargetType() {
		return AdresseEmail.class;
	}

}
