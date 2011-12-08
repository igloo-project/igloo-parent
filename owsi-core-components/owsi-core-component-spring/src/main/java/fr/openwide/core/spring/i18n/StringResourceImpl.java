package fr.openwide.core.spring.i18n;

import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import fr.openwide.core.spring.config.CoreConfigurer;

/**
 *
 * @author hduprat
 */
@Service("stringResource")
public abstract class StringResourceImpl extends ReloadableResourceBundleMessageSource implements IStringResource {

	public static Locale DEFAULT_LOCALE;
	
	@Autowired
	public StringResourceImpl(CoreConfigurer configurer) {
		super();
		setDefaultEncoding("utf-8");
		DEFAULT_LOCALE = configurer.getDefaultLocale();
	}
	
	public Locale getDefaultLocale() {
		return DEFAULT_LOCALE;
	}
	
	public String getString(String key) {
		return getString(key, getDefaultLocale());
	}
	
	public String getString(String key, Locale locale) {
		return getString(key, null, locale);
	}
	
	public String getString(String key, Map<String, Object> variableMap) {
		return getString(key, variableMap, getDefaultLocale());
	}
	
	public String getString(String key, Map<String, Object> variableMap, Locale locale) {
		String value = getMessage(key, null, null, locale);
		if (value == null) {
			return null;
		} else {
			return interpolate(value, variableMap, locale);
		}
	}
	
	protected String interpolate(final String string, final Map<String, Object> variableMap, final Locale locale) {
		final StringBuffer buffer = new StringBuffer();
		
		// For each occurrence of "${"
		int start;
		int pos = 0;
		
		while ((start = string.indexOf("${", pos)) != -1)
		{
			// Append text before possible variable
			buffer.append(string.substring(pos, start));
			
			// Position is now where we found the "${"
			pos = start;
			
			// Get start and end of variable name
			final int startVariableName = start + 2;
			final int endVariableName = string.indexOf('}', startVariableName);
			
			// Found a close brace?
			if (endVariableName != -1)
			{
				// Get variable name inside brackets
				final String variableName = string.substring(startVariableName, endVariableName);
				
				// Get value of variable
				final String value = getValue(variableMap, variableName, locale);
				
				// If there's no value
				if (value == null)
				{
					// Leave variable uninterpolated, allowing multiple
					// interpolators to
					// do their work on the same string
					buffer.append("${" + variableName + "}");
				}
				else
				{
					// Append variable value
					buffer.append(value);
				}
				
				// Move past variable
				pos = endVariableName + 1;
			}
			else
			{
				break;
			}
		}
		
		// Append anything that might be left
		if (pos < string.length())
		{
			buffer.append(string.substring(pos));
		}
		
		// Convert result to String
		return buffer.toString();
	}
	
	protected String getValue(Map<String, Object> variableMap, String variableName, Locale locale) {
		if (variableMap == null) {
			return null;
		}
			
		Object variable = variableMap.get(variableName);
		
		if (Enum.class.isAssignableFrom(variable.getClass())) {
			Enum<?> enumVariable = (Enum<?>) variable;
			
			StringBuilder enumKey = new StringBuilder();
			
			enumKey.append(enumVariable.getClass().getSimpleName());
			enumKey.append(".");
			enumKey.append(enumVariable.name());
			
			return getMessage(enumKey.toString(), null, enumVariable.name(), locale);
		} else {
			return variable.toString();
		}
	}
}
