package fr.openwide.core.wicket.more.notification.service;

import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.util.lang.Args;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.NodeVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import fr.openwide.core.commons.util.context.IExecutionContext;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.spring.notification.exception.NotificationContentRenderingException;
import fr.openwide.core.spring.notification.model.AbstractExecutionContextNotificationContentDescriptorWrapper;
import fr.openwide.core.spring.notification.model.INotificationContentDescriptor;
import fr.openwide.core.spring.notification.model.INotificationRecipient;
import fr.openwide.core.spring.property.SpringPropertyIds;
import fr.openwide.core.spring.property.service.IPropertyService;
import fr.openwide.core.spring.util.StringUtils;
import fr.openwide.core.wicket.more.notification.model.IWicketNotificationDescriptor;
import fr.openwide.core.wicket.more.notification.service.IHtmlNotificationCssService.IHtmlNotificationCssRegistry;

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
	
	@SuppressWarnings("deprecation")
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
	
	protected abstract class AbstractWicketNotificationDescriptor implements IWicketNotificationDescriptor {

		@Override
		public final String renderSubject() {
			return renderSubject(getDefaultLocale());
		}
		
		protected abstract String renderSubject(Locale locale);
		
		@Override
		public final String renderTextBody() throws NotificationContentRenderingException {
			return null;
		}
		
		public String getVariation() {
			return IWicketNotificationDescriptor.DEFAULT_NOTIFICATION_VARIATION;
		}

		@Override
		public final String renderHtmlBody() {
			return renderHtmlBody(getDefaultLocale());
		}
		
		private String renderHtmlBody(Locale locale) {
			return AbstractNotificationContentDescriptorFactory.this.renderComponent(
					new Supplier<Component>() {
						@Override
						public Component get() {
							return createComponent("htmlComponent");
						}
					},
					locale,
					getVariation()
			);
		}
		
		@Override
		public abstract Component createComponent(String wicketId);
		
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
			return AbstractNotificationContentDescriptorFactory.this.renderString(
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
	@SuppressWarnings("deprecation")
	protected String postProcessHtml(Component component, Locale locale, String variation, String htmlBodyToProcess) {
		htmlBodyToProcess = super.postProcessHtml(component, locale, variation, htmlBodyToProcess);
		IHtmlNotificationCssRegistry cssRegistry = null;
		if (cssService.hasRegistry(variation)) {
			try {
				cssRegistry = cssService.getRegistry(variation);
			} catch (ServiceException e) {
				LOGGER.error("Unable to load the CSS file", e);
			}
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
