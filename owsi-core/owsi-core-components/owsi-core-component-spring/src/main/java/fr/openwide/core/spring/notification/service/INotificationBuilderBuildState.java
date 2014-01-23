package fr.openwide.core.spring.notification.service;

import java.util.Collection;

import fr.openwide.core.spring.notification.model.INotificationRecipient;


public interface INotificationBuilderBuildState extends INotificationBuilderToState {

	/**
	 * @deprecated Use {@link #ccAddress(String, String...)} instead.
	 */
	@Deprecated
	INotificationBuilderBuildState cc(String... cc);
	
	INotificationBuilderBuildState ccAddress(String toFirst, String... toOthers);
	
	INotificationBuilderBuildState ccAddress(Collection<String> to);
	
	INotificationBuilderBuildState cc(INotificationRecipient toFirst, INotificationRecipient ... toOthers);
	
	INotificationBuilderBuildState cc(Collection<? extends INotificationRecipient> cc);

	/**
	 * @deprecated Use {@link #bccAddress(String, String...)} instead.
	 */
	@Deprecated
	INotificationBuilderBuildState bcc(String... cc);
	
	INotificationBuilderBuildState bccAddress(String toFirst, String... toOthers);
	
	INotificationBuilderBuildState bccAddress(Collection<String> to);
	
	INotificationBuilderBuildState bcc(INotificationRecipient toFirst, INotificationRecipient ... toOthers);
	
	INotificationBuilderBuildState bcc(Collection<? extends INotificationRecipient> bcc);
	
	INotificationBuilderBodyState subject(String subject);
	
	INotificationBuilderBodyState subject(String prefix, String subject);
	
	INotificationBuilderTemplateState template(String templateKey);
}
