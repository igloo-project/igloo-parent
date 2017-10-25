package fr.openwide.core.spring.property;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import fr.openwide.core.spring.property.model.AbstractPropertyIds;
import fr.openwide.core.spring.property.model.ImmutablePropertyId;

public final class SpringPropertyIds extends AbstractPropertyIds {
	
	private SpringPropertyIds() {
		
	}

	/*
	 * Mutable Properties
	 */
	
	// None
	
	/*
	 * Immutable Properties
	 */
	
	// The following properties are used in ConfigurationLogger
	public static final ImmutablePropertyId<String> DB_JDBC_URL = immutable("db.jdbcUrl");
	public static final ImmutablePropertyId<String> DB_TYPE = immutable("db.type");
	public static final ImmutablePropertyId<String> DB_USER = immutable("db.user");
	
	public static final String CONFIGURATION_TYPE_DEVELOPMENT = "development";
	public static final String CONFIGURATION_TYPE_DEPLOYMENT = "deployment";
	
	public static final ImmutablePropertyId<String> VERSION = immutable("version");
	public static final ImmutablePropertyId<String> OWSI_CORE_VERSION = immutable("owsi-core.version");
	public static final ImmutablePropertyId<String> CONFIGURATION_TYPE = immutable("configurationType");
	
	public static final ImmutablePropertyId<File> TMP_PATH = immutable("tmp.path");
	public static final ImmutablePropertyId<File> TMP_EXPORT_EXCEL_PATH = immutable("tmp.exportExcel.path");
	
	public static final ImmutablePropertyId<Set<Locale>> AVAILABLE_LOCALES = immutable("locale.availableLocales");
	public static final ImmutablePropertyId<Locale> DEFAULT_LOCALE = immutable("locale.default");
	
	public static final ImmutablePropertyId<String> NOTIFICATION_MAIL_SENDER = immutable("notification.mail.sender");
	public static final ImmutablePropertyId<String> NOTIFICATION_MAIL_FROM = immutable("notification.mail.from");
	public static final ImmutablePropertyId<String> NOTIFICATION_MAIL_SUBJECT_PREFIX = immutable("notification.mail.subjectPrefix");
	public static final ImmutablePropertyId<Boolean> NOTIFICATION_MAIL_RECIPIENTS_FILTERED = immutable("notification.mail.recipientsFiltered");
	public static final ImmutablePropertyId<List<String>> NOTIFICATION_TEST_EMAILS = immutable("notification.test.emails");
	public static final ImmutablePropertyId<List<String>> NOTIFICATION_MAIL_DISABLED_RECIPIENT_FALLBACK = immutable("notification.mail.disabledRecipientFallback");
	
}
