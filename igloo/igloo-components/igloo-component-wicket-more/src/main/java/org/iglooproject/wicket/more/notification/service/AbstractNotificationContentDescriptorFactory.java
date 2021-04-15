package org.iglooproject.wicket.more.notification.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.util.lang.Args;
import org.iglooproject.commons.util.context.IExecutionContext;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.mail.api.INotificationRecipient;
import org.iglooproject.spring.notification.model.AbstractExecutionContextNotificationContentDescriptorWrapper;
import org.iglooproject.spring.notification.model.INotificationContentBody;
import org.iglooproject.spring.notification.model.INotificationContentDescriptor;
import org.iglooproject.spring.notification.model.NotificationContentBody;
import org.iglooproject.spring.property.SpringPropertyIds;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.more.notification.service.IHtmlNotificationCssService.IHtmlNotificationCssRegistry;
import org.iglooproject.wicket.more.notification.service.jsoup.HtmlToPlainText;
import org.iglooproject.wicket.more.property.WicketMorePropertyIds;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.NodeVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Iterables;

import igloo.wicket.offline.IOfflineComponentProvider;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;

public abstract class AbstractNotificationContentDescriptorFactory extends AbstractWicketRendererServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNotificationContentDescriptorFactory.class);
	
	@Deprecated
	private static final String STYLE_ATTRIBUTE = "style";
	@Deprecated
	private static final String STYLE_ATTRIBUTE_SEPARATOR = "; ";
	
	private static final String LINK_TAG = "a";
	private static final String LINK_TARGET_ATTRIBUTE = "target";
	private static final String LINK_TARGET_ATTRIBUTE_BLANK_VALUE = "_blank";
	
	@Autowired
	private IHtmlNotificationCssService cssService;
	
	@Autowired
	private IPropertyService propertyService;

	private HttpClient inlinerHttpClient = HttpClient.newBuilder()
		.followRedirects(Redirect.NORMAL)
		.build();
	private ObjectMapper inlinerObjectMapper = new ObjectMapper()
		.configure(SerializationFeature.INDENT_OUTPUT, true)
		.setSerializationInclusion(Include.NON_NULL);
	
	protected AbstractNotificationContentDescriptorFactory(IWicketContextProvider wicketContextProvider) {
		super(wicketContextProvider);
	}
	
	protected abstract class AbstractWicketNotificationDescriptor implements INotificationContentDescriptor {
		
		@Override
		public final String renderSubject() {
			return renderSubject(getDefaultLocale());
		}
		
		protected abstract String renderSubject(Locale locale);
		
		@Override
		public final INotificationContentBody renderBody() {
			String htmlText = renderBodyHtmlText(getDefaultLocale());
			String plainText = renderBodyPlainText(htmlText);
			
			return NotificationContentBody.start()
				.with(o -> {
					o.setPlainText(plainText);
					o.setHtmlText(htmlText);
				})
				.build();
		}
		
		@SuppressWarnings("unchecked")
		private String renderBodyHtmlText(Locale locale) {
			return AbstractNotificationContentDescriptorFactory.this.renderPage(
				// we can't ensure that component and class match with available generic information
				IOfflineComponentProvider.fromSupplier(() -> createPage(locale), (Class<Page>) (Object) getComponentClass()),
				locale
			);
		}
		
		private String renderBodyPlainText(String bodyHtmlText) {
			return new HtmlToPlainText().getPlainText(Jsoup.parse(bodyHtmlText));
		}
		
		public abstract Page createPage(Locale locale);
		
		public abstract Class<? extends Component> getComponentClass();
		
		@Override
		public INotificationContentDescriptor withContext(INotificationRecipient recipient) {
			return new Wrapper(getExecutionContext(recipient));
		}
		
		private class Wrapper extends AbstractExecutionContextNotificationContentDescriptorWrapper
				implements INotificationContentDescriptor {
			public Wrapper(IExecutionContext excecutionContext) {
				super(AbstractWicketNotificationDescriptor.this, excecutionContext);
			}
			@Override
			public INotificationContentDescriptor withContext(INotificationRecipient recipient) {
				return AbstractWicketNotificationDescriptor.this.withContext(recipient);
			}
		}
		
		private Locale getDefaultLocale() {
			if (Session.exists()) {
				return Session.get().getLocale();
			} else {
				return propertyService.get(SpringPropertyIds.DEFAULT_LOCALE);
			}
		}
	}
	
	/**
	 * @deprecated Use {@link AbstractWicketNotificationDescriptor} instead.
	 */
	@Deprecated
	protected abstract class AbstractWicketNotificationPanelDescriptor implements INotificationContentDescriptor {
		
		@Override
		public final String renderSubject() {
			return renderSubject(getDefaultLocale());
		}
		
		protected abstract String renderSubject(Locale locale);
		
		@Override
		public final INotificationContentBody renderBody() {
			String htmlText = renderBodyHtmlText(getDefaultLocale());
			String plainText = renderBodyPlainText(htmlText);
			
			return NotificationContentBody.start()
				.with(o -> {
					o.setPlainText(plainText);
					o.setHtmlText(htmlText);
				})
				.build();
		}
		
		@SuppressWarnings("unchecked")
		private String renderBodyHtmlText(Locale locale) {
			return AbstractNotificationContentDescriptorFactory.this.renderComponent(
				// we can't ensure that component and class match with available generic information
				IOfflineComponentProvider.<Component>fromSupplier(() -> createComponent("htmlComponent"), (Class) (Object) getComponentClass()),
				locale
			);
		}
		
		private String renderBodyPlainText(String bodyHtmlText) {
			return new HtmlToPlainText().getPlainText(Jsoup.parse(bodyHtmlText));
		}
		
		public abstract Component createComponent(String wicketId);
		
		public abstract Class<? extends Component> getComponentClass();
		
		@Override
		public INotificationContentDescriptor withContext(INotificationRecipient recipient) {
			return new Wrapper(getExecutionContext(recipient));
		}
		
		private class Wrapper extends AbstractExecutionContextNotificationContentDescriptorWrapper
				implements INotificationContentDescriptor {
			public Wrapper(IExecutionContext excecutionContext) {
				super(AbstractWicketNotificationPanelDescriptor.this, excecutionContext);
			}
			@Override
			public INotificationContentDescriptor withContext(INotificationRecipient recipient) {
				return AbstractWicketNotificationPanelDescriptor.this.withContext(recipient);
			}
		}
		
		private Locale getDefaultLocale() {
			if (Session.exists()) {
				return Session.get().getLocale();
			} else {
				return propertyService.get(SpringPropertyIds.DEFAULT_LOCALE);
			}
		}
	}
	
	protected abstract class AbstractSimpleWicketNotificationDescriptor extends AbstractWicketNotificationDescriptor {
		
		private final String messageKeyRoot;
		
		protected AbstractSimpleWicketNotificationDescriptor(String messageKeyRoot) {
			super();
			Args.notEmpty(messageKeyRoot, "messageKeyRoot");
			this.messageKeyRoot = messageKeyRoot;
		}
		
		public String getMessageKeyRoot() {
			return messageKeyRoot;
		}
		
		@Override
		public final String renderSubject(Locale locale) {
			return AbstractNotificationContentDescriptorFactory.this.getRendererService().localize(
				getSubjectMessageKey(),
				locale,
				getSubjectParameter(locale),
				Iterables.toArray(getSubjectPositionalParameters(locale), Object.class)
			);
		}
		
		protected Object getSubjectParameter() {
			return null;
		}
		
		protected Object getSubjectParameter(Locale locale) {
			return getSubjectParameter();
		}
		
		protected Iterable<?> getSubjectPositionalParameters() {
			return List.of();
		}
		
		protected Iterable<?> getSubjectPositionalParameters(Locale locale) {
			return getSubjectPositionalParameters();
		}
		
		protected String getSubjectMessageKey() {
			return getMessageKeyRoot() + ".subject";
		}
	}
	
	/**
	 * @deprecated Use {@link AbstractSimpleWicketNotificationDescriptor} instead.
	 */
	@Deprecated
	protected abstract class AbstractSimpleWicketNotificationPanelDescriptor extends AbstractWicketNotificationPanelDescriptor {
		
		private final String messageKeyRoot;
		
		protected AbstractSimpleWicketNotificationPanelDescriptor(String messageKeyRoot) {
			super();
			Args.notEmpty(messageKeyRoot, "messageKeyRoot");
			this.messageKeyRoot = messageKeyRoot;
		}
		
		public String getMessageKeyRoot() {
			return messageKeyRoot;
		}
		
		@Override
		public final String renderSubject(Locale locale) {
			return AbstractNotificationContentDescriptorFactory.this.getRendererService().localize(
				getSubjectMessageKey(),
				locale,
				getSubjectParameter(locale),
				Iterables.toArray(getSubjectPositionalParameters(locale), Object.class)
			);
		}
		
		protected Object getSubjectParameter() {
			return null;
		}
		
		protected Object getSubjectParameter(Locale locale) {
			return getSubjectParameter();
		}
		
		protected Iterable<?> getSubjectPositionalParameters() {
			return List.of();
		}
		
		protected Iterable<?> getSubjectPositionalParameters(Locale locale) {
			return getSubjectPositionalParameters();
		}
		
		protected String getSubjectMessageKey() {
			return getMessageKeyRoot() + ".subject";
		}
	}
	
	@Override
	protected String postProcessHtml(Component component, String htmlBody, Locale locale, String variation) {
		if (Objects.equals(propertyService.get(WicketMorePropertyIds.NOTIFICATION_INLINER_JUICE_ENABLED), true)) {
			if (component instanceof Page) {
				return postProcessJuice(postProcessTargetBlank(htmlBody));
			} else {
				return postProcessPanelJuice(postProcessTargetBlank(htmlBody), variation);
			}
		} else {
			return postProcessPanelPhloc(htmlBody, locale, variation);
		}
	}
	
	private String postProcessTargetBlank(String html) {
		Document doc = Jsoup.parse(html);
		doc.traverse(new AddTargetBlankAttributesNodeVisitor());
		return doc.html();
	}
	
	private class AddTargetBlankAttributesNodeVisitor implements NodeVisitor {
		
		@Override
		public void head(Node node, int depth) {
			if (LINK_TAG.equals(node.nodeName())) {
				node.attr(LINK_TARGET_ATTRIBUTE, LINK_TARGET_ATTRIBUTE_BLANK_VALUE);
			}
		}
		
		@Override
		public void tail(Node node, int depth) {
			// nothing to do
		}
	}
	
	private String postProcessJuice(String html) {
		return postProcessJuice(html, null);
	}

	private String postProcessJuice(String html, String extraCss) {
		try {
			InlinerQuery query = new InlinerQuery();
			query.setContent(html);
			
			InlinerOptions options = new InlinerOptions();
			options.setExtraCss(extraCss);
			options.setRemoveStyleTags(false);
			query.setOptions(options);
			
			String rawQuery = inlinerObjectMapper.writeValueAsString(query);
			
			HttpRequest request = HttpRequest
				.newBuilder(URI.create("http://localhost:8000/juice"))
				.method("GET", BodyPublishers.ofString(rawQuery))
				.header("Content-Type", "application/json")
				.timeout(Duration.ofMinutes(1))
				.build();
			
			CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("juice-css");
			
			Retry retry =  Retry.of(
				"juice-css",
				RetryConfig.<HttpResponse<String>>custom()
					.maxAttempts(3)
					.waitDuration(Duration.ofMillis(500))
					.retryOnResult(response -> response.statusCode() != 200)
					.retryExceptions(IOException.class, HttpTimeoutException.class)
					.build()
			);
			
			HttpResponse<String> response = Decorators.ofCallable(() -> inlinerHttpClient.send(request, BodyHandlers.ofString()))
				.withCircuitBreaker(circuitBreaker)
				.withRetry(retry)
				.call();
			
			if (response.statusCode() != 200) {
				throw new IllegalStateException("Error inlining HTML CSS, response status code %s.".formatted(response.statusCode()));
			}
			
			return response.body();
		} catch (Exception e) {
			if (e instanceof InterruptedException) { //NOSONAR
				Thread.currentThread().interrupt();
			}
			throw new IllegalStateException("Error inlining HTML CSS", e);
		}
	}
	
	public static class InlinerQuery {
		
		private String content;
		
		private InlinerOptions options;
		
		public synchronized String getContent() {
			return content;
		}
		
		public synchronized void setContent(String content) {
			this.content = content;
		}
		
		public synchronized InlinerOptions getOptions() {
			return options;
		}
		
		public synchronized void setOptions(InlinerOptions options) {
			this.options = options;
		}
	}

	/**
	 * @deprecated Use {@link #postProcessJuice(String)} instead.
	 */
	@Deprecated
	private String postProcessPanelJuice(String html, String variation) {
		String extraCss = null;
		try {
			extraCss = cssService.getCss(variation);
		} catch (ServiceException e) {
			LOGGER.error("Unable to get the extra CSS", e);
			throw new IllegalStateException("Error inlining HTML CSS");
		}
		
		return postProcessJuice(html, extraCss);
	}

	public static class InlinerOptions {
		
		private Boolean applyAttributesTableElements;
		
		private Boolean applyHeightAttributes;
		
		private Boolean applyStyleTags;
		
		private Boolean applyWidthAttributes;
		
		private String extraCss;
		
		private Boolean insertPreservedExtraCss;
		
		private Boolean inlinePseudoElements;
		
		private Boolean preserveFontFaces;
		
		private Boolean preserveImportant;
		
		private Boolean preserveMediaQueries;
		
		private Boolean preserveKeyFrames;
		
		private Boolean preservePseudos;
		
		private Boolean removeStyleTags;
		
		private Boolean xmlMode;
		
		public synchronized Boolean getApplyAttributesTableElements() {
			return applyAttributesTableElements;
		}
		
		public synchronized void setApplyAttributesTableElements(Boolean applyAttributesTableElements) {
			this.applyAttributesTableElements = applyAttributesTableElements;
		}
		
		public synchronized Boolean getApplyHeightAttributes() {
			return applyHeightAttributes;
		}
		
		public synchronized void setApplyHeightAttributes(Boolean applyHeightAttributes) {
			this.applyHeightAttributes = applyHeightAttributes;
		}
		
		public synchronized Boolean getApplyStyleTags() {
			return applyStyleTags;
		}
		
		public synchronized void setApplyStyleTags(Boolean applyStyleTags) {
			this.applyStyleTags = applyStyleTags;
		}
		
		public synchronized Boolean getApplyWidthAttributes() {
			return applyWidthAttributes;
		}
		
		public synchronized void setApplyWidthAttributes(Boolean applyWidthAttributes) {
			this.applyWidthAttributes = applyWidthAttributes;
		}
		
		public synchronized String getExtraCss() {
			return extraCss;
		}
		
		public synchronized void setExtraCss(String extraCss) {
			this.extraCss = extraCss;
		}
		
		public synchronized Boolean getInsertPreservedExtraCss() {
			return insertPreservedExtraCss;
		}
		
		public synchronized void setInsertPreservedExtraCss(Boolean insertPreservedExtraCss) {
			this.insertPreservedExtraCss = insertPreservedExtraCss;
		}
		
		public synchronized Boolean getInlinePseudoElements() {
			return inlinePseudoElements;
		}
		
		public synchronized void setInlinePseudoElements(Boolean inlinePseudoElements) {
			this.inlinePseudoElements = inlinePseudoElements;
		}
		
		public synchronized Boolean getPreserveFontFaces() {
			return preserveFontFaces;
		}
		
		public synchronized void setPreserveFontFaces(Boolean preserveFontFaces) {
			this.preserveFontFaces = preserveFontFaces;
		}
		
		public synchronized Boolean getPreserveImportant() {
			return preserveImportant;
		}
		
		public synchronized void setPreserveImportant(Boolean preserveImportant) {
			this.preserveImportant = preserveImportant;
		}
		
		public synchronized Boolean getPreserveMediaQueries() {
			return preserveMediaQueries;
		}
		
		public synchronized void setPreserveMediaQueries(Boolean preserveMediaQueries) {
			this.preserveMediaQueries = preserveMediaQueries;
		}
		
		public synchronized Boolean getPreserveKeyFrames() {
			return preserveKeyFrames;
		}
		
		public synchronized void setPreserveKeyFrames(Boolean preserveKeyFrames) {
			this.preserveKeyFrames = preserveKeyFrames;
		}
		
		public synchronized Boolean getPreservePseudos() {
			return preservePseudos;
		}
		
		public synchronized void setPreservePseudos(Boolean preservePseudos) {
			this.preservePseudos = preservePseudos;
		}
		
		public synchronized Boolean getRemoveStyleTags() {
			return removeStyleTags;
		}
		
		public synchronized void setRemoveStyleTags(Boolean removeStyleTags) {
			this.removeStyleTags = removeStyleTags;
		}
		
		public synchronized Boolean getXmlMode() {
			return xmlMode;
		}
		
		public synchronized void setXmlMode(Boolean xmlMode) {
			this.xmlMode = xmlMode;
		}
	}
	
	/**
	 * @deprecated Use {@link #postProcessJuice(String)} instead.
	 */
	@Deprecated
	private String postProcessPanelPhloc(String htmlBody, Locale locale, String variation) {
		IHtmlNotificationCssRegistry cssRegistry = null;
		try {
			cssRegistry = cssService.getRegistry(variation);
		} catch (ServiceException e) {
			LOGGER.error("Unable to load the CSS file", e);
		}
		if (cssRegistry == null) {
			LOGGER.error("No style linked to variation '{}'", variation);
		}
		
		Document doc = Jsoup.parse(htmlBody);
		doc.traverse(new AddStyleAndTargetBlankAttributesNodeVisitor(cssRegistry));
		return doc.html();
	}
	
	private class AddStyleAndTargetBlankAttributesNodeVisitor implements NodeVisitor {
		private final IHtmlNotificationCssRegistry cssRegistry;
		
		private AddStyleAndTargetBlankAttributesNodeVisitor(IHtmlNotificationCssRegistry cssRegistry) {
			this.cssRegistry = cssRegistry;
		}
		
		@Override
		public void head(Node node, int depth) {
			if (cssRegistry != null) {
				String style = cleanAttribute(cssRegistry.getStyle(node));
				
				if (StringUtils.hasText(style)) {
					String existingStyleAttribute = cleanAttribute(node.attr(STYLE_ATTRIBUTE));
					
					StringBuilder styleAttributeSb = new StringBuilder();
					if (StringUtils.hasText(existingStyleAttribute)) {
						styleAttributeSb.append(existingStyleAttribute);
						styleAttributeSb.append(STYLE_ATTRIBUTE_SEPARATOR);
					}
					styleAttributeSb.append(style);
					
					node.attr(STYLE_ATTRIBUTE, styleAttributeSb.toString());
				}
			}
			
			if (LINK_TAG.equals(node.nodeName())) {
				node.attr(LINK_TARGET_ATTRIBUTE, LINK_TARGET_ATTRIBUTE_BLANK_VALUE);
			}
		}
		
		@Override
		public void tail(Node node, int depth) {
			// nothing to do
		}
		
		private String cleanAttribute(String attribute) {
			return StringUtils.trimTrailingCharacter(
					StringUtils.trimLeadingCharacter(
							StringUtils.trimWhitespace(attribute),
							';'
					),
					';'
			);
		}
	}
	
	public IExecutionContext getExecutionContext(INotificationRecipient recipient) {
		return context(recipient.getLocale());
	}

}
