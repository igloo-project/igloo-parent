package fr.openwide.core.spring.notification.service;

import java.io.File;
import java.util.Locale;
import java.util.Map;

import org.springframework.util.MultiValueMap;

import fr.openwide.core.jpa.exception.ServiceException;

public interface INotificationBuilderSendState {
	
	INotificationBuilderSendState htmlBody(String htmlBody);
	
	INotificationBuilderSendState htmlBody(String htmlBody, Locale locale);

	INotificationBuilderSendState attach(String attachmentFilename, File file);
	
	INotificationBuilderSendState attach(Map<String, File> attachments);

	INotificationBuilderSendState inline(String contentId, File file);

	INotificationBuilderSendState header(String name, String value);
	
	INotificationBuilderSendState headers(MultiValueMap<String, String> headers);
	
	INotificationBuilderSendState priority(int priority);
	
	void send() throws ServiceException;
	
	void send(String encoding) throws ServiceException;
}
