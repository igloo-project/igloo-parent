package fr.openwide.core.spring.notification.service;

import java.util.Locale;


public interface INotificationBuilderBodyState {

	INotificationBuilderSendState textBody(String textBody);
	
	INotificationBuilderSendState htmlBody(String htmlBody);
	
	INotificationBuilderSendState htmlBody(String htmlBody, Locale locale);
	
}
