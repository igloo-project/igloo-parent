package org.iglooproject.spring.notification.service;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.iglooproject.spring.notification.util.NotificationSendMode.FILTER_RECIPIENTS;
import static org.iglooproject.spring.notification.util.NotificationSendMode.NO_EMAIL;
import static org.iglooproject.spring.property.SpringPropertyIds.DEFAULT_LOCALE;
import static org.iglooproject.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_DISABLED_RECIPIENT_FALLBACK;
import static org.iglooproject.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_FILTER_EMAILS;
import static org.iglooproject.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_FROM;
import static org.iglooproject.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_SENDER;
import static org.iglooproject.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_SENDER_BEHAVIOR;
import static org.iglooproject.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_SEND_MODE;
import static org.iglooproject.spring.property.SpringPropertyIds.NOTIFICATION_MAIL_SUBJECT_PREFIX;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.text.StringEscapeUtils;
import org.iglooproject.functional.Function2;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.spring.config.util.MailSenderBehavior;
import org.iglooproject.spring.notification.exception.NotificationContentRenderingException;
import org.iglooproject.spring.notification.model.INotificationContentDescriptor;
import org.iglooproject.spring.notification.model.INotificationRecipient;
import org.iglooproject.spring.notification.model.NotificationTarget;
import org.iglooproject.spring.notification.model.SimpleRecipient;
import org.iglooproject.spring.notification.service.impl.ExplicitelyDefinedNotificationContentDescriptorImpl;
import org.iglooproject.spring.notification.service.impl.FirstNotNullNotificationContentDescriptorImpl;
import org.iglooproject.spring.notification.service.impl.FreemarkerTemplateNotificationContentDescriptorImpl;
import org.iglooproject.spring.notification.util.NotificationUtils;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.SpringBeanUtils;
import org.iglooproject.spring.util.StringUtils;
import org.javatuples.LabelValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import freemarker.template.Configuration;

public class NotificationBuilder implements INotificationBuilderInitState, INotificationBuilderBaseState,
		INotificationBuilderBuildState, INotificationBuilderBodyState, INotificationBuilderContentState,
		INotificationBuilderTemplateState, INotificationBuilderSendState {
	
	private static final Charset DEFAULT_MAIL_CHARSET = Charsets.UTF_8;
	
	private static final String NEW_LINE_TEXT_PLAIN = StringUtils.NEW_LINE_ANTISLASH_N;
	private static final String NEW_LINE_HTML = "<br />";
	
	private static final String DEV_SUBJECT_PREFIX = "[Dev]";
	
	private static final Function2<String, NotificationTarget> ADDRESS_TO_TARGET_FUNCTION =
			address -> address != null ? NotificationTarget.of(address) : null;
	
	private static final Function2<INotificationRecipient, NotificationTarget> I_NOTIFICATION_RECIPIENT_TO_TARGET_FUNCTION =
			recipient -> recipient != null ? NotificationTarget.of(recipient.getEmail()) : null;

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private IPropertyService propertyService;
	
	@Autowired
	@Qualifier(value = "freemarkerMailConfiguration")
	private Configuration templateConfiguration;
	
	private ApplicationContext applicationContext;
	
	private String from;
	
	private NotificationTarget sender;
	
	private NotificationTarget replyTo;
	
	private final Map<NotificationTarget, INotificationRecipient> toByAddress = Maps.newLinkedHashMap();
	
	private final Map<NotificationTarget, INotificationRecipient> ccByAddress = Maps.newLinkedHashMap();
	
	private final Map<NotificationTarget, INotificationRecipient> bccByAddress = Maps.newLinkedHashMap();
	
	private final Set<NotificationTarget> recipientsToIgnore = Sets.newHashSet();
	
	private String subjectPrefix;
	
	private INotificationContentDescriptor userContentDescriptor;
	
	private FreemarkerTemplateNotificationContentDescriptorImpl templateContentDescriptor;
	
	private ExplicitelyDefinedNotificationContentDescriptorImpl explicitelyDefinedContentDescriptor;

	/**
	 * This is not a map, since duplicate filenames are allowed. This is not a multimap either, since mutliple
	 * attachements with the same name are not related to each other.
	 */
	private final Collection<LabelValue<String, File>> attachments = Lists.newArrayList();
	
	private final Map<String, File> inlines = Maps.newHashMap();
	
	private final Multimap<String, String> headers = LinkedHashMultimap.create();
	
	private int priority;
	
	private Charset charset;
	
	private boolean bypassDisabledRecipients = false;
	
	protected NotificationBuilder() {
		this(DEFAULT_MAIL_CHARSET);
	}
	
	protected NotificationBuilder(Charset charset) {
		this.charset = charset;
	}
	
	public static INotificationBuilderInitState create() {
		return new NotificationBuilder();
	}
	
	public static INotificationBuilderInitState create(Charset charset) {
		return new NotificationBuilder(charset);
	}
	
	@Override
	public INotificationBuilderBaseState init(ApplicationContext applicationContext) {
		SpringBeanUtils.autowireBean(applicationContext, this);
		this.applicationContext = applicationContext;
		
		this.subjectPrefix = propertyService.get(NOTIFICATION_MAIL_SUBJECT_PREFIX);
		this.explicitelyDefinedContentDescriptor =
				new ExplicitelyDefinedNotificationContentDescriptorImpl(getDefaultLocale());
		SpringBeanUtils.autowireBean(applicationContext, explicitelyDefinedContentDescriptor);
		
		return this;
	}
	
	@Override
	public INotificationBuilderReplyToState from(String from) {
		Assert.hasText(from, "Sender's email address must contain text");
		this.from = from;
		return this;
	}
	
	@Override
	public INotificationBuilderToState replyToAddress(String replyTo) {
		if (StringUtils.hasText(replyTo)) {
			this.replyTo = NotificationTarget.of(replyTo);
		} else {
			this.replyTo = null;
		}
		return this;
	}
	
	@Override
	public INotificationBuilderToState replyTo(INotificationRecipient replyTo) {
		if (replyTo != null) {
			this.replyTo = NotificationTarget.of(replyTo, charset);
		} else {
			this.replyTo = null;
		}
		return this;
	}

	/**
	 * Argument may be either an addr-spec or an address
	 * (<i>local-part@domain</i> or <i>Personal &lt;local-part@domain&lt;</i>)
	 * 
	 * @see InternetAddress
	 */
	@Override
	public INotificationBuilderBaseState sender(String sender) {
		if (StringUtils.hasText(sender)) {
			this.sender = NotificationTarget.ofInternetAddress(sender);
		} else {
			this.sender = null;
		}
		return this;
	}
	
	/**
	 * Arguments must be simple email addresses (addr-spec), not RFC822 address.
	 * 
	 * i.e. arguments must be <i>local-part@domain</i> addresses and NOT <i>Personal &lt;local-part@domain&lt;</i>
	 */
	@Override
	public INotificationBuilderBuildState toAddress(String toFirst, String... toOthers) {
		return toAddress(Lists.asList(toFirst, toOthers));
	}
	
	@Override
	public INotificationBuilderBuildState toAddress(Collection<String> to) {
		if (to != null) {
			for (String email : to) {
				if (StringUtils.hasText(email)) {
					addRecipient(toByAddress, new SimpleRecipient(getDefaultLocale(), email, null));
				}
			}
		}
		return this;
	}
	
	@Override
	public INotificationBuilderBuildState to(INotificationRecipient toFirst, INotificationRecipient ... toOthers) {
		return to(Lists.asList(toFirst, toOthers));
	}
	
	@Override
	public INotificationBuilderBuildState to(Collection<? extends INotificationRecipient> to) {
		addRecipients(toByAddress, to);
		return this;
	}

	@Override
	public INotificationBuilderBuildState bypassDisabledRecipients() {
		this.bypassDisabledRecipients = true;
		return this;
	}
	
	@Override
	public INotificationBuilderBuildState ccAddress(String ccFirst, String... ccOthers) {
		return ccAddress(Lists.asList(ccFirst, ccOthers));
	}
	
	@Override
	public INotificationBuilderBuildState ccAddress(Collection<String> cc) {
		addAddresses(ccByAddress, cc);
		return this;
	}
	
	@Override
	public INotificationBuilderBuildState cc(INotificationRecipient ccFirst, INotificationRecipient ... ccOthers) {
		return cc(Lists.asList(ccFirst, ccOthers));
	}
	
	@Override
	public INotificationBuilderBuildState cc(Collection<? extends INotificationRecipient> cc) {
		addRecipients(ccByAddress, cc);
		return this;
	}
	
	@Override
	public INotificationBuilderBuildState bccAddress(String bccFirst, String... bccOthers) {
		return bccAddress(Lists.asList(bccFirst, bccOthers));
	}
	
	@Override
	public INotificationBuilderBuildState bccAddress(Collection<String> bcc) {
		addAddresses(bccByAddress, bcc);
		return this;
	}
	
	@Override
	public INotificationBuilderBuildState bcc(INotificationRecipient bccFirst, INotificationRecipient ... bccOthers) {
		return bcc(Lists.asList(bccFirst, bccOthers));
	}
	
	@Override
	public INotificationBuilderBuildState bcc(Collection<? extends INotificationRecipient> bcc) {
		addRecipients(bccByAddress, bcc);
		return this;
	}
	
	@Override
	public INotificationBuilderBuildState exceptAddress(Collection<String> except) {
		recipientsToIgnore.addAll(except.stream().map(ADDRESS_TO_TARGET_FUNCTION).collect(Collectors.toList()));
		return this;
	}
	
	@Override
	public INotificationBuilderBuildState exceptAddress(String exceptFirst, String... exceptOthers) {
		return exceptAddress(Lists.asList(exceptFirst, exceptOthers));
	}
	
	@Override
	public INotificationBuilderBuildState except(INotificationRecipient exceptFirst, INotificationRecipient... exceptOthers) {
		return except(Lists.asList(exceptFirst, exceptOthers));
	}
	
	@Override
	public INotificationBuilderBuildState except(Collection<? extends INotificationRecipient> except) {
		recipientsToIgnore.addAll(except.stream().map(I_NOTIFICATION_RECIPIENT_TO_TARGET_FUNCTION).collect(Collectors.toList()));
		return this;
	}
	
	@Override
	public INotificationBuilderTemplateState template(String templateKey) {
		Assert.hasText(templateKey, "Template key must contain text");
		this.templateContentDescriptor =
				new FreemarkerTemplateNotificationContentDescriptorImpl(
						templateConfiguration, templateKey, Collections.unmodifiableCollection(attachments),
						getDefaultLocale()
				);
		SpringBeanUtils.autowireBean(applicationContext, this.templateContentDescriptor);
		return this;
	}
	
	@Override
	public INotificationBuilderTemplateState variable(String name, Object value) {
		return variable(name, value, null);
	}
	
	/**
	 * If locale == null, the variable will be considered as not locale-sensitive and will be available for all locales
	 */
	@Override
	public INotificationBuilderTemplateState variable(String name, Object value, Locale locale) {
		Assert.hasText(name, "Variable name must contain text");
		templateContentDescriptor.setVariable(locale, name, value);
		return this;
	}
	
	@Override
	public INotificationBuilderTemplateState variables(Map<String, ?> variables) {
		return variables(variables, null);
	}

	@Override
	public INotificationBuilderTemplateState variables(Map<String, ?> variables, Locale locale) {
		templateContentDescriptor.setVariables(locale, variables);
		return this;
	}
	
	@Override
	public INotificationBuilderContentState content(INotificationContentDescriptor contentDescriptor) {
		this.userContentDescriptor = checkNotNull(contentDescriptor);
		return this;
	}
	
	@Override
	public INotificationBuilderBodyState subject(String subject) {
		Assert.hasText(subject, "Email subject must contain text");
		this.explicitelyDefinedContentDescriptor.setSubject(null, subject);
		return this;
	}
	
	@Override
	public INotificationBuilderBuildState subjectPrefix(String prefix) {
		this.subjectPrefix = prefix;
		return this;
	}
	
	@Override
	public INotificationBuilderSendState textBody(String textBody) {
		return textBody(textBody, null);
	}
	
	@Override
	public INotificationBuilderSendState textBody(String textBody, Locale locale) {
		this.explicitelyDefinedContentDescriptor.setTextBody(locale, textBody);
		return this;
	}
	
	@Override
	public INotificationBuilderSendState htmlBody(String htmlBody) {
		return htmlBody(htmlBody, null);
	}
	
	// If locale == null, the associated htmlBody will be used for any locale that do not have one
	@Override
	public INotificationBuilderSendState htmlBody(String htmlBody, Locale locale) {
		this.explicitelyDefinedContentDescriptor.setHtmlBody(locale, htmlBody);
		return this;
	}
	
	@Override
	public INotificationBuilderSendState attach(String attachmentFilename, File file) {
		Assert.hasText(attachmentFilename, "Attachment filename must contain text");
		Assert.notNull(file, "Attached file must not be null");
		this.attachments.add(LabelValue.with(attachmentFilename, file));
		return this;
	}
	
	@Override
	public INotificationBuilderSendState attach(Collection<LabelValue<String, File>> attachments) {
		Assert.notNull(attachments, "Attachment map must not be null");
		this.attachments.addAll(attachments);
		return this;
	}
	
	@Override
	public INotificationBuilderSendState attach(Map<String, File> attachments) {
		Assert.notNull(attachments, "Attachment map must not be null");
		for (Map.Entry<String, File> attachment : attachments.entrySet()) {
			attach(attachment.getKey(), attachment.getValue());
		}
		return this;
	}
	
	@Override
	public INotificationBuilderSendState inline(String contentId, File file) {
		Assert.hasText(contentId, "Content ID must contain text");
		Assert.notNull(file, "Inline file must not be null");
		this.inlines.put(contentId, file);
		return this;
	}
	
	@Override
	public INotificationBuilderSendState header(String name, String value) {
		Assert.hasText(name, "Header name must contain text");
		Assert.hasText(value, "Header value must contain text");
		this.headers.put(name, value);
		return this;
	}
	
	@Override
	public INotificationBuilderSendState headers(MultiValueMap<String, String> headers) {
		Assert.notNull(headers, "Header map must not be null");
		for (Entry<String, List<String>> header : headers.entrySet()) {
			for (String value : header.getValue()) {
				header(header.getKey(), value);
			}
		}
		return this;
	}
	
	@Override
	public INotificationBuilderSendState priority(int priority) {
		if (priority < 1 || priority > 5) {
			throw new IllegalArgumentException("Priority must be between 1 (highest) and 5 (lowest)");
		}
		this.priority = priority;
		return this;
	}

	@Override
	public void send() throws ServiceException {
		if (isNotificationsDisabled()) {
			return;
		}
		
		removeDuplicatesAndRecipientsToIgnoreBeforeSendingMessage();
		
		for (Entry<INotificationContentDescriptor, Targets> entry : groupTargetsByContext()) {
			INotificationContentDescriptor contentDescriptor = entry.getKey();
			Targets targets = entry.getValue();
			if (targets.to.isEmpty() && targets.cc.isEmpty() && targets.bcc.isEmpty()) { // Multimap.get(unknown key) returns an empty collection
				continue;
			}
			
			MimeMessage message;
			Address[] effectiveTo;
			Address[] effectiveCc;
			Address[] effectiveBcc;
			Address[] from;
			Address[] replyTo;
			Address sender;
			try {
				// Step 1 : Build message. Effective recipients could be different than expected ones, we store.
				message = buildMessage(contentDescriptor, targets, charset.name());
				if (message == null) {
					continue;
				}
				effectiveTo = message.getRecipients(RecipientType.TO);
				effectiveCc = message.getRecipients(RecipientType.CC);
				effectiveBcc = message.getRecipients(RecipientType.BCC);
				replyTo = message.getReplyTo();
				from = message.getFrom();
				sender = message.getSender();
			} catch (NotificationContentRenderingException e) {
				throw new ServiceException("Error while rendering email notification", e);
			} catch (MessagingException e) {
				throw new ServiceException(
						String.format("Error building the MIME message (to:%s, cc:%s, bcc:%s)", targets.to, targets.cc, targets.bcc),
						e
				);
			}
			
			try {
				// Step 2 : Send message.
				mailSender.send(message);
			} catch (MailException e) {
				throw new ServiceException(
						String.format("Error sending email notification "
								+ "(from:%s, sender:%s, reply-to: %s; "
								+ "original to:%s, cc:%s, bcc:%s; "
								+ "effective to:%s, cc:%s, bcc:%s)",
								Arrays.toString(from), sender, Arrays.toString(replyTo),
								targets.to, targets.cc, targets.bcc, 
								Arrays.toString(effectiveTo), Arrays.toString(effectiveCc), Arrays.toString(effectiveBcc)),
						e
				);
			}
		}
	}
	
	private static class Targets {
		private final Collection<NotificationTarget> to = Lists.newArrayList();
		private final Collection<NotificationTarget> cc = Lists.newArrayList();
		private final Collection<NotificationTarget> bcc = Lists.newArrayList();
		
		public void to(NotificationTarget target) {
			to.add(target);
		}
		
		public void cc(NotificationTarget target) {
			cc.add(target);
		}
		
		public void bcc(NotificationTarget target) {
			bcc.add(target);
		}
	}
	
	private static class TargetContextGrouper {
		
		private final INotificationContentDescriptor noContextContentDescriptor;
		
		private final Map<INotificationContentDescriptor, Targets> data = Maps.newLinkedHashMap();
		
		public TargetContextGrouper(INotificationContentDescriptor noContextContentDescriptor) {
			super();
			this.noContextContentDescriptor = noContextContentDescriptor;
		}
		
		public void to(NotificationTarget target, INotificationRecipient context) {
			get(context).to(target);
		}
		
		public void cc(NotificationTarget target, INotificationRecipient context) {
			get(context).cc(target);
		}
		
		public void bcc(NotificationTarget target, INotificationRecipient context) {
			get(context).bcc(target);
		}

		private Targets get(INotificationRecipient context) {
			INotificationContentDescriptor contentDescriptorWithContext =
					noContextContentDescriptor.withContext(context);
			Targets result = data.get(contentDescriptorWithContext);
			if (result == null) {
				result = new Targets();
				data.put(contentDescriptorWithContext, result);
			}
			return result;
		}
		
		public Iterable<Map.Entry<INotificationContentDescriptor, Targets>> getAll() {
			return data.entrySet();
		}
	}
	
	private Iterable<Entry<INotificationContentDescriptor, Targets>> groupTargetsByContext() {
		INotificationContentDescriptor contentDescriptor = chooseNotificationContentDescriptor();
		TargetContextGrouper result = new TargetContextGrouper(contentDescriptor);
		
		for (Map.Entry<NotificationTarget, INotificationRecipient> entry : toByAddress.entrySet()) {
			result.to(entry.getKey(), entry.getValue());
		}

		for (Map.Entry<NotificationTarget, INotificationRecipient> entry : ccByAddress.entrySet()) {
			result.cc(entry.getKey(), entry.getValue());
		}

		for (Map.Entry<NotificationTarget, INotificationRecipient> entry : bccByAddress.entrySet()) {
			result.bcc(entry.getKey(), entry.getValue());
		}
		
		return result.getAll();
	}

	private void addAddresses(Map<NotificationTarget, INotificationRecipient> recipientsByTarget,
			Iterable<? extends String> addresses) {
		if (addresses != null) {
			for (String address : addresses) {
				addAddress(recipientsByTarget, address);
			}
		}
	}

	private void addRecipients(Map<NotificationTarget, INotificationRecipient> recipientsByTarget,
			Iterable<? extends INotificationRecipient> recipients) {
		if (recipients != null) {
			for (INotificationRecipient recipient : recipients) {
				addRecipient(recipientsByTarget, recipient);
			}
		}
	}

	private void addAddress(Map<NotificationTarget, INotificationRecipient> recipientsByTarget,
			String address) {
		if (StringUtils.hasText(address)) {
			addRecipient(recipientsByTarget, new SimpleRecipient(getDefaultLocale(), address, null));
		}
	}
	
	private void addRecipient(Map<NotificationTarget, INotificationRecipient> recipientsByTarget,
			INotificationRecipient recipient) {
		if ((recipient.isNotificationEnabled() || bypassDisabledRecipients) && recipient.isActive() && StringUtils.hasText(recipient.getEmail())) {
			addRecipientUnsafe(
					recipientsByTarget,
					NotificationTarget.of(recipient, charset), recipient
			);
		} else if (propertyService.get(NOTIFICATION_MAIL_DISABLED_RECIPIENT_FALLBACK) != null) {
			for (String redirectionEmail : propertyService.get(NOTIFICATION_MAIL_DISABLED_RECIPIENT_FALLBACK) ) {
				addRecipientUnsafe(
						recipientsByTarget,
						NotificationTarget.of(redirectionEmail), new SimpleRecipient(recipient, redirectionEmail)
				);
			}
		}
	}
	
	private void addRecipientUnsafe(Map<NotificationTarget, INotificationRecipient> recipientsByTarget,
			NotificationTarget target, INotificationRecipient recipient) {
		if (!recipientsByTarget.containsKey(target)) {
			recipientsByTarget.put(target, recipient);
		}
	}
	
	private void removeDuplicatesAndRecipientsToIgnoreBeforeSendingMessage() {
		toByAddress.keySet().removeAll(recipientsToIgnore);
		
		ccByAddress.keySet().removeAll(recipientsToIgnore);
		ccByAddress.keySet().removeAll(toByAddress.keySet());
		
		bccByAddress.keySet().removeAll(recipientsToIgnore);
		bccByAddress.keySet().removeAll(toByAddress.keySet());
		bccByAddress.keySet().removeAll(ccByAddress.keySet());
	}
	
	private INotificationContentDescriptor chooseNotificationContentDescriptor() {
		List<INotificationContentDescriptor> descriptors = Lists.newArrayList();
		if (userContentDescriptor != null) { // Maximum priority
			descriptors.add(userContentDescriptor);
		}
		if (templateContentDescriptor != null) {
			descriptors.add(templateContentDescriptor);
		}
		descriptors.add(explicitelyDefinedContentDescriptor); // Minimum priority
		return new FirstNotNullNotificationContentDescriptorImpl(descriptors);
	}
	
	private MimeMessage buildMessage(INotificationContentDescriptor contentDescriptor, Targets recipients,
			String encoding) throws NotificationContentRenderingException, MessagingException {
		String subject = buildSubject(contentDescriptor.renderSubject());
		String textBody = contentDescriptor.renderTextBody();
		String htmlBody = contentDescriptor.renderHtmlBody();
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, isMultipartNeeded(textBody, htmlBody), encoding);
		
		if (from == null) {
			from = getDefaultFrom();
		}
		helper.setFrom(from);
		
		if (sender != null) {
			message.setSender(sender.getAddress());
		} else {
			String defaultEmailSender = getDefaultSender(from);
			if (defaultEmailSender != null && !defaultEmailSender.isEmpty()) {
				message.setSender(NotificationTarget.ofInternetAddress(defaultEmailSender).getAddress());
			}
		}
		
		Collection<NotificationTarget> filteredTos = filterTo(recipients.to);
		Collection<NotificationTarget> filteredCcs = filterCcBcc(recipients.cc);
		Collection<NotificationTarget> filteredBccs = filterCcBcc(recipients.bcc);
		if (filteredTos.isEmpty() && filteredCcs.isEmpty() && filteredBccs.isEmpty()) {
			return null;
		}
		
		if (replyTo != null) {
			helper.setReplyTo(replyTo.getAddress());
		}
		for (NotificationTarget to : filteredTos) {
			helper.addTo(to.getAddress());
		}
		for (NotificationTarget cc : filteredCcs) {
			helper.addCc(cc.getAddress());
		}
		for (NotificationTarget bcc : filteredBccs) {
			helper.addBcc(bcc.getAddress());
		}
		
		helper.setSubject(subject);

		String textBodyPrefix = getBodyPrefix(recipients.to, recipients.cc, recipients.bcc, encoding, MailFormat.TEXT);
		String htmlBodyPrefix = getBodyPrefix(recipients.to, recipients.cc, recipients.bcc, encoding, MailFormat.HTML);
		
		if (StringUtils.hasText(textBody) && StringUtils.hasText(htmlBody)) {
			helper.setText(textBodyPrefix + textBody, htmlBodyPrefix + htmlBody);
		} else if (StringUtils.hasText(htmlBody)) {
			helper.setText(htmlBodyPrefix + htmlBody, true);
		} else {
			helper.setText(textBodyPrefix + textBody);
		}
	
		for (LabelValue<String, File> attachment : attachments) {
			helper.addAttachment(attachment.getLabel(), attachment.getValue());
		}
		for (Map.Entry<String, File> inline : inlines.entrySet()) {
			helper.addInline(inline.getKey(), inline.getValue());
		}
		for (Entry<String, Collection<String>> header : headers.asMap().entrySet()) {
			for (String value : header.getValue()) {
				message.addHeader(header.getKey(), value);
			}
		}
		if (priority != 0) {
			helper.setPriority(priority);
		}
		return message;
	}
	
	private String buildSubject(String subject) {
		StringBuilder builder = new StringBuilder();
		if (StringUtils.hasText(subjectPrefix)) {
			builder.append(subjectPrefix);
		}
		if (propertyService.isConfigurationTypeDevelopment()) {
			builder.append(DEV_SUBJECT_PREFIX);
		}
		if (builder.length() > 0) {
			builder.append(" ");
		}
		builder.append(subject);
		return builder.toString();
	}

	private boolean isMultipartNeeded(String textBody, String htmlBody) {
		boolean multipleBodies = StringUtils.hasText(textBody) && StringUtils.hasText(htmlBody);
		return multipleBodies || !attachments.isEmpty() || !inlines.isEmpty();
	}
	
	private String getDefaultFrom() {
		return propertyService.get(NOTIFICATION_MAIL_FROM);
	}
	
	private String getDefaultSender(String from) {
		MailSenderBehavior behavior = propertyService.get(NOTIFICATION_MAIL_SENDER_BEHAVIOR);
		switch (behavior) {
		/** Do not override builder value */
		case EXPLICIT:
			return null;
		/** Use value from configuration */
		case FALLBACK_TO_CONFIGURATION:
			return propertyService.get(NOTIFICATION_MAIL_SENDER);
		/** Use value from From: */
		case FALLBACK_TO_FROM:
			return from;
		default:
			throw new IllegalStateException(String.format("Unknown value %s for %s",
					behavior, MailSenderBehavior.class.getSimpleName()));
		}
	}
	
	private Locale getDefaultLocale() {
		return propertyService.get(DEFAULT_LOCALE);
	}
	
	protected boolean isMailRecipientsFiltered() {
		return propertyService.isConfigurationTypeDevelopment() || 
				FILTER_RECIPIENTS.equals(propertyService.get(NOTIFICATION_MAIL_SEND_MODE));
	}
	
	protected boolean isNotificationsDisabled() {
		return !NotificationUtils.isNotificationsEnabled() ||
				NO_EMAIL.equals(propertyService.get(NOTIFICATION_MAIL_SEND_MODE));
	}
	
	private Collection<NotificationTarget> filterTo(Collection<NotificationTarget> emails) {
		if (isMailRecipientsFiltered()) {
			return getNotificationFilterEmails();
		}
		return emails;
	}
	
	protected Collection<NotificationTarget> getNotificationFilterEmails() {
		return propertyService.get(NOTIFICATION_MAIL_FILTER_EMAILS).stream().map(ADDRESS_TO_TARGET_FUNCTION).collect(Collectors.toList());
	}
	
	private Collection<NotificationTarget> filterCcBcc(Collection<NotificationTarget> emails) {
		if (isMailRecipientsFiltered()) {
			return Sets.newHashSetWithExpectedSize(0);
		}
		return emails;
	}
	
	private String getBodyPrefix(Collection<NotificationTarget> to, Collection<NotificationTarget> cc, Collection<NotificationTarget> bcc,
			String encoding, MailFormat mailFormat) {
		if (isMailRecipientsFiltered()) {
			String newLine = MailFormat.HTML.equals(mailFormat) ? NEW_LINE_HTML : NEW_LINE_TEXT_PLAIN;
			
			StringBuffer newBody = new StringBuffer();
			newBody.append("#############").append(newLine);
			newBody.append("#").append(newLine);
			newBody.append("# To: ").append(renderAddressesForDebug(to, mailFormat)).append(newLine);
			newBody.append("# Cc: ").append(renderAddressesForDebug(cc, mailFormat)).append(newLine);
			newBody.append("# Bcc: ").append(renderAddressesForDebug(bcc, mailFormat)).append(newLine);
			newBody.append("#").append(newLine);
			newBody.append("# Encoding: ").append(encoding).append(newLine);
			newBody.append("#").append(newLine);
			newBody.append("#############").append(newLine).append(newLine).append(newLine);
			
			return newBody.toString();
		}
		return "";
	}
	
	private String renderAddressesForDebug(Collection<NotificationTarget> addresses, MailFormat mailFormat) {
		String debug;
		
		if (addresses != null && !addresses.isEmpty()) {
			debug = Joiner.on(", ").join(addresses);
		} else {
			debug = "<none>";
		}
		if (MailFormat.HTML.equals(mailFormat)) {
			return StringEscapeUtils.escapeHtml4(debug);
		} else {
			return debug;
		}
	}
	
	private enum MailFormat {
		HTML,
		TEXT
	}
}
