package fr.openwide.core.spring.config;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.spring.util.StringUtils;

@Deprecated
public abstract class AbstractConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConfigurer.class);
	
	@Autowired
	private CorePropertyPlaceholderConfigurer propertySource;
	
	/**
	 * Espace séparant deux valeurs dans une liste.
	 */
	@Deprecated
	public static final String SEPARATOR = " ";
	
	/**
	 * Retourne une propriété spécifique à partir de sa clé sous la forme d'un
	 * booléen.
	 * 
	 * @param key
	 * @return booléen de la propriété
	 */
	@Deprecated
	protected Boolean getPropertyAsBoolean(String key) {
		return Boolean.valueOf(StringUtils.trimWhitespace(getPropertyAsString(key)));
	}
	
	/**
	 * Retourne une propriété spécifique à partir de sa clé sous la forme d'une
	 * chaîne.
	 * 
	 * @param key la clé
	 * @return chaîne de la propriété
	 */
	@Deprecated
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
	@Deprecated
	protected String getPropertyAsString(String key, String defaultValue) {
		Object property = propertySource.getProperty(key);
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
	@Deprecated
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
	@Deprecated
	protected String[] getPropertyAsStringArray(String key) {
		String property = getPropertyAsString(key);
		if(property == null) {
			return new String[0];
		} else {
			return StringUtils.delimitedListToStringArray(property, SEPARATOR);
		}
	}
	
	/**
	 * Retourne une propriété sous la forme d'un entier.
	 * 
	 * @param key la clé
	 * @param defaultValue valeur par défaut
	 * @return valeur de la propriété sous la forme d'un entier ou defaultValue
	 *         si la valeur de la propriété n'est pas un entier valide
	 */
	@Deprecated
	protected Integer getPropertyAsInteger(String key, Integer defaultValue) {
		return getPropertyAsInteger(key, defaultValue, null, null);
	}
	
	/**
	 * Retourne une propriété sous la forme d'un entier.
	 * 
	 * @param key la clé
	 * @param defaultValue valeur par défaut
	 * @param minValue valeur minimale
	 * @param maxValue valeur maximale
	 * @return valeur de la propriété sous la forme d'un entier
	 *         ou defaultValue si la valeur de la propriété n'est pas un entier valide
	 *         ou minValue si la valeur est inférieure à la valeur minimale
	 *         ou maxValue si la valeur est supérieure à la valeur maximale
	 */
	@Deprecated
	protected Integer getPropertyAsInteger(String key, Integer defaultValue, Integer minValue, Integer maxValue) {
		Integer integerProperty = defaultValue;
		String stringProperty = StringUtils.trimWhitespace(getPropertyAsString(key));
		
		if (!StringUtils.hasText(stringProperty)) {
			LOGGER.warn("La propriété " + key + " n'est pas définie : utilisation de la valeur par défaut.");
			return integerProperty;
		}
		try {
			integerProperty = Integer.parseInt(stringProperty);
		} catch(NumberFormatException e) {
			throw new IllegalStateException("La valeur de la propriété " + key + " n'est pas un entier valide : utilisation de la valeur par défaut.", e);
		}
		if (minValue != null && integerProperty < minValue) {
			LOGGER.warn("La propriété " + key + " est inférieure à la valeur minimale : utilisation de cette valeur minimale.");
			return minValue;
		} else if (maxValue != null && integerProperty > maxValue) {
			LOGGER.warn("La propriété " + key + " est supérieure à la valeur maximale : utilisation de cette valeur maximale.");
			return maxValue;
		}
		return integerProperty;
	}
	
	/**
	 * Retourne une propriété sous la forme d'un BigDecimal.
	 * 
	 * @param key la clé
	 * @param defaultValue valeur par défaut
	 * @return valeur de la propriété sous la forme d'un BigDecimal
	 *         ou defaultValue si la valeur de la propriété n'est pas un BigDecimal valide
	 */
	@Deprecated
	protected BigDecimal getPropertyAsBigDecimal(String key, BigDecimal defaultValue) {
		return getPropertyAsBigDecimal(key, defaultValue, null, null);
	}
	
	/**
	 * Retourne une propriété sous la forme d'un BigDecimal.
	 * 
	 * @param key la clé
	 * @param defaultValue valeur par défaut
	 * @param minValue valeur minimale
	 * @param maxValue valeur maximale
	 * @return valeur de la propriété sous la forme d'un BigDecimal
	 *         ou defaultValue si la valeur de la propriété n'est pas un BigDecimal valide
	 *         ou minValue si la valeur est inférieure à la valeur minimale
	 *         ou maxValue si la valeur est supérieure à la valeur maximale
	 */
	@Deprecated
	protected BigDecimal getPropertyAsBigDecimal(String key, BigDecimal defaultValue, BigDecimal minValue, BigDecimal maxValue) {
		BigDecimal bigDecimalProperty = defaultValue;
		String stringProperty = StringUtils.trimWhitespace(getPropertyAsString(key));
		
		if (!StringUtils.hasText(stringProperty)) {
			LOGGER.warn("La propriété " + key + " n'est pas définie : utilisation de la valeur par défaut.");
			return bigDecimalProperty;
		}
		try {
			bigDecimalProperty = new BigDecimal(stringProperty);
		} catch(Exception e) {
			throw new IllegalStateException("La valeur de la propriété " + key + " n'est pas un bigDecimalProperty valide : utilisation de la valeur par défaut.", e);
		}
		
		if (minValue != null && bigDecimalProperty.compareTo(minValue) < 0) {
			LOGGER.warn("La propriété " + key + " est inférieure à la valeur minimale : utilisation de cette valeur minimale.");
			return minValue;
		} else if (maxValue != null && bigDecimalProperty.compareTo(maxValue) > 0) {
			LOGGER.warn("La propriété " + key + " est supérieure à la valeur maximale : utilisation de cette valeur maximale.");
			return maxValue;
		}
		return bigDecimalProperty;
	}
	
	/**
	 * Retourne une propriété sous la forme d'une enum.
	 * 
	 * @param key la clé
	 * @param defaultValue valeur par défaut
	 * @return valeur de la propriété sous la forme d'une enum ou defaultValue
	 *         si la valeur de la propriété ne correspond pas à une instance de l'enum
	 */
	@Deprecated
	protected <E extends Enum<E>> E getPropertyAsEnum(String key, Class<E> enumType, E defaultValue) {
		E enumProperty = defaultValue;
		String stringProperty = StringUtils.trimWhitespace(getPropertyAsString(key));
		
		if (!StringUtils.hasText(stringProperty)) {
			LOGGER.warn("La propriété " + key + " n'est pas définie : utilisation de la valeur par défaut.");
			return enumProperty;
		}
		try {
			enumProperty = Enum.valueOf(enumType, stringProperty);
		} catch(Exception e) {
			throw new IllegalStateException("La valeur de la propriété " + key + " n'est pas une enum valide : utilisation de la valeur par défaut.", e);
		}
		return enumProperty;
	}
	
	/**
	 * Retourne un répertoire dans lequel on est sûr de pouvoir écrire à partir du path déclaré dans la propriété.
	 * 
	 * @param key la clé
	 * @return un répertoire dans lequel on est sûr de pouvoir écrire
	 */
	@Deprecated
	protected File getPropertyAsWritableDirectory(String key) {
		String path = StringUtils.trimWhitespace(getPropertyAsString(key));
		
		if (StringUtils.hasText(path) && !"/".equals(path)) {
			File directory = new File(path);
			
			if (directory.isDirectory() && directory.canWrite()) {
				return directory;
			}
			if (!directory.exists()) {
				try {
					FileUtils.forceMkdir(directory);
					return directory;
				} catch (Exception e) {
					throw new IllegalStateException("The directory " + key + " - " + path + " does not exist and it is impossible to create it.");
				}
			}
		}
		throw new IllegalStateException("The tmp directory " + key + " - " + path + " is not writable.");
	}

}
