package fr.openwide.core.spring.notification.service.impl;

import java.util.Locale;
import java.util.Map;

import com.google.common.collect.Maps;

import fr.openwide.core.spring.notification.model.INotificationContentDescriptor;

public class ExplicitelyDefinedNotificationContentDescriptorImpl implements INotificationContentDescriptor {
	
	private Map<Locale, String> subjectByLocale = Maps.newHashMap();
	
	private Map<Locale, String> htmlBodyByLocale = Maps.newHashMap();
	
	private Map<Locale, String> textBodyByLocale = Maps.newHashMap();

	@Override
	public String renderSubject(Locale locale) {
		String htmlBody = subjectByLocale.get(locale);
		if (htmlBody == null) {
			htmlBody = subjectByLocale.get(null);
		}
		return htmlBody;
	}

	/**
	 * If locale == null, the given subject will be used for any unreferenced locale
	 */
	public void setSubject(Locale locale, String subject) {
		subjectByLocale.put(locale, subject);
	}
	
	@Override
	public String renderHtmlBody(Locale locale) {
		String body = htmlBodyByLocale.get(locale);
		if (body == null) {
			body = htmlBodyByLocale.get(null);
		}
		return body;
	}

	/**
	 * If locale == null, the given body will be used for any unreferenced locale
	 */
	public void setHtmlBody(Locale locale, String body) {
		htmlBodyByLocale.put(locale, body);
	}
	
	@Override
	public String renderTextBody(Locale locale) {
		String body = textBodyByLocale.get(locale);
		if (body == null) {
			body = textBodyByLocale.get(null);
		}
		return body;
	}

	/**
	 * If locale == null, the given body will be used for any unreferenced locale
	 */
	public void setTextBody(Locale locale, String body) {
		textBodyByLocale.put(locale, body);
	}

}
