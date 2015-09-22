package fr.openwide.core.basicapp.core.business.notification.service;

import java.util.Date;
import java.util.Locale;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.spring.notification.exception.NotificationContentRenderingException;
import fr.openwide.core.spring.notification.model.INotificationContentDescriptor;


/**
 * Implémentation bouche-trou, uniquement pour combler la dépendance.
 */
public class EmptyNotificationContentDescriptorFactoryImpl implements IBasicApplicationNotificationContentDescriptorFactory<INotificationContentDescriptor> {
	
	private static final INotificationContentDescriptor NULL_DESCRIPTOR = new INotificationContentDescriptor() {
		@Override
		public String renderSubject(Locale locale) {
			return null;
		}
		
		@Override
		public String renderHtmlBody(Locale locale) throws NotificationContentRenderingException {
			return null;
		}
		
		@Override
		public String renderTextBody(Locale locale) throws NotificationContentRenderingException {
			return null;
		}
	};

	@Override
	public INotificationContentDescriptor example(User user, Date date) {
		return NULL_DESCRIPTOR;
	}

	@Override
	public INotificationContentDescriptor userPasswordRecoveryRequest(User user) {
		return NULL_DESCRIPTOR;
	}

}
