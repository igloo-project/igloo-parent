package fr.openwide.core.spring.config.spring;

import static fr.openwide.core.spring.property.SpringPropertyIds.AUTOCOMPLETE_LIMIT;
import static fr.openwide.core.spring.property.SpringPropertyIds.AVAILABLE_LOCALES;
import static fr.openwide.core.spring.property.SpringPropertyIds.CONFIGURATION_TYPE;
import static fr.openwide.core.spring.property.SpringPropertyIds.CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT;
import static fr.openwide.core.spring.property.SpringPropertyIds.CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE;
import static fr.openwide.core.spring.property.SpringPropertyIds.DB_JDBC_URL;
import static fr.openwide.core.spring.property.SpringPropertyIds.DB_TYPE;
import static fr.openwide.core.spring.property.SpringPropertyIds.DB_USER;
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

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.lucene.search.BooleanQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.google.common.base.Converter;
import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import fr.openwide.core.commons.util.functional.Suppliers2;
import fr.openwide.core.commons.util.functional.converter.StringCollectionConverter;
import fr.openwide.core.commons.util.functional.converter.StringLocaleConverter;
import fr.openwide.core.spring.property.SpringPropertyIds;
import fr.openwide.core.spring.property.service.IPropertyRegistry;
import fr.openwide.core.spring.util.StringUtils;

@Import(SpringSecurityApplicationPropertyRegistryConfig.class)
@Configuration
public class SpringApplicationPropertyRegistryConfig extends AbstractApplicationPropertyRegistryConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringApplicationPropertyRegistryConfig.class);

	@Override
	protected void register(IPropertyRegistry registry) {
		registry.registerString(DB_JDBC_URL);
		registry.registerString(DB_TYPE);
		registry.registerString(DB_USER);
		
		registry.registerString(VERSION);
		registry.registerString(OWSI_CORE_VERSION);
		registry.register(
				CONFIGURATION_TYPE,
				new Function<String, String>() {
					@Override
					public String apply(String input) {
						if (SpringPropertyIds.CONFIGURATION_TYPE_DEVELOPMENT.equals(input) || SpringPropertyIds.CONFIGURATION_TYPE_DEPLOYMENT.equals(input)) {
							return input;
						} else {
							throw new IllegalStateException("Configuration type should be either development or deployment");
						}
					}
				}
		);
		
		registry.registerBoolean(MIGRATION_LOGGING_MEMORY);
		
		registry.registerDirectoryFile(TMP_PATH);
		
		registry.registerInteger(LUCENE_BOOLEAN_QUERY_MAX_CLAUSE_COUNT, BooleanQuery.getMaxClauseCount());
		
		registry.register(
				IMAGE_MAGICK_CONVERT_BINARY_PATH,
				new Function<String, File>() {
					@Override
					public File apply(String input) {
						if (!StringUtils.hasText(input)) {
							return null;
						}
						return new File(input);
					}
				},
				new File("/usr/bin/convert")
		);
		
		registry.register(AVAILABLE_LOCALES, new Function<String, Set<Locale>>() {
			@Override
			public Set<Locale> apply(String input) {
				Set<Locale> locales = Sets.newHashSet();
				for (String localeAsString : Splitter.on(" ").omitEmptyStrings().split(input)) {
					try {
						locales.add(StringLocaleConverter.get().convert(localeAsString));
					} catch (Exception e) {
						LOGGER.error(String.format(
								"%1$s string from locale.availableLocales cannot be mapped to Locale, ignored",
								localeAsString
						));
					}
				}
				return locales;
			}
		});
		registry.registerLocale(SpringPropertyIds.DEFAULT_LOCALE);
		
		registry.registerInteger(HIBERNATE_SEARCH_REINDEX_BATCH_SIZE, 25);
		registry.registerInteger(HIBERNATE_SEARCH_REINDEX_LOAD_THREADS, 8);
		
		registry.registerString(NOTIFICATION_MAIL_FROM);
		registry.registerString(NOTIFICATION_MAIL_SUBJECT_PREFIX);
		registry.registerBoolean(NOTIFICATION_MAIL_RECIPIENTS_FILTERED);
		registry.register(NOTIFICATION_TEST_EMAILS, new StringCollectionConverter<String, List<String>>(Converter.<String>identity(), Suppliers2.<String>arrayList()), Lists.<String>newArrayList());
		registry.register(NOTIFICATION_MAIL_DISABLED_RECIPIENT_FALLBACK, new StringCollectionConverter<String, List<String>>(Converter.<String>identity(), Suppliers2.<String>arrayList()), Lists.<String>newArrayList());
		
		
		registry.registerString(WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SCHEME, "http");
		registry.registerString(WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_NAME, "localhost");
		registry.registerInteger(WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_PORT, 8080);
		registry.registerString(WICKET_DISK_DATA_STORE_PATH, "");
		registry.registerInteger(WICKET_DISK_DATA_STORE_IN_MEMORY_CACHE_SIZE, 40);
		registry.registerInteger(WICKET_DISK_DATA_STORE_MAX_SIZE_PER_SESSION, 10);
		
		registry.registerDirectoryFile(TMP_EXPORT_EXCEL_PATH);
		
		registry.registerInteger(GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE, 5);
		registry.registerEnum(GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT, TimeUnit.class, TimeUnit.SECONDS);
		registry.registerInteger(CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE, 5);
		registry.registerEnum(CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT, TimeUnit.class, TimeUnit.SECONDS);
		
		registry.registerInteger(AUTOCOMPLETE_LIMIT, 20);
	}

}
