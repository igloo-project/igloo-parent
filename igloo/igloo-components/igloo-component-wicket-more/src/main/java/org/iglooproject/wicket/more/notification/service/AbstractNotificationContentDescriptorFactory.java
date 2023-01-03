package org.iglooproject.wicket.more.notification.service;

import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.util.lang.Args;
import org.iglooproject.commons.util.context.IExecutionContext;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.mail.api.INotificationRecipient;
import org.iglooproject.spring.notification.exception.NotificationContentRenderingException;
import org.iglooproject.spring.notification.model.AbstractExecutionContextNotificationContentDescriptorWrapper;
import org.iglooproject.spring.notification.model.INotificationContentDescriptor;
import org.iglooproject.spring.property.SpringPropertyIds;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.more.notification.service.IHtmlNotificationCssService.IHtmlNotificationCssRegistry;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.NodeVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import igloo.wicket.offline.IOfflineComponentProvider;

public abstract class AbstractNotificationContentDescriptorFactory extends AbstractWicketRendererServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNotificationContentDescriptorFactory.class);
	
	private static final String STYLE_ATTRIBUTE = "style";
	private static final String STYLE_ATTRIBUTE_SEPARATOR = "; ";
	
	private static final String LINK_TAG = "a";
	private static final String LINK_TARGET_ATTRIBUTE = "target";
	private static final String LINK_TARGET_ATTRIBUTE_BLANK_VALUE = "_blank";
	
	@Autowired
	private IHtmlNotificationCssService cssService;
	
	@Autowired
	private IPropertyService propertyService;
	
	public AbstractNotificationContentDescriptorFactory(IWicketContextProvider wicketContextProvider) {
		super(wicketContextProvider);
	}
	
	@Override
	protected IWicketContextProvider getWicketContextProvider() {
		return super.getWicketContextProvider();
	}
	
	private Locale getDefaultLocale() {
		if (Session.exists()) {
			return Session.get().getLocale();
		} else {
			return propertyService.get(SpringPropertyIds.DEFAULT_LOCALE);
		}
	}
	
	protected abstract class AbstractWicketNotificationDescriptor implements INotificationContentDescriptor {

		@Override
		public final String renderSubject() {
			return renderSubject(getDefaultLocale());
		}
		
		protected abstract String renderSubject(Locale locale);
		
		@Override
		public final String renderTextBody() throws NotificationContentRenderingException {
			return null;
		}

		@Override
		public final String renderHtmlBody() {
			return renderHtmlBody(getDefaultLocale());
		}
		
		@SuppressWarnings("unchecked")
		private String renderHtmlBody(Locale locale) {
			return AbstractNotificationContentDescriptorFactory.this.renderComponent(
					// we can't ensure that component and class match with available generic information
					IOfflineComponentProvider.<Component>fromSupplier(() -> createComponent("htmlComponent"), (Class) (Object) getComponentClass()),
					locale
			);
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
				super(AbstractWicketNotificationDescriptor.this, excecutionContext);
			}
			@Override
			public INotificationContentDescriptor withContext(INotificationRecipient recipient) {
				return AbstractWicketNotificationDescriptor.this.withContext(recipient);
			}
		}
	}
	
	protected abstract class AbstractSimpleWicketNotificationDescriptor extends AbstractWicketNotificationDescriptor {
		
		private final String messageKeyRoot;
		
		public AbstractSimpleWicketNotificationDescriptor(String messageKeyRoot) {
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
					getSubjectMessageKey(), locale,
					getSubjectParameter(locale),
					(Object[]) Iterables.toArray(getSubjectPositionalParameters(locale), Object.class)
			);
		}
		
		protected Object getSubjectParameter() {
			return null;
		}
		
		protected Object getSubjectParameter(Locale locale) {
			return getSubjectParameter();
		}
		
		protected Iterable<?> getSubjectPositionalParameters() {
			return ImmutableList.of();
		}
		
		protected Iterable<?> getSubjectPositionalParameters(Locale locale) {
			return getSubjectPositionalParameters();
		}
		
		protected String getSubjectMessageKey() {
			return getMessageKeyRoot() + ".subject";
		}
	}
	
	/**
	 * Replace CSS classes by the corresponding style and add a target="_blank" attribute to links.
	 */
	@Override
	protected String postProcessHtml(Component component, Locale locale, String variation, String htmlBodyToProcess) {
		IHtmlNotificationCssRegistry cssRegistry = null;
		try {
			cssRegistry = cssService.getRegistry(variation);
		} catch (ServiceException e) {
			LOGGER.error("Unable to load the CSS file", e);
		}
		if (cssRegistry == null) {
			LOGGER.error("No style linked to variation '{}'", variation);
		}
		
		Document doc = Jsoup.parse(htmlBodyToProcess);
		doc.traverse(new AddStyleAndTargetBlankAttributesNodeVisitor(cssRegistry));
		return doc.html();
	}
	
	public IExecutionContext getExecutionContext(INotificationRecipient recipient) {
		return getWicketContextProvider().context(recipient.getLocale());
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

}
