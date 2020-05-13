package org.iglooproject.spring.notification.model;

import java.util.Locale;

public class SimpleRecipient implements INotificationRecipient {
	
	private final Locale locale;
	
	private final String email;
	
	private final String fullName;
	
	public SimpleRecipient(Locale locale, String email, String fullName) {
		super();
		this.locale = locale;
		this.email = email;
		this.fullName = fullName;
	}
	
	public SimpleRecipient(INotificationRecipient recipient, String overriddenEmail) {
		this(recipient.getLocale(), overriddenEmail, recipient.getFullName());
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
		return isActive();
	}

	@Override
	public boolean isActive() {
		return true;
	}
	
}
