package fr.openwide.core.spring.i18n;

import java.util.Locale;
import java.util.Map;

public interface IStringResource {

	public Locale getDefaultLocale();
	
	public String getString(String key);
	
	public String getString(String key, Locale locale);
	
	public String getString(String key, Map<String, Object> variableMap);
	
	public String getString(String key, Map<String, Object> variableMap, Locale locale);
}
