package fr.openwide.commons.spring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import fr.openwide.commons.string.StringUtils;

/**
 * Ajout de fonctionnalités supplémentaires par rapport au configurer Spring permettant :
 *  - la résolution via l'appel aux méthodes getPropertyAsXXXX(propertyName) (avec substitution des placeholders)
 *  - 
 * 
 * @author jgonzalez
 */
public class OwPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	public static final String SEPARATOR = " ";
	
	private static final String CONFIGURATION_TYPE = "configurationType";
	
	static final String CONFIGURATION_TYPE_DEVELOPMENT = "development";
	
	private Properties properties;
	
	public boolean isConfigurationTypeDevelopment() {
		return CONFIGURATION_TYPE_DEVELOPMENT.equals(getPropertyAsString(CONFIGURATION_TYPE));
	}
	
	protected void loadProperties(Properties properties) throws IOException {
		super.loadProperties(properties);
		this.properties = properties;
	}
	
	protected Boolean getPropertyAsBoolean(String key) {
		return Boolean.valueOf(getPropertyAsString(key));
	}
	
	protected String getPropertyAsString(String key) {
		Object property = getProperty(key);
		if(property == null) {
			return null;
		} else {
			return property.toString();
		}
	}
	
	protected List<String> getPropertyAsStringList(String key) {
		String property = getPropertyAsString(key);
		if(property == null) {
			return new ArrayList<String>(0);
		} else {
			return StringUtils.splitAsList(property, SEPARATOR);
		}
	}
	
	protected String[] getPropertyAsStringArray(String key) {
		String property = getPropertyAsString(key);
		if(property == null) {
			return new String[0];
		} else {
			return StringUtils.delimitedListToStringArray(property, SEPARATOR);
		}
	}
	
	protected Integer getPropertyAsInteger(String key, Integer defaultValue) {
		Integer property = defaultValue;
		try {
			property = Integer.parseInt(getPropertyAsString(key));
		} catch(NumberFormatException e) {
		}
		return property;
	}

	private Object getProperty(String key) {
		Object value = properties.get(key);
		// If the value contain a ${, this means it as to be _placeholdered_
		if (value != null && value.toString().contains("${"))  {
			value = resolvePlaceholder(value.toString(), properties);
		}
		return value;
	}

}