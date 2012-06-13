package fr.openwide.core.spring.notification.model;

import java.util.Locale;

public interface INotificationRecipient {

	Locale getLocale();
	
	String getEmail();
	
	String getFullName();
}
