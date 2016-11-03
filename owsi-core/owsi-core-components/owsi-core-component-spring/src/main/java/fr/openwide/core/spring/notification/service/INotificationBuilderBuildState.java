package fr.openwide.core.spring.notification.service;

import java.util.Collection;

import fr.openwide.core.spring.notification.model.INotificationContentDescriptor;
import fr.openwide.core.spring.notification.model.INotificationRecipient;


public interface INotificationBuilderBuildState extends INotificationBuilderToState {

	/**
	 * @deprecated Use {@link #ccAddress(String, String...)} instead.
	 */
	@Deprecated
	INotificationBuilderBuildState cc(String... cc);
	
	INotificationBuilderBuildState ccAddress(String ccFirst, String... ccOthers);
	
	INotificationBuilderBuildState ccAddress(Collection<String> cc);
	
	INotificationBuilderBuildState cc(INotificationRecipient ccFirst, INotificationRecipient ... ccOthers);
	
	INotificationBuilderBuildState cc(Collection<? extends INotificationRecipient> cc);

	/**
	 * @deprecated Use {@link #bccAddress(String, String...)} instead.
	 */
	@Deprecated
	INotificationBuilderBuildState bcc(String... cc);
	
	INotificationBuilderBuildState bccAddress(String bccFirst, String... bccOthers);
	
	INotificationBuilderBuildState bccAddress(Collection<String> bcc);
	
	INotificationBuilderBuildState bcc(INotificationRecipient bccFirst, INotificationRecipient ... bccOthers);
	
	INotificationBuilderBuildState bcc(Collection<? extends INotificationRecipient> bcc);

	INotificationBuilderContentState content(INotificationContentDescriptor contentDescriptor);
	
	INotificationBuilderBuildState subjectPrefix(String subject);
	
	INotificationBuilderBodyState subject(String subject);

	/**
	 * @deprecated Use {@link #subjectPrefix(String)} and then {@link #subject(String)} instead.
	 */
	@Deprecated
	INotificationBuilderBodyState subject(String prefix, String subject);
	
	INotificationBuilderTemplateState template(String templateKey);

	INotificationBuilderBuildState except(Collection<? extends INotificationRecipient> except);

	INotificationBuilderBuildState except(INotificationRecipient exceptFirst, INotificationRecipient... exceptOthers);

	INotificationBuilderBuildState exceptAddress(String exceptFirst, String... exceptOthers);

	INotificationBuilderBuildState exceptAddress(Collection<String> except);
	
}
