package fr.openwide.core.spring.notification.service;

import fr.openwide.core.spring.notification.model.INotificationRecipient;

public interface INotificationBuilderReplyToState extends INotificationBuilderToState {
	
	INotificationBuilderToState replyToAddress(String replyTo);
	
	INotificationBuilderToState replyTo(INotificationRecipient replyTo);
	
}
