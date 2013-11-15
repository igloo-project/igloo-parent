package fr.openwide.core.basicapp.core.business.notification.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.spring.notification.service.AbstractNotificationServiceImpl;

@Service("notificationService")
public class NotificationServiceImpl extends AbstractNotificationServiceImpl implements INotificationService {

	@Autowired
	private INotificationUrlBuilderService notificationUrlBuilderService;
	
	@Autowired
	private IBasicApplicationNotificationPanelRendererService notificationPanelRendererService;
	
	@Override
	public void sendExampleNotification(User user) throws ServiceException {
		Date date = new Date();
		String url = notificationUrlBuilderService.getUserDescriptionUrl(user);
		String htmlPanel = notificationPanelRendererService.renderExampleNotificationPanel(user, date);
		
		try {
			builder().to(user).template("example.ftl")
					.variable("userFullName", user.getFullName())
					.variable("date", date)
					.variable("url", url)
					.htmlBody(htmlPanel)
					.send();
		} catch (Exception e) {
			throw new ServiceException("Error during send mail process", e);
		}
	}
}
