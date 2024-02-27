package org.iglooproject.wicket.more.notification.service;

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

import com.google.common.collect.Iterables;

import igloo.juice.IJuiceInliner;
import igloo.wicket.offline.IOfflineComponentProvider;

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

	@Autowired
	private IJuiceInliner juiceInliner;

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
		return juiceInliner.postProcessJuice(html, null);
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
		
		return juiceInliner.postProcessJuice(html, extraCss);
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
