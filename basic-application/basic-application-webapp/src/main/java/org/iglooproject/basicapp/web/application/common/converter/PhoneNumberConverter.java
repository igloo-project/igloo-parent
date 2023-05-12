package org.iglooproject.basicapp.web.application.common.converter;

import java.util.Locale;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.converter.AbstractConverter;
import org.iglooproject.basicapp.core.business.common.model.PhoneNumber;
import org.iglooproject.commons.util.validator.PermissivePhoneNumberValidator;
import org.springframework.util.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public final class PhoneNumberConverter extends AbstractConverter<PhoneNumber> {

	private static final long serialVersionUID = 7575610087030468757L;

	@Override
	public PhoneNumber convertToObject(String value, Locale locale) throws ConversionException {
		String trimmedValue = StringUtils.trimAllWhitespace(value);
		if (StringUtils.hasText(trimmedValue)) {
			if (!PermissivePhoneNumberValidator.getInstance().isValid(trimmedValue)) {
				throw newConversionException("Invalid phone number format", value, locale)
					.setResourceKey("common.validator.phoneNumber");
			}
			return PhoneNumber.buildClean(trimmedValue);
		}
		return null;
	}

	@Override
	public String convertToString(PhoneNumber value, Locale locale) {
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
	protected Class<PhoneNumber> getTargetType() {
		return PhoneNumber.class;
	}

}
