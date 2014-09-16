package fr.openwide.core.wicket.more.notification.service;

import java.util.Locale;

import org.apache.http.util.Args;
import org.apache.wicket.Component;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import fr.openwide.core.spring.notification.exception.NotificationContentRenderingException;
import fr.openwide.core.wicket.more.notification.model.IWicketNotificationDescriptor;

public abstract class AbstractNotificationContentDescriptorFactory extends AbstractWicketRendererServiceImpl {
	
	@Override
	protected abstract String getApplicationName();
	
	protected abstract class AbstractWicketNotificationDescriptor implements IWicketNotificationDescriptor {
		
		@Override
		public abstract String renderSubject(Locale locale);
		
		@Override
		public String renderTextBody(Locale locale) throws NotificationContentRenderingException {
			return null;
		}

		@Override
		public final String renderHtmlBody(Locale locale) {
			return AbstractNotificationContentDescriptorFactory.this.renderComponent(
					new Supplier<Component>() {
						@Override
						public Component get() {
							return createComponent("htmlComponent");
						}
					},
					locale
			);
		}
		
		@Override
		public abstract Component createComponent(String wicketId);
	}
	
	protected abstract class AbstractSimpleWicketNotificationDescriptor extends AbstractWicketNotificationDescriptor {
		
		private final String messageKeyRoot;
		
		public AbstractSimpleWicketNotificationDescriptor(String messageKeyRoot) {
			super();
			Args.notBlank("messageKeyRoot", messageKeyRoot);
			this.messageKeyRoot = messageKeyRoot;
		}

		public String getMessageKeyRoot() {
			return messageKeyRoot;
		}

		@Override
		public final String renderSubject(Locale locale) {
			return AbstractNotificationContentDescriptorFactory.this.renderString(
					getSubjectMessageKey(), locale,
					getSubjectParameter(),
					(Object[]) Iterables.toArray(getSubjectPositionalParameters(), Object.class)
			);
		}
		
		protected Object getSubjectParameter() {
			return null;
		}
		
		protected Iterable<?> getSubjectPositionalParameters() {
			return ImmutableList.of();
		}
		
		protected String getSubjectMessageKey() {
			return getMessageKeyRoot() + ".subject";
		}
	}

}
