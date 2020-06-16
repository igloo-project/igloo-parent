package test.wicket.more.notification;

import java.util.Locale;

import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.spring.notification.model.SimpleRecipient;
import org.iglooproject.spring.notification.service.INotificationBuilderBaseState;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import test.wicket.more.notification.service.INotificationContentDescriptorFactory;

public class TestWicketNotification extends AbstractTestWicketNotification {

	@Autowired
	public INotificationContentDescriptorFactory notificationContentDescriptorFactory;

	/**
	 * Send one notification to two recipients.
	 */
	@Test
	public void testWicketNotificationGroup() throws ServiceException {
		INotificationBuilderBaseState builder = createNotificationBuilder();
		builder.toAddress("test-1@example.com", "test-2@example.com")
			.content(notificationContentDescriptorFactory.simpleContent("my content")).send();
		mockitoSend(Mockito.times(1));
	}

	/**
	 * Send one notification to two (locale=fr) recipients
	 */
	@Test
	public void testWicketNotificationGroupLocale() throws ServiceException {
		INotificationBuilderBaseState builder = createNotificationBuilder();
		builder.to(
				new SimpleRecipient(Locale.FRENCH, "test-1@example.com", "Recipient 1"),
				new SimpleRecipient(Locale.FRENCH, "test-2@example.com", "Recipient 2")
			).content(notificationContentDescriptorFactory.simpleContent("my content")).send();
		mockitoSend(Mockito.times(1));
	}

	/**
	 * Check that locale=null is grouped with locale=fr as default locale is fr
	 */
	@Test
	public void testWicketNotificationGroupLocaleNullAndDefault() throws ServiceException {
		INotificationBuilderBaseState builder = createNotificationBuilder();
		builder.to(
				new SimpleRecipient(Locale.FRENCH, "test-1@example.com", "Recipient 1"),
				new SimpleRecipient(null, "test-2@example.com", "Recipient 2")
			).content(notificationContentDescriptorFactory.simpleContent("my content")).send();
		mockitoSend(Mockito.times(1));
	}

	/**
	 * Check that locale=fr_CA and locale=fr_FR are grouped as they collapsed to the same
	 * available locale (same prefix fr).
	 */
	@Test
	public void testWicketNotificationGroupEquivalentLocale() throws ServiceException {
		INotificationBuilderBaseState builder = createNotificationBuilder();
		builder.to(
				new SimpleRecipient(Locale.FRANCE, "test-1@example.com", "Recipient 1"),
				new SimpleRecipient(Locale.CANADA_FRENCH, "test-2@example.com", "Recipient 2")
			).content(notificationContentDescriptorFactory.simpleContent("my content")).send();
		mockitoSend(Mockito.times(1));
	}

	/**
	 * Check that locale=de and locale=fr_FR are grouped as 'de' is not available and is
	 * replaced by default locale 'fr' and fr_FR is collapsed to 'fr'.
	 */
	@Test
	public void testWicketNotificationGroupEquivalentLocale2() throws ServiceException {
		INotificationBuilderBaseState builder = createNotificationBuilder();
		builder.to(
				new SimpleRecipient(Locale.FRANCE, "test-1@example.com", "Recipient 1"),
				new SimpleRecipient(Locale.GERMAN, "test-2@example.com", "Recipient 2")
			).content(notificationContentDescriptorFactory.simpleContent("my content")).send();
		mockitoSend(Mockito.times(1));
	}

	/**
	 * Check that locale=null and locale=en are splitted as default locale is not 'en', and
	 * 'en' is an available locale.
	 */
	@Test
	public void testWicketNotificationSplitLocaleNullAndNotDefault() throws ServiceException {
		INotificationBuilderBaseState builder = createNotificationBuilder();
		builder.to(
				new SimpleRecipient(Locale.ENGLISH, "test-1@example.com", "Recipient 1"),
				new SimpleRecipient(null, "test-2@example.com", "Recipient 2")
			).content(notificationContentDescriptorFactory.simpleContent("my content")).send();
		mockitoSend(Mockito.times(2));
	}

	/**
	 * Check that locale=fr_FR and locale=en_EN are splitted as they collapsed to different
	 * available locales.
	 */
	@Test
	public void testWicketNotificationSplitLocales() throws ServiceException {
		INotificationBuilderBaseState builder = createNotificationBuilder();
		builder.to(
				new SimpleRecipient(Locale.FRANCE, "test-1@example.com", "Recipient 1"),
				new SimpleRecipient(Locale.ENGLISH, "test-2@example.com", "Recipient 2")
			).content(notificationContentDescriptorFactory.simpleContent("my content")).send();
		mockitoSend(Mockito.times(2));
	}

	/**
	 * Check that locale=fr_FR and locale=en_EN are splitted as they collapsed to different
	 * available locales.
	 */
	@Test
	public void testWicketNotificationBypassDisabledRecipient() throws ServiceException {
		INotificationBuilderBaseState builder = createNotificationBuilder();
		builder
			.bypassDisabledRecipients()
			.to(
				new SimpleRecipient(Locale.FRANCE, "test-1@example.com", "Recipient 1") {
					@Override
					public boolean isNotificationEnabled() {
						return false;
					}
				}
			).content(notificationContentDescriptorFactory.simpleContent("my content")).send();
		mockitoSend(Mockito.times(1));
	}

}
