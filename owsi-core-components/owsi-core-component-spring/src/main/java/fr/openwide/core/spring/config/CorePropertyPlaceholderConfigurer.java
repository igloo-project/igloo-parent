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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurablePropertyResolver;
import org.springframework.core.env.PropertyResolver;

import fr.openwide.core.spring.util.StringUtils;

/**
 * <p>Gère la récupération des propriétés de configuration.</p>
 * 
 * <p>Ajout de fonctionnalités supplémentaires par rapport au configurer Spring permettant :
 *  - la résolution via l'appel aux méthodes getPropertyAsXXXX(propertyName) (avec substitution des placeholders)</p>
 * 
 * @author Open Wide
 */
public class CorePropertyPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(CorePropertyPlaceholderConfigurer.class);
	
	/**
	 * Espace séparant deux valeurs dans une liste.
	 */
	public static final String SEPARATOR = " ";
	
	/**
	 * Propriétés de configuration.
	 */
	private PropertyResolver propertyResolver;
	
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
		return getPropertyAsString(key, null);
	}
	
	/**
	 * Retourne une propriété spécifique à partir de sa clé sous la forme d'une
	 * chaîne. Retourne la valeur par défaut si on récupère null.
	 * 
	 * /!\ bien renvoyer la chaîne vide si la chaîne est vide : il ne faut pas
	 * envoyer la valeur par défaut dans ce cas.
	 * 
	 * @param key la clé
	 * @param defaultValue la valeur par défaut
	 * @return chaîne de la propriété
	 */
	protected String getPropertyAsString(String key, String defaultValue) {
		Object property = getProperty(key);
		if(property == null) {
			return defaultValue;
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
		Integer integerProperty = defaultValue;
		String stringProperty = getPropertyAsString(key);
		
		if (!StringUtils.hasText(stringProperty)) {
			LOGGER.warn("La propriété " + key + " n'est pas définie : utilisation de la valeur par défaut.");
			return integerProperty;
		}
		try {
			integerProperty = Integer.parseInt(stringProperty);
		} catch(NumberFormatException e) {
			LOGGER.warn("La valeur de la propriété " + key + " n'est pas un entier valide : utilisation de la valeur par défaut.", e);
		}
		return integerProperty;
	}

	/**
	 * Retourne une propriété spécifique à partir de sa clé La propriété
	 * retournée n'est pas castée.
	 * 
	 * @param key la clé
	 * @return l'objet propriété
	 */
	private Object getProperty(String key) {
		String rawValue = propertyResolver.getProperty(key);
		
		if (rawValue != null) {
			return propertyResolver.resolvePlaceholders(rawValue);
		} else {
			return null;
		}
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess,
			ConfigurablePropertyResolver propertyResolver) throws BeansException {
		this.propertyResolver = propertyResolver;
		
		// on garde les traitements dans la classe parente
		super.processProperties(beanFactoryToProcess, propertyResolver);
	}

}