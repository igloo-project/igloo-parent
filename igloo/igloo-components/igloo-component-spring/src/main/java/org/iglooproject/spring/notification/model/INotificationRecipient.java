package org.iglooproject.spring.notification.model;

import java.util.Locale;

public interface INotificationRecipient {

	Locale getLocale();

	String getEmail();

	String getFullName();

	/**
	 * Indicateur pour savoir si les notifications doivent être envoyées au recipient désigné par cet objet.
	 * 
	 * @return true si le recipient lié doit recevoir les notifications.
	 */
	boolean isNotificationEnabled();

	boolean isEnabled();

}
