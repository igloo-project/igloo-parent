package fr.openwide.core.basicapp.web.application.common.converter;

import java.util.Locale;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.converter.AbstractConverter;
import org.springframework.util.StringUtils;

import fr.openwide.core.basicapp.core.business.common.model.CodePostal;
import fr.openwide.core.basicapp.web.application.common.validator.CodePostalValidator;

public final class CodePostalConverter extends AbstractConverter<CodePostal> {

	private static final long serialVersionUID = 7575610087030468757L;

	@Override
	public CodePostal convertToObject(String value, Locale locale) throws ConversionException {
		String trimmedValue = StringUtils.trimAllWhitespace(value);
		if (StringUtils.hasText(trimmedValue)) {
			if (!CodePostalValidator.getInstance().isValid(trimmedValue)) {
				throw newConversionException("Invalid post code format", value, locale)
						.setResourceKey("common.validator.codePostal");
			}
			return new CodePostal(trimmedValue);
		}
		return null;
	}

	@Override
	public String convertToString(CodePostal value, Locale locale) {
		if (value == null) {
			return null;
		}
		return value.getValue();
	}

	@Override
	protected Class<CodePostal> getTargetType() {
		return CodePostal.class;
	}

}
