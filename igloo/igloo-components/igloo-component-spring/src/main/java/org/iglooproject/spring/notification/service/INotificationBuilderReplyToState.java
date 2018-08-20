package org.iglooproject.spring.notification.service;

import org.iglooproject.spring.notification.model.INotificationRecipient;

public interface INotificationBuilderReplyToState extends INotificationBuilderToState {
	
	INotificationBuilderToState replyToAddress(String replyTo);
	
	INotificationBuilderToState replyTo(INotificationRecipient replyTo);
	
}
