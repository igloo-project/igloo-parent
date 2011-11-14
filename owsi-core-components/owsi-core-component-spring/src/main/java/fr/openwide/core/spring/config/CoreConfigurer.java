package fr.openwide.core.spring.config;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.spring.util.StringUtils;

public class CoreConfigurer extends CorePropertyPlaceholderConfigurer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CoreConfigurer.class);

	private static final String VERSION_PROPERTY = "version";
	
	private static final String CONFIGURATION_TYPE_PROPERTY = "configurationType";
	private static final String CONFIGURATION_TYPE_DEVELOPMENT = "development";
	private static final String CONFIGURATION_TYPE_DEPLOYMENT = "deployment";
	
	private static final String TMP_PATH = "tmp.path";
	private static final String IMAGE_MAGICK_CONVERT_BINARY_PATH = "imageMagick.convertBinary.path";
	
	private static final String LOCALE_AVAILABLE_LOCALES = "locale.availableLocales";
	private static final String LOCALE_DEFAULT = "locale.default";
	
	private static final String HIBERNATE_SEARCH_REINDEX_BATCH_SIZE = "hibernate.search.reindex.batchSize";
	private static final String HIBERNATE_SEARCH_REINDEX_FETCHING_THREADS = "hibernate.search.reindex.fetchingThreads";
	private static final String HIBERNATE_SEARCH_REINDEX_LOAD_THREADS = "hibernate.search.reindex.loadThreads";
	
	public String getVersion() {
		return getPropertyAsString(VERSION_PROPERTY);
	}

	public String getConfigurationType() {
		String configurationType = getPropertyAsString(CONFIGURATION_TYPE_PROPERTY);
		if (CONFIGURATION_TYPE_DEVELOPMENT.equals(configurationType) || CONFIGURATION_TYPE_DEPLOYMENT.equals(configurationType)) {
			return configurationType;
		} else {
			throw new IllegalStateException("Configuration type should be either development or deployment");
		}
	}
	
	public boolean isConfigurationTypeDevelopment() {
		return CONFIGURATION_TYPE_DEVELOPMENT.equals(getConfigurationType());
	}
	
	public File getTmpDirectory() {
		String tmpPath = getPropertyAsString(TMP_PATH);
		
		if (StringUtils.hasText(tmpPath)) {
			File tmpDirectory = new File(tmpPath);
			
			if (tmpDirectory.isDirectory() && tmpDirectory.canWrite()) {
				return tmpDirectory;
			}
			if (!tmpDirectory.exists()) {
				try {
					FileUtils.forceMkdir(tmpDirectory);
					return tmpDirectory;
				} catch (Exception e) {
					throw new IllegalStateException("The tmp directory " + tmpPath + " does not exist and it is impossible to create it.");
				}
			}
		}
		throw new IllegalStateException("The tmp directory " + tmpPath + " is not writable.");
	}
	
	public File getImageMagickConvertBinary() {
		String imageMagickConvertBinary = getPropertyAsString(IMAGE_MAGICK_CONVERT_BINARY_PATH);
		
		if (StringUtils.hasText(imageMagickConvertBinary)) {
			return new File(imageMagickConvertBinary);
		} else {
			return null;
		}
	}
	
	public Set<Locale> getAvailableLocales() {
		List<String> localesAsString = getPropertyAsStringList(LOCALE_AVAILABLE_LOCALES);
		Set<Locale> locales = new HashSet<Locale>(localesAsString.size());
		
		for (String localeAsString : localesAsString) {
			try {
				locales.add(LocaleUtils.toLocale(localeAsString));
			} catch (Exception e) {
				LOGGER.error(String.format(
						"%1$s string from %2$s cannot be mapped to Locale, ignored",
						localeAsString, LOCALE_AVAILABLE_LOCALES
				));
			}
		}
		return locales;
	}
	
	public Locale getDefaultLocale() {
		Locale defaultLocale = LocaleUtils.toLocale(getPropertyAsString(LOCALE_DEFAULT));
		if (defaultLocale == null) {
			defaultLocale = Locale.US;
		}
		return defaultLocale;
	}
	
	/**
	 * <p> Le but est de partir d'une locale
	 * quelconque et d'aboutir obligatoirement à une locale provenant de la liste
	 * <i>locale.availableLocales</i>.</p>
	 * 
	 * <p>Le mapping se fait ainsi :
	 * <ul>
	 * <li>si la locale est dans locale.availableLocales, alors on utilise la locale</li>
	 * <li>sinon on vérifié si le <i>Language</i> de la locale correspond à un <i>Language</i>
	 * dans locale.availableLocales ; alors on utilise la locale correspondante
	 * </li>
	 * <li>sinon on utilise <i>locale.default</i></li>
	 * </ul>
	 * </p>
	 * 
	 * <p>Exemple :<br/>
	 * <code>locale.availableLocales=fr en</code><br/>
	 * <code>locale.default=fr</code><br/>
	 * <br/>
	 * Les résultats seront les suivants
	 * <ul>
	 * <li>fr -> fr (correspondance exacte)</li>
	 * <li>fr_FR -> fr (correspondance sur Language)</li>
	 * <li>en -> en (correspondance exacte)</li>
	 * <li>en_US -> en (correspondance sur Language)</li>
	 * <li>ar_SA -> fr (défaut)</li>
	 * </ul>
	 * </p>
	 *
	 * @param locale
	 * @return locale, not null, from locale.availableLocales
	 */
	public Locale toAvailableLocale(Locale locale) {
		if (locale != null) {
			Set<Locale> availableLocales = getAvailableLocales();
			if (availableLocales.contains(locale)) {
				return locale;
			} else {
				for (Locale availableLocale : availableLocales) {
					if (availableLocale.getLanguage().equals(locale.getLanguage())) {
						return availableLocale;
					}
				}
			}
		}
		
		// default locale from configuration
		return getDefaultLocale();
	}
	
	public int getHibernateSearchReindexBatchSize() {
		return getPropertyAsInteger(HIBERNATE_SEARCH_REINDEX_BATCH_SIZE, 10);
	}
	
	public int getHibernateSearchReindexFetchingThreads() {
		return getPropertyAsInteger(HIBERNATE_SEARCH_REINDEX_FETCHING_THREADS, 2);
	}
	
	public int getHibernateSearchReindexLoadThreads() {
		return getPropertyAsInteger(HIBERNATE_SEARCH_REINDEX_LOAD_THREADS, 2);
	}
	
}