package fr.openwide.hibernate.util.spring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import fr.openwide.hibernate.util.StringUtils;

public class CustomPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	public static final String SEPARATOR = " ";
	
	private Properties properties;

	protected void loadProperties(Properties properties) throws IOException {
		super.loadProperties(properties);
		this.properties = properties;
	}

	private Object getProperty(String key) {
		Object value = properties.get(key);
		// If the value contain a ${, this means it as to be _placeholdered_
		if (value != null && value.toString().contains("${"))  {
			value = parseStringValue(value.toString(), properties, new HashSet<String>());
		}
		return value;
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
	
	/**
	 * Exposed in this class to be able to access the method from the
	 * PropertyInjectorBeanProcessor
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String parseStringValue(String strVal, Properties props,
			Set visitedPlaceholders) throws BeanDefinitionStoreException {
		return super.parseStringValue(strVal, props, visitedPlaceholders);
	}

}