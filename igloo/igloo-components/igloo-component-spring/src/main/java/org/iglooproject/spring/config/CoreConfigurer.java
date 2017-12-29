package org.iglooproject.spring.config;

import static org.iglooproject.spring.property.SpringPropertyIds.AVAILABLE_LOCALES;
import static org.iglooproject.spring.property.SpringPropertyIds.CONFIGURATION_TYPE;
import static org.iglooproject.spring.property.SpringPropertyIds.DEFAULT_LOCALE;
import static org.iglooproject.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_DISABLED_RECIPIENT_FALLBACK;
import static org.iglooproject.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_FROM;
import static org.iglooproject.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_RECIPIENTS_FILTERED;
import static org.iglooproject.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_SUBJECT_PREFIX;
import static org.iglooproject.spring.property.SpringPropertyIds.NOTIFICATION_TEST_EMAILS;
import static org.iglooproject.spring.property.SpringPropertyIds.IGLOO_VERSION;
import static org.iglooproject.spring.property.SpringPropertyIds.TMP_PATH;
import static org.iglooproject.spring.property.SpringPropertyIds.VERSION;
import static org.iglooproject.spring.property.SpringSecurityPropertyIds.PASSWORD_EXPIRATION_DAYS;
import static org.iglooproject.spring.property.SpringSecurityPropertyIds.PASSWORD_HISTORY_COUNT;
import static org.iglooproject.spring.property.SpringSecurityPropertyIds.PASSWORD_RECOVERY_REQUEST_EXPIRATION_MINUTES;
import static org.iglooproject.spring.property.SpringSecurityPropertyIds.PASSWORD_RECOVERY_REQUEST_TOKEN_RANDOM_COUNT;
import static org.iglooproject.spring.property.SpringSecurityPropertyIds.PASSWORD_SALT;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.lucene.search.BooleanQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import org.iglooproject.spring.config.util.TaskQueueStartMode;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.StringUtils;

/**
 * @deprecated Use {@link IPropertyService} instead
 */
@Deprecated
public class CoreConfigurer extends AbstractConfigurer {
	
	@Autowired
	private IPropertyService propertyService;
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.VERSION)
	 */
	@Deprecated
	public String getVersion() {
		return propertyService.get(VERSION);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.IGLOO_VERSION)
	 */
	@Deprecated
	public String getIglooVersion() {
		return propertyService.get(IGLOO_VERSION);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.CONFIGURATION_TYPE)
	 */
	@Deprecated
	public String getConfigurationType() {
		return propertyService.get(CONFIGURATION_TYPE);
	}

	/**
	 * @deprecated Use propertyService.isConfigurationTypeDevelopment()
	 */
	@Deprecated
	public boolean isConfigurationTypeDevelopment() {
		return propertyService.isConfigurationTypeDevelopment();
	}

	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.MIGRATION_LOGGING_MEMORY)
	 */
	@Deprecated
	public boolean isMigrationLoggingMemory() {
		return getPropertyAsBoolean("migration.logging.memory");
	}

	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.TMP_PATH)
	 */
	@Deprecated
	public File getTmpDirectory() {
		return propertyService.get(TMP_PATH);
	}

	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.LUCENE_BOOLEANQUERY_MAX_CLAUSE_COUNT)
	 */
	@Deprecated
	public int getLuceneBooleanQueryMaxClauseCount() {
		return getPropertyAsInteger("lucene.booleanQuery.maxClauseCount", BooleanQuery.getMaxClauseCount());
	}

	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.IMAGE_MAGICK_CONVERT_BINARY_PATH)
	 */
	@Deprecated
	public File getImageMagickConvertBinary() {
		String imageMagickConvertBinary = getPropertyAsString("imageMagick.convertBinary.path", "/usr/bin/convert");
		
		if (StringUtils.hasText(imageMagickConvertBinary)) {
			return new File(imageMagickConvertBinary);
		} else {
			return null;
		}
	}

	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.AVAILABLE_LOCALES)
	 */
	@Deprecated
	public Set<Locale> getAvailableLocales() {
		return propertyService.get(AVAILABLE_LOCALES);
	}

	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.DEFAULT_LOCALE)
	 */
	@Deprecated
	public Locale getDefaultLocale() {
		return propertyService.get(DEFAULT_LOCALE);
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
	 * @deprecated Use propertyService.toAvailableLocale(locale);
	 */
	@Deprecated
	public Locale toAvailableLocale(Locale locale) {
		return propertyService.toAvailableLocale(locale);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.HIBERNATE_SEARCH_REINDEX_BATCH_SIZE)
	 */
	@Deprecated
	public int getHibernateSearchReindexBatchSize() {
		return getPropertyAsInteger("hibernate.search.reindex.batchSize", 25);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.HIBERNATE_SEARCH_REINDEX_LOAD_THREADS)
	 */
	@Deprecated
	public int getHibernateSearchReindexLoadThreads() {
		return getPropertyAsInteger("hibernate.search.reindex.loadThreads", 8);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.NOTIFICATION_MAIL_FROM)
	 */
	@Deprecated
	public String getNotificationMailFrom() {
		return propertyService.get(NOTIFICATION_MAIL_FROM);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.NOTIFICATION_MAIL_SUBJECT_PREFIX)
	 */
	@Deprecated
	public String getNotificationMailSubjectPrefix() {
		return propertyService.get(NOTIFICATION_MAIL_SUBJECT_PREFIX);
	}
	
	/**
	 * @deprecated Use (propertyService.isConfigurationTypeDevelopment() || propertyService.get(CorePropertyIds.NOTIFICATION_MAIL_RECIPIENTS_FILTERED))
	 */
	@Deprecated
	public boolean isNotificationMailRecipientsFiltered() {
		return propertyService.isConfigurationTypeDevelopment() || propertyService.get(NOTIFICATION_MAIL_RECIPIENTS_FILTERED);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.NOTIFICATION_TEST_EMAILS)
	 */
	@Deprecated
	public List<String> getNotificationTestEmails() {
		return propertyService.get(NOTIFICATION_TEST_EMAILS);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.NOTIFICATION_MAIL_DISABLED_RECIPIENT_FALLBACK)
	 */
	@Deprecated
	public List<String> getDisabledRecipientFallback() {
		return propertyService.get(NOTIFICATION_MAIL_DISABLED_RECIPIENT_FALLBACK);
	}
	
	/**
	 * @deprecated Use propertyService.get(JpaMoreTaskPropertyIds.STOP_TIMEOUT)
	 */
	@Deprecated
	public int getTaskStopTimeout() {
		return getPropertyAsInteger("task.stop.timeout", 70000);
	}
	

	/**
	 * @deprecated Use propertyService.get(JpaMoreTaskPropertyIds.START_MODE)
	 */
	@Deprecated
	public TaskQueueStartMode getTaskQueueStartMode() {
		return getPropertyAsEnum("task.startMode", TaskQueueStartMode.class, TaskQueueStartMode.manual);
	}
	

	/**
	 * @deprecated Use propertyService.get(JpaMoreTaskPropertyIds.queueNumberOfThreads(queueId))
	 */
	@Deprecated
	public int getTaskQueueNumberOfThreads(String queueId) {
		Assert.notNull(queueId);
		return getPropertyAsInteger("task.queues.config." + queueId + ".threads", 1);
	}
	
	/**
	 * @deprecated Use propertyService.get(SpringSecurityPropertyIds.PASSWORD_SALT)
	 */
	@Deprecated
	public String getSecurityPasswordSalt() {
		return propertyService.get(PASSWORD_SALT);
	}
	
	/**
	 * @deprecated Use propertyService.get(SpringSecurityPropertyIds.PASSWORD_EXPIRATION_DAYS)
	 */
	@Deprecated
	public Integer getSecurityPasswordExpirationDays() {
		return propertyService.get(PASSWORD_EXPIRATION_DAYS);
	}
	
	/**
	 * @deprecated Use propertyService.get(SpringSecurityPropertyIds.PASSWORD_HISTORY_COUNT)
	 */
	@Deprecated
	public Integer getSecurityPasswordHistoryCount() {
		return propertyService.get(PASSWORD_HISTORY_COUNT);
	}
	
	/**
	 * @deprecated Use propertyService.get(SpringSecurityPropertyIds.PASSWORD_RECOVERY_REQUEST_TOKEN_RANDOM_COUNT)
	 */
	@Deprecated
	public Integer getSecurityPasswordRecoveryRequestTokenRandomCount() {
		return propertyService.get(PASSWORD_RECOVERY_REQUEST_TOKEN_RANDOM_COUNT);
	}
	
	/**
	 * @deprecated Use propertyService.get(SpringSecurityPropertyIds.PASSWORD_RECOVERY_REQUEST_EXPIRATION_MINUTES)
	 */
	@Deprecated
	public Integer getSecurityPasswordRecoveryRequestExpirationMinutes() {
		return propertyService.get(PASSWORD_RECOVERY_REQUEST_EXPIRATION_MINUTES);
	}
	
	/**
	 * @deprecated Use propertyService.get(JpaExternalLinkCheckerPropertyIds.USER_AGENT)
	 */
	@Deprecated
	public String getExternalLinkCheckerUserAgent() {
		return getPropertyAsString("externalLinkChecker.userAgent", "Core External Link Checker");
	}
	
	/**
	 * @deprecated Use propertyService.get(JpaExternalLinkCheckerPropertyIds.MAX_REDIRECTS)
	 */
	@Deprecated
	public int getExternalLinkCheckerMaxRedirects() {
		return getPropertyAsInteger("externalLinkChecker.maxRedirects", 5);
	}
	
	/**
	 * @deprecated Use propertyService.get(JpaExternalLinkCheckerPropertyIds.TIMEOUT)
	 */
	@Deprecated
	public int getExternalLinkCheckerTimeout() {
		return getPropertyAsInteger("externalLinkChecker.timeout", 10000);
	}
	
	/**
	 * @deprecated Use propertyService.get(JpaExternalLinkCheckerPropertyIds.RETRY_ATTEMPTS_NUMBER)
	 */
	@Deprecated
	public int getExternalLinkCheckerRetryAttemptsLimit() {
		return getPropertyAsInteger("externalLinkChecker.retryAttemptsNumber", 4);
	}
	
	/**
	 * @deprecated Use propertyService.get(JpaExternalLinkCheckerPropertyIds.THREAD_POOL_SIZE)
	 */
	@Deprecated
	public int getExternalLinkCheckerThreadPoolSize() {
		return getPropertyAsInteger("externalLinkChecker.threadPoolSize", Runtime.getRuntime().availableProcessors());
	}
	
	/**
	 * @deprecated Use propertyService.get(JpaExternalLinkCheckerPropertyIds.BATCH_SIZE)
	 */
	@Deprecated
	public int getExternalLinkCheckerBatchSize() {
		return getPropertyAsInteger("externalLinkChecker.batchSize", 500);
	}
	
	/**
	 * @deprecated Use propertyService.get(JpaExternalLinkCheckerPropertyIds.MIN_DELAY_BETWEEN_TWO_CHECKS_IN_DAYS)
	 */
	@Deprecated
	public int getExternalLinkCheckerMinDelayBetweenTwoChecksInDays() {
		return getPropertyAsInteger("externalLinkChecker.minDelayBetweenTwoChecksInDays", 2, 0, null);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SCHEME)
	 */
	@Deprecated
	public String getWicketBackgroundRequestCycleBuilderUrlScheme() {
		return getPropertyAsString("wicket.backgroundThreadContextBuilder.url.scheme", "http");
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_NAME)
	 */
	@Deprecated
	public String getWicketBackgroundRequestCycleBuilderUrlServerName() {
		return getPropertyAsString("wicket.backgroundThreadContextBuilder.url.serverName", "localhost");
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_PORT)
	 */
	@Deprecated
	public int getWicketBackgroundRequestCycleBuilderUrlServerPort() {
		return getPropertyAsInteger("wicket.backgroundThreadContextBuilder.url.serverPort", 8080);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.WICKET_DISK_DATA_STORE_PATH)
	 */
	@Deprecated
	public String getWicketDiskDataStorePath() {
		return getPropertyAsString("wicket.diskDataStore.path", null);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.WICKET_DISK_DATA_STORE_IN_MEMORY_CACHE_SIZE)
	 */
	@Deprecated
	public int getWicketDiskDataStoreInMemoryCacheSize() {
		// Default to 0, see http://markmail.org/message/lq4lkfxi5whb5clr#query:+page:1+mid:m5qzptq24kxvmefo+state:results
		return getPropertyAsInteger("wicket.diskDataStore.inMemoryCacheSize", 0);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.WICKET_DISK_DATA_STORE_MAX_SIZE_PER_SESSION)
	 */
	@Deprecated
	public int getWicketDiskDataStoreMaxSizePerSession() {
		return getPropertyAsInteger("wicket.diskDataStore.maxSizePerSession", 10);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.TMP_EXPORT_EXCEL_PATH)
	 */
	@Deprecated
	public File getTmpExportExcelDirectory() {
		return getPropertyAsWritableDirectory("tmp.exportExcel.path");
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.)
	 */
	@Deprecated
	public Integer getGlobalFeedbackAutohideDelayValue() {
		return getPropertyAsInteger("globalFeedback.autohide.delay.value", 5);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.)
	 */
	@Deprecated
	public TimeUnit getGlobalFeedbackAutohideDelayUnit() {
		return getPropertyAsEnum("globalFeedback.autohide.delay.unit", TimeUnit.class, TimeUnit.SECONDS);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE)
	 */
	@Deprecated
	public Integer getConsoleGlobalFeedbackAutohideDelayValue() {
		return getPropertyAsInteger("console.globalFeedback.autohide.delay.value", 5);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT)
	 */
	@Deprecated
	public TimeUnit getConsoleGlobalFeedbackAutohideDelayUnit() {
		return getPropertyAsEnum("console.globalFeedback.autohide.delay.unit", TimeUnit.class, TimeUnit.SECONDS);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.AUTOCOMPLETE_LIMIT)
	 */
	@Deprecated
	public int getAutocompleteLimit() {
		return getPropertyAsInteger("autocomplete.limit", 20);
	}
	
	

}
