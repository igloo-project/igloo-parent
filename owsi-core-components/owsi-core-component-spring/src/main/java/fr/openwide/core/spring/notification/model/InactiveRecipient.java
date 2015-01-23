package fr.openwide.core.spring.notification.model;

import java.util.Locale;

public class InactiveRecipient implements INotificationRecipient {
	
	private final Locale locale;
	
	private final String email;
	
	private final String fullName;
	
	public InactiveRecipient(INotificationRecipient recipient, String inactiveUserEmail) {
		this.locale = recipient.getLocale();
		this.email = inactiveUserEmail;
		this.fullName = recipient.getFullName();
	}
	
	@Override
	public Locale getLocale() {
		return locale;
	}
	
	@Override
	public String getEmail() {
		return email;
	}
	
	@Override
	public String getFullName() {
		return fullName;
	}

	@Override
	public boolean isNotificationEnabled() {
		return true;
	}
	
}
