package fr.openwide.core.basicapp.core.business.notification.service;

import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.spring.notification.model.SimpleRecipient;
import fr.openwide.core.spring.notification.service.AbstractNotificationServiceImpl;

@Service("notificationService")
public class NotificationServiceImpl extends AbstractNotificationServiceImpl implements INotificationService {

	@Autowired
	private INotificationUrlBuilderService notificationUrlBuilderService;
	
	@Autowired
	private IBasicApplicationNotificationContentDescriptorFactory contentDescriptorFactory;
	
	@Override
	public void sendExampleNotification(User user) throws ServiceException {
		Date date = new Date();
		String url = notificationUrlBuilderService.getUserDescriptionUrl(user);
		
		try {
			builder().to(user)
					.content(contentDescriptorFactory.example(user, date))
					.template("example.ftl")
					.variable("userFullName", user.getFullName())
					.variable("date", date)
					.variable("url", url)
					.send();
		} catch (RuntimeException | ServiceException e) {
			throw new ServiceException("Error during send mail process", e);
		}
	}
	
	@Override
	public void sendUserPasswordRecoveryRequest(User user) throws ServiceException {
		try {
			builder()
					.to(user)
					.content(contentDescriptorFactory.userPasswordRecoveryRequest(user))
					.send();
		} catch (RuntimeException | ServiceException e) {
			throw new ServiceException("Error during send mail process", e);
		}
	}

	@Override
	public void sendExampleNotification(User userTo, String from) throws ServiceException {
		try {
			Date date = new Date();
			builder()
			.from(from)
			.sender("no-reply@basicapp.org")
			.to(new SimpleRecipient(Locale.FRANCE, userTo.getEmail(), userTo.getDisplayName()))
			.content(contentDescriptorFactory.example(userTo, date))
			.send();
		} catch (RuntimeException | ServiceException e) {
			throw new ServiceException("Error during send mail process", e);
		}
		
	}
}
