package fr.openwide.core.spring.property;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
	
	public static final ImmutablePropertyId<File> TMP_PATH = new ImmutablePropertyId<>("tmp.path");
	
	public static final ImmutablePropertyId<Set<Locale>> AVAILABLE_LOCALES = new ImmutablePropertyId<>("locale.availableLocales");
	public static final ImmutablePropertyId<Locale> DEFAULT_LOCALE = new ImmutablePropertyId<>("locale.default");
	
	public static final ImmutablePropertyId<String> NOTIFICATION_MAIL_FROM = new ImmutablePropertyId<>("notification.mail.from");
	public static final ImmutablePropertyId<String> NOTIFICATION_MAIL_SUBJECT_PREFIX = new ImmutablePropertyId<>("notification.mail.subjectPrefix");
	public static final ImmutablePropertyId<Boolean> NOTIFICATION_MAIL_RECIPIENTS_FILTERED = new ImmutablePropertyId<>("notification.mail.recipientsFiltered");
	public static final ImmutablePropertyId<List<String>> NOTIFICATION_TEST_EMAILS = new ImmutablePropertyId<>("notification.test.emails");
	public static final ImmutablePropertyId<List<String>> NOTIFICATION_MAIL_DISABLED_RECIPIENT_FALLBACK = new ImmutablePropertyId<>("notification.mail.disabledRecipientFallback");
	
}
