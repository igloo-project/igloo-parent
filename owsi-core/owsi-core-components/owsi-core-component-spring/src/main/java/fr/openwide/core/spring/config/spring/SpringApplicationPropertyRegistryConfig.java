package fr.openwide.core.spring.config.spring;

import static fr.openwide.core.spring.property.SpringPropertyIds.AVAILABLE_LOCALES;
import static fr.openwide.core.spring.property.SpringPropertyIds.CONFIGURATION_TYPE;
import static fr.openwide.core.spring.property.SpringPropertyIds.DB_JDBC_URL;
import static fr.openwide.core.spring.property.SpringPropertyIds.DB_TYPE;
import static fr.openwide.core.spring.property.SpringPropertyIds.DB_USER;
import static fr.openwide.core.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_DISABLED_RECIPIENT_FALLBACK;
import static fr.openwide.core.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_FROM;
import static fr.openwide.core.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_RECIPIENTS_FILTERED;
import static fr.openwide.core.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_SENDER;
import static fr.openwide.core.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_SUBJECT_PREFIX;
import static fr.openwide.core.spring.property.SpringPropertyIds.NOTIFICATION_TEST_EMAILS;
import static fr.openwide.core.spring.property.SpringPropertyIds.OWSI_CORE_VERSION;
import static fr.openwide.core.spring.property.SpringPropertyIds.TMP_EXPORT_EXCEL_PATH;
import static fr.openwide.core.spring.property.SpringPropertyIds.TMP_PATH;
import static fr.openwide.core.spring.property.SpringPropertyIds.VERSION;

import java.util.List;
import java.util.Locale;
import java.util.Set;

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
		
		registry.registerWriteableDirectoryFile(TMP_PATH);
		registry.registerWriteableDirectoryFile(TMP_EXPORT_EXCEL_PATH);
		
		registry.register(AVAILABLE_LOCALES, new Function<String, Set<Locale>>() {
			@Override
			public Set<Locale> apply(String input) {
				Set<Locale> locales = Sets.newHashSet();
				for (String localeAsString : Splitter.on(" ").omitEmptyStrings().split(input)) {
					try {
						locales.add(StringLocaleConverter.get().convert(localeAsString));
					} catch (RuntimeException e) {
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
		
		registry.registerString(NOTIFICATION_MAIL_FROM);
		registry.registerString(NOTIFICATION_MAIL_SUBJECT_PREFIX);
		registry.registerString(NOTIFICATION_MAIL_SENDER);
		registry.registerBoolean(NOTIFICATION_MAIL_RECIPIENTS_FILTERED);
		registry.register(NOTIFICATION_TEST_EMAILS, new StringCollectionConverter<String, List<String>>(Converter.<String>identity(), Suppliers2.<String>arrayList()), Lists.<String>newArrayList());
		registry.register(NOTIFICATION_MAIL_DISABLED_RECIPIENT_FALLBACK, new StringCollectionConverter<String, List<String>>(Converter.<String>identity(), Suppliers2.<String>arrayList()), Lists.<String>newArrayList());
	}
}
