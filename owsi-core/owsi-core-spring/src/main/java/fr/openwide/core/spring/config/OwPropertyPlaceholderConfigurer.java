/*
 * Copyright (C) 2009-2010 Open Wide
 * Contact: contact@openwide.fr
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.openwide.core.spring.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import fr.openwide.core.spring.util.StringUtils;

/**
 * <p>Gère la récupération des propriétés de configuration.</p>
 * 
 * <p>Ajout de fonctionnalités supplémentaires par rapport au configurer Spring permettant :
 *  - la résolution via l'appel aux méthodes getPropertyAsXXXX(propertyName) (avec substitution des placeholders)</p>
 * 
 * @author Open Wide
 */
public class OwPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	private static final Log LOGGER = LogFactory.getLog(OwPropertyPlaceholderConfigurer.class);
	
	/**
	 * Espace séparant deux valeurs dans une liste.
	 */
	public static final String SEPARATOR = " ";
	
	/**
	 * Propriété définissant le type de configuration
	 */
	private static final String CONFIGURATION_TYPE = "configurationType";
	
	/**
	 * Type de configuration : development / deployment
	 */
	static final String CONFIGURATION_TYPE_DEVELOPMENT = "development";
	
	/**
	 * Propriétés de configuration.
	 */
	private Properties properties;
	
	/**
	 * Vérifie le type de configuration
	 */
	public boolean isConfigurationTypeDevelopment() {
		return CONFIGURATION_TYPE_DEVELOPMENT.equals(getPropertyAsString(CONFIGURATION_TYPE));
	}
	
	/**
	 * Charge les propriétés.
	 * 
	 * @param properties les propriétés
	 */
	protected void loadProperties(Properties properties) throws IOException {
		super.loadProperties(properties);
		this.properties = properties;
	}
	
	/**
	 * Retourne une propriété spécifique à partir de sa clé sous la forme d'un
	 * booléen.
	 * 
	 * @param key
	 * @return booléen de la propriété
	 */
	protected Boolean getPropertyAsBoolean(String key) {
		return Boolean.valueOf(getPropertyAsString(key));
	}
	
	/**
	 * Retourne une propriété spécifique à partir de sa clé sous la forme d'une
	 * chaîne.
	 * 
	 * @param key la clé
	 * @return chaîne de la propriété
	 */
	protected String getPropertyAsString(String key) {
		Object property = getProperty(key);
		if(property == null) {
			return null;
		} else {
			return property.toString();
		}
	}
	
	/**
	 * Retourne une propriété sous la forme d'une liste de chaînes La séparation
	 * se fait sur le caractère espace.
	 * 
	 * @param key la clé
	 * @return liste de chaînes
	 */
	protected List<String> getPropertyAsStringList(String key) {
		String property = getPropertyAsString(key);
		if(property == null) {
			return new ArrayList<String>(0);
		} else {
			return StringUtils.splitAsList(property, SEPARATOR);
		}
	}
	
	/**
	 * Retourne une propriété sous la forme d'un tableau de chaînes La
	 * séparation se fait sur le caractère espace.
	 * 
	 * @param key la clé
	 * @return liste de chaînes
	 */
	protected String[] getPropertyAsStringArray(String key) {
		String property = getPropertyAsString(key);
		if(property == null) {
			return new String[0];
		} else {
			return StringUtils.delimitedListToStringArray(property, SEPARATOR);
		}
	}
	
	/**
	 * Retourne une propriété sous la forme d'un entier La séparation se fait
	 * sur le caractère espace.
	 * 
	 * @param key la clé
	 * @param defaultValue valeur par défaut
	 * @return valeur de la propriété sous la forme d'un entier ou defaultValue
	 *         si la valeur de la propriété n'est pas un entier valide
	 */
	protected Integer getPropertyAsInteger(String key, Integer defaultValue) {
		Integer property = defaultValue;
		try {
			property = Integer.parseInt(getPropertyAsString(key));
		} catch(NumberFormatException e) {
			LOGGER.warn("La valeur de la propriété " + key + " n'est pas un entier valide.", e);
		}
		return property;
	}

	/**
	 * Retourne une propriété spécifique à partir de sa clé La propriété
	 * retournée n'est pas castée.
	 * 
	 * @param key la clé
	 * @return l'objet propriété
	 */
	@SuppressWarnings("deprecation")
	private Object getProperty(String key) {
		Object value = properties.get(key);
		// If the value contain a ${, this means it as to be _placeholdered_
		if (value != null && value.toString().contains(PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_PREFIX))  {
			value = parseStringValue(value.toString(), properties, new HashSet<String>());
		}
		return value;
	}

}