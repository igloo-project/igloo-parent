package fr.openwide.core.spring.config;

import static fr.openwide.core.spring.property.SpringPropertyIds.AUTOCOMPLETE_LIMIT;
import static fr.openwide.core.spring.property.SpringPropertyIds.AVAILABLE_LOCALES;
import static fr.openwide.core.spring.property.SpringPropertyIds.CONFIGURATION_TYPE;
import static fr.openwide.core.spring.property.SpringPropertyIds.CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT;
import static fr.openwide.core.spring.property.SpringPropertyIds.CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE;
import static fr.openwide.core.spring.property.SpringPropertyIds.DEFAULT_LOCALE;
import static fr.openwide.core.spring.property.SpringPropertyIds.GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT;
import static fr.openwide.core.spring.property.SpringPropertyIds.GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE;
import static fr.openwide.core.spring.property.SpringPropertyIds.HIBERNATE_SEARCH_REINDEX_BATCH_SIZE;
import static fr.openwide.core.spring.property.SpringPropertyIds.HIBERNATE_SEARCH_REINDEX_LOAD_THREADS;
import static fr.openwide.core.spring.property.SpringPropertyIds.IMAGE_MAGICK_CONVERT_BINARY_PATH;
import static fr.openwide.core.spring.property.SpringPropertyIds.LUCENE_BOOLEAN_QUERY_MAX_CLAUSE_COUNT;
import static fr.openwide.core.spring.property.SpringPropertyIds.MIGRATION_LOGGING_MEMORY;
import static fr.openwide.core.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_DISABLED_RECIPIENT_FALLBACK;
import static fr.openwide.core.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_FROM;
import static fr.openwide.core.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_RECIPIENTS_FILTERED;
import static fr.openwide.core.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_SUBJECT_PREFIX;
import static fr.openwide.core.spring.property.SpringPropertyIds.NOTIFICATION_TEST_EMAILS;
import static fr.openwide.core.spring.property.SpringPropertyIds.OWSI_CORE_VERSION;
import static fr.openwide.core.spring.property.SpringPropertyIds.TMP_EXPORT_EXCEL_PATH;
import static fr.openwide.core.spring.property.SpringPropertyIds.TMP_PATH;
import static fr.openwide.core.spring.property.SpringPropertyIds.VERSION;
import static fr.openwide.core.spring.property.SpringPropertyIds.WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SCHEME;
import static fr.openwide.core.spring.property.SpringPropertyIds.WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_NAME;
import static fr.openwide.core.spring.property.SpringPropertyIds.WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_PORT;
import static fr.openwide.core.spring.property.SpringPropertyIds.WICKET_DISK_DATA_STORE_IN_MEMORY_CACHE_SIZE;
import static fr.openwide.core.spring.property.SpringPropertyIds.WICKET_DISK_DATA_STORE_MAX_SIZE_PER_SESSION;
import static fr.openwide.core.spring.property.SpringPropertyIds.WICKET_DISK_DATA_STORE_PATH;
import static fr.openwide.core.spring.property.SpringSecurityPropertyIds.PASSWORD_EXPIRATION_DAYS;
import static fr.openwide.core.spring.property.SpringSecurityPropertyIds.PASSWORD_HISTORY_COUNT;
import static fr.openwide.core.spring.property.SpringSecurityPropertyIds.PASSWORD_RECOVERY_REQUEST_EXPIRATION_MINUTES;
import static fr.openwide.core.spring.property.SpringSecurityPropertyIds.PASSWORD_RECOVERY_REQUEST_TOKEN_RANDOM_COUNT;
import static fr.openwide.core.spring.property.SpringSecurityPropertyIds.PASSWORD_SALT;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import fr.openwide.core.spring.config.util.TaskQueueStartMode;
import fr.openwide.core.spring.property.service.IPropertyService;

public class CoreConfigurer extends CorePropertyPlaceholderConfigurer {

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
	 * @deprecated Use propertyService.get(CorePropertyIds.OWSI_CORE_VERSION)
	 */
	@Deprecated
	public String getOwsiCoreVersion() {
		return propertyService.get(OWSI_CORE_VERSION);
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
		return propertyService.get(MIGRATION_LOGGING_MEMORY);
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
		return propertyService.get(LUCENE_BOOLEAN_QUERY_MAX_CLAUSE_COUNT);
	}

	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.IMAGE_MAGICK_CONVERT_BINARY_PATH)
	 */
	@Deprecated
	public File getImageMagickConvertBinary() {
		return propertyService.get(IMAGE_MAGICK_CONVERT_BINARY_PATH);
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
		return propertyService.get(HIBERNATE_SEARCH_REINDEX_BATCH_SIZE);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.HIBERNATE_SEARCH_REINDEX_LOAD_THREADS)
	 */
	@Deprecated
	public int getHibernateSearchReindexLoadThreads() {
		return propertyService.get(HIBERNATE_SEARCH_REINDEX_LOAD_THREADS);
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
		return propertyService.get(WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SCHEME);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_NAME)
	 */
	@Deprecated
	public String getWicketBackgroundRequestCycleBuilderUrlServerName() {
		return propertyService.get(WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_NAME);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_PORT)
	 */
	@Deprecated
	public int getWicketBackgroundRequestCycleBuilderUrlServerPort() {
		return propertyService.get(WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_PORT);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.WICKET_DISK_DATA_STORE_PATH)
	 */
	@Deprecated
	public String getWicketDiskDataStorePath() {
		return propertyService.get(WICKET_DISK_DATA_STORE_PATH);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.WICKET_DISK_DATA_STORE_IN_MEMORY_CACHE_SIZE)
	 */
	@Deprecated
	public int getWicketDiskDataStoreInMemoryCacheSize() {
		return propertyService.get(WICKET_DISK_DATA_STORE_IN_MEMORY_CACHE_SIZE);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.WICKET_DISK_DATA_STORE_MAX_SIZE_PER_SESSION)
	 */
	@Deprecated
	public int getWicketDiskDataStoreMaxSizePerSession() {
		return propertyService.get(WICKET_DISK_DATA_STORE_MAX_SIZE_PER_SESSION);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.TMP_EXPORT_EXCEL_PATH)
	 */
	@Deprecated
	public File getTmpExportExcelDirectory() {
		return propertyService.get(TMP_EXPORT_EXCEL_PATH);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.)
	 */
	@Deprecated
	public Integer getGlobalFeedbackAutohideDelayValue() {
		return propertyService.get(GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.)
	 */
	@Deprecated
	public TimeUnit getGlobalFeedbackAutohideDelayUnit() {
		return propertyService.get(GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE)
	 */
	@Deprecated
	public Integer getConsoleGlobalFeedbackAutohideDelayValue() {
		return propertyService.get(CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT)
	 */
	@Deprecated
	public TimeUnit getConsoleGlobalFeedbackAutohideDelayUnit() {
		return propertyService.get(CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT);
	}
	
	/**
	 * @deprecated Use propertyService.get(CorePropertyIds.AUTOCOMPLETE_LIMIT)
	 */
	@Deprecated
	public int getAutocompleteLimit() {
		return propertyService.get(AUTOCOMPLETE_LIMIT);
	}

}