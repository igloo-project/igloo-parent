package test.wicket.more.notification.service;

import org.iglooproject.spring.notification.model.INotificationContentDescriptor;

public interface INotificationContentDescriptorFactory {

  INotificationContentDescriptor simpleContent(String content);
}
