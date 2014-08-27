package fr.openwide.core.spring.notification.model;

import java.util.Locale;

import fr.openwide.core.spring.notification.exception.NotificationContentRenderingException;

public interface INotificationContentDescriptor {
	
	String renderSubject(Locale locale) throws NotificationContentRenderingException;
	
	String renderHtmlBody(Locale locale) throws NotificationContentRenderingException;
	
	String renderTextBody(Locale locale) throws NotificationContentRenderingException;

}
