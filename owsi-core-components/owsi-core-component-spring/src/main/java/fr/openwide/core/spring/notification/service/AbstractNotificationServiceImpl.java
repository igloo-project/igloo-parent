package fr.openwide.core.spring.notification.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.MultiValueMap;

import fr.openwide.core.spring.config.CoreConfigurer;
import fr.openwide.core.spring.notification.model.INotificationRecipient;
import fr.openwide.core.spring.util.StringUtils;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public class AbstractNotificationServiceImpl {
	
	private static final Log LOGGER = LogFactory.getLog(AbstractNotificationServiceImpl.class);

	@Autowired
	protected JavaMailSender mailSender;
	
	@Autowired
	protected CoreConfigurer configurer;
	
	@Autowired
	@Qualifier(value = "freemarkerMailConfiguration")
	protected Configuration templateConfiguration;
	
	private static final String DEV_SUBJECT_PREFIX = "[Dev]";
	
	protected void sendMailToEmails(String[] emails, String subject, String body) {
		MultiValueMap<String, String> headers = null;
		sendMailToEmails(emails, subject, body, headers);
	}

	protected void sendMailToEmails(String[] emails, String subject, String body, MultiValueMap<String, String> headers) {
		try {
			String[] filteredEmails = filterEmails(emails);
			if (filteredEmails.length == 0) {
				return;
			}
			
			String emailContent;
			
			if(configurer.isConfigurationTypeDevelopment()) {
				StringBuffer newBody = new StringBuffer();
				newBody.append("#############\n");
				newBody.append("#\n");
				newBody.append("# To: ").append(StringUtils.join(emails, ", ")).append("\n");
				newBody.append("#\n");
				newBody.append("#############\n\n\n");
				newBody.append(body);
				
				emailContent = newBody.toString();
			} else {
				emailContent = body;
			}
			
			MimeMessage message = mailSender.createMimeMessage();
			
			MimeMessageHelper helper = new MimeMessageHelper(message);
			helper.setSentDate(new Date());
			helper.setFrom(getFrom());
			helper.setTo(filteredEmails);
			helper.setSubject(getSubjectPrefix() + " " + subject);
			helper.setText(emailContent);
			
			if (headers != null) {
				for (Entry<String, List<String>> entry : headers.entrySet()) {
					for (String value : entry.getValue()) {
						message.addHeader(entry.getKey(), value);
					}
				}
			}
			
			mailSender.send(message);
		} catch(Exception e) {
			LOGGER.error("Error during send mail process", e);
		}
	}

	protected void sendMailToEmail(String email, String subject, String body) {
		sendMailToEmails(new String[] { email }, subject, body);
	}

	protected void sendMailToEmail(String email, String subject, String body, MultiValueMap<String, String> headers) {
		sendMailToEmails(new String[] { email }, subject, body);
	}
	
	protected Locale getLocale(INotificationRecipient recipient) {
		return configurer.toAvailableLocale(recipient.getLocale());
	}
	
	protected String[] filterEmails(String email) {
		if(!configurer.isConfigurationTypeDevelopment()) {
			return new String[] { email };
		} else {
			return configurer.getNotificationTestEmails();
		}
	}
	
	protected String[] filterEmails(List<String> emails) {
		if(!configurer.isConfigurationTypeDevelopment()) {
			return emails.toArray(new String[emails.size()]);
		} else {
			return configurer.getNotificationTestEmails();
		}
	}
	
	protected String[] filterEmails(String[] emails) {
		if(!configurer.isConfigurationTypeDevelopment()) {
			return emails;
		} else {
			return configurer.getNotificationTestEmails();
		}
	}
	
	protected String getBodyText(String key, HashMap<String, Object> map, Locale locale)
			throws IOException, TemplateException {
		return getMailElement(key, MailElement.BODY_TEXT, map, locale);
	}
	
	protected String getSubject(String key, HashMap<String, Object> map, Locale locale)
			throws IOException, TemplateException {
		return getMailElement(key, MailElement.SUBJECT, map, locale);
	}
	
	protected String getMailElement(String key, MailElement element, HashMap<String, Object> map, Locale locale)
			throws IOException, TemplateException {
		if(map.containsKey(element.toString())) {
			throw new IllegalStateException(String.format("Provided map must not contain %1$s key", element));
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> fMap = (Map<String, Object>) map.clone();
		fMap.put(element.toString(), true);
		return FreeMarkerTemplateUtils.processTemplateIntoString(templateConfiguration.getTemplate(key, locale), fMap);
	}
	
	protected String getFrom() {
		return configurer.getNotificationMailFrom();
	}
	
	protected String getSubjectPrefix() {
		StringBuilder subjectPrefix = new StringBuilder();
		subjectPrefix.append(configurer.getNotificationMailSubjectPrefix());
		if (configurer.isConfigurationTypeDevelopment()) {
			subjectPrefix.append(DEV_SUBJECT_PREFIX);
		}
		return subjectPrefix.toString();
	}
	
	private enum MailElement {
		BODY_TEXT,
		BODY_HTML, // not used at the moment
		SUBJECT
	}
	
}
