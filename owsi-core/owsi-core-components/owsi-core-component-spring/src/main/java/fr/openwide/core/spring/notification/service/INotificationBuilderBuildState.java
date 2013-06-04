package fr.openwide.core.spring.notification.service;

import java.util.List;

import fr.openwide.core.spring.notification.model.INotificationRecipient;


public interface INotificationBuilderBuildState {

	INotificationBuilderBuildState cc(String... cc);
	
	INotificationBuilderBuildState cc(INotificationRecipient cc);
	
	INotificationBuilderBuildState cc(List<? extends INotificationRecipient> cc);

	INotificationBuilderBuildState bcc(String... bcc);
	
	INotificationBuilderBuildState bcc(INotificationRecipient bcc);
	
	INotificationBuilderBuildState bcc(List<? extends INotificationRecipient> bcc);
	
	INotificationBuilderBodyState subject(String subject);
	
	INotificationBuilderBodyState subject(String prefix, String subject);
	
	INotificationBuilderTemplateState template(String templateKey);
}
