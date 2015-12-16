package fr.openwide.core.spring.property;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import fr.openwide.core.spring.property.model.ImmutablePropertyId;

public final class SpringPropertyIds {

	/*
	 * Mutable Properties
	 */
	
	
	
	/*
	 * Immutable Properties
	 */
	
	// The following properties are used in ConfigurationLogger
	public static final ImmutablePropertyId<String> DB_JDBC_URL = new ImmutablePropertyId<>("db.jdbcUrl");
	public static final ImmutablePropertyId<String> DB_TYPE = new ImmutablePropertyId<>("db.type");
	public static final ImmutablePropertyId<String> DB_USER = new ImmutablePropertyId<>("db.user");
	
	public static final String CONFIGURATION_TYPE_DEVELOPMENT = "development";
	public static final String CONFIGURATION_TYPE_DEPLOYMENT = "deployment";
	
	public static final ImmutablePropertyId<String> VERSION = new ImmutablePropertyId<>("version");
	public static final ImmutablePropertyId<String> OWSI_CORE_VERSION = new ImmutablePropertyId<>("owsi-core.version");
	public static final ImmutablePropertyId<String> CONFIGURATION_TYPE = new ImmutablePropertyId<>("configurationType");
	
	public static final ImmutablePropertyId<Boolean> MIGRATION_LOGGING_MEMORY = new ImmutablePropertyId<>("migration.logging.memory");
	
	public static final ImmutablePropertyId<File> TMP_PATH = new ImmutablePropertyId<>("tmp.path");
	
	public static final ImmutablePropertyId<Integer> LUCENE_BOOLEAN_QUERY_MAX_CLAUSE_COUNT = new ImmutablePropertyId<>("lucene.booleanQuery.maxClauseCount");
	
	public static final ImmutablePropertyId<File> IMAGE_MAGICK_CONVERT_BINARY = new ImmutablePropertyId<>("imageMagick.convertBinary.path");
	
	public static final ImmutablePropertyId<Set<Locale>> AVAILABLE_LOCALES = new ImmutablePropertyId<>("locale.availableLocales");
	public static final ImmutablePropertyId<Locale> DEFAULT_LOCALE = new ImmutablePropertyId<>("locale.default");
	
	public static final ImmutablePropertyId<Integer> HIBERNATE_SEARCH_REINDEX_BATCH_SIZE = new ImmutablePropertyId<>("hibernate.search.reindex.batchSize");
	public static final ImmutablePropertyId<Integer> HIBERNATE_SEARCH_REINDEX_LOAD_THREADS = new ImmutablePropertyId<>("hibernate.search.reindex.loadThreads");
	
	public static final ImmutablePropertyId<String> NOTIFICATION_MAIL_FROM = new ImmutablePropertyId<>("notification.mail.from");
	public static final ImmutablePropertyId<String> NOTIFICATION_MAIL_SUBJECT_PREFIX = new ImmutablePropertyId<>("notification.mail.subjectPrefix");
	public static final ImmutablePropertyId<Boolean> NOTIFICATION_MAIL_RECIPIENTS_FILTERED = new ImmutablePropertyId<>("notification.mail.recipientsFiltered");
	public static final ImmutablePropertyId<List<String>> NOTIFICATION_TEST_EMAILS = new ImmutablePropertyId<>("notification.test.emails");
	public static final ImmutablePropertyId<List<String>> NOTIFICATION_MAIL_DISABLED_RECIPIENT_FALLBACK = new ImmutablePropertyId<>("notification.mail.disabledRecipientFallback");
	
	public static final ImmutablePropertyId<String> WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SCHEME = new ImmutablePropertyId<>("wicket.backgroundThreadContextBuilder.url.scheme");
	public static final ImmutablePropertyId<String> WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_NAME = new ImmutablePropertyId<>("wicket.backgroundThreadContextBuilder.url.serverName");
	public static final ImmutablePropertyId<Integer> WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_PORT = new ImmutablePropertyId<>("wicket.backgroundThreadContextBuilder.url.serverPort");
	public static final ImmutablePropertyId<String> WICKET_DISK_DATA_STORE_PATH = new ImmutablePropertyId<>("wicket.diskDataStore.path");
	public static final ImmutablePropertyId<Integer> WICKET_DISK_DATA_STORE_IN_MEMORY_CACHE_SIZE = new ImmutablePropertyId<>("wicket.diskDataStore.inMemoryCacheSize");
	public static final ImmutablePropertyId<Integer> WICKET_DISK_DATA_STORE_MAX_SIZE_PER_SESSION = new ImmutablePropertyId<>("wicket.diskDataStore.maxSizePerSession");
	
	public static final ImmutablePropertyId<File> TMP_EXPORT_EXCEL_PATH = new ImmutablePropertyId<>("tmp.exportExcel.path");
	
	public static final ImmutablePropertyId<Integer> GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE = new ImmutablePropertyId<>("globalFeedback.autohide.delay.value");
	public static final ImmutablePropertyId<TimeUnit> GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT = new ImmutablePropertyId<>("globalFeedback.autohide.delay.unit");
	public static final ImmutablePropertyId<Integer> CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE = new ImmutablePropertyId<>("console.globalFeedback.autohide.delay.value");
	public static final ImmutablePropertyId<TimeUnit> CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT = new ImmutablePropertyId<>("console.globalFeedback.autohide.delay.unit");
	
	public static final ImmutablePropertyId<Integer> AUTOCOMPLETE_LIMIT = new ImmutablePropertyId<>("autocomplete.limit");
}
