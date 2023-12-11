package org.iglooproject.spring.config.spring;

import static org.iglooproject.spring.property.SpringPropertyIds.AVAILABLE_LOCALES;
import static org.iglooproject.spring.property.SpringPropertyIds.CONFIGURATION_TYPE;
import static org.iglooproject.spring.property.SpringPropertyIds.DB_JDBC_URL;
import static org.iglooproject.spring.property.SpringPropertyIds.DB_TYPE;
import static org.iglooproject.spring.property.SpringPropertyIds.DB_USER;
import static org.iglooproject.spring.property.SpringPropertyIds.IGLOO_VERSION;
import static org.iglooproject.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_DISABLED_RECIPIENT_FALLBACK;
import static org.iglooproject.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_FILTER_EMAILS;
import static org.iglooproject.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_FROM;
import static org.iglooproject.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_SENDER;
import static org.iglooproject.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_SENDER_BEHAVIOR;
import static org.iglooproject.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_SEND_MODE;
import static org.iglooproject.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_SUBJECT_PREFIX;
import static org.iglooproject.spring.property.SpringPropertyIds.PROPERTIES_HIDDEN;
import static org.iglooproject.spring.property.SpringPropertyIds.TMP_EXPORT_EXCEL_PATH;
import static org.iglooproject.spring.property.SpringPropertyIds.TMP_PATH;
import static org.iglooproject.spring.property.SpringPropertyIds.VERSION;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.iglooproject.functional.Suppliers2;
import org.iglooproject.functional.converter.StringCollectionConverter;
import org.iglooproject.functional.converter.StringLocaleConverter;
import org.iglooproject.spring.config.util.MailSenderBehavior;
import org.iglooproject.spring.notification.util.NotificationSendMode;
import org.iglooproject.spring.property.SpringPropertyIds;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.google.common.base.Converter;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Import(SpringSecurityApplicationPropertyRegistryConfig.class)
@Configuration
public class SpringApplicationPropertyRegistryConfig extends AbstractApplicationPropertyRegistryConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringApplicationPropertyRegistryConfig.class);

	@Override
	public void register(IPropertyRegistry registry) {
		registry.registerString(DB_JDBC_URL);
		registry.registerString(DB_TYPE);
		registry.registerString(DB_USER);
		
		registry.registerString(VERSION);
		registry.registerString(IGLOO_VERSION);
		registry.register(
			CONFIGURATION_TYPE,
			input -> {
				if (SpringPropertyIds.CONFIGURATION_TYPE_DEVELOPMENT.equals(input) || SpringPropertyIds.CONFIGURATION_TYPE_DEPLOYMENT.equals(input)) {
					return input;
				} else {
					throw new IllegalStateException("Configuration type should be either development or deployment");
				}
			}
		);
		
		registry.registerWriteableDirectoryFile(TMP_PATH);
		registry.registerWriteableDirectoryFile(TMP_EXPORT_EXCEL_PATH);
		
		registry.register(
			AVAILABLE_LOCALES,
			input -> {
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
		);
		registry.registerLocale(SpringPropertyIds.DEFAULT_LOCALE);
		
		registry.registerString(NOTIFICATION_MAIL_FROM);
		registry.registerString(NOTIFICATION_MAIL_SUBJECT_PREFIX);
		registry.registerString(NOTIFICATION_MAIL_SENDER);
		registry.registerEnum(NOTIFICATION_MAIL_SENDER_BEHAVIOR, MailSenderBehavior.class, MailSenderBehavior.EXPLICIT);
		registry.registerEnum(NOTIFICATION_MAIL_SEND_MODE, NotificationSendMode.class, NotificationSendMode.SEND);
		registry.register(NOTIFICATION_MAIL_FILTER_EMAILS, new StringCollectionConverter<String, List<String>>(Converter.<String>identity(), Suppliers2.<String>arrayList()), Lists.<String>newArrayList());
		registry.register(NOTIFICATION_MAIL_DISABLED_RECIPIENT_FALLBACK, new StringCollectionConverter<String, List<String>>(Converter.<String>identity(), Suppliers2.<String>arrayList()), Lists.<String>newArrayList());
		
		registry.register(
			PROPERTIES_HIDDEN,
			new StringCollectionConverter<>(
				Converter.<String>identity(),
				Suppliers2.<String>arrayList()
			),
			Lists.<String>newArrayList()
		);
	}
}
