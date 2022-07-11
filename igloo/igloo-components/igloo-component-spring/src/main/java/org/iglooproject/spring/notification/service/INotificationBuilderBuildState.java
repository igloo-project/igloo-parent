package org.iglooproject.spring.notification.service;

import java.util.Collection;

import org.iglooproject.mail.api.INotificationRecipient;
import org.iglooproject.spring.notification.model.INotificationContentDescriptor;

public interface INotificationBuilderBuildState extends INotificationBuilderToState {

	INotificationBuilderBuildState ccAddress(String ccFirst, String... ccOthers);

	INotificationBuilderBuildState ccAddress(Collection<String> cc);

	INotificationBuilderBuildState cc(INotificationRecipient ccFirst, INotificationRecipient... ccOthers);

	INotificationBuilderBuildState cc(Collection<? extends INotificationRecipient> cc);

	INotificationBuilderBuildState bccAddress(String bccFirst, String... bccOthers);

	INotificationBuilderBuildState bccAddress(Collection<String> bcc);

	INotificationBuilderBuildState bcc(INotificationRecipient bccFirst, INotificationRecipient... bccOthers);

	INotificationBuilderBuildState bcc(Collection<? extends INotificationRecipient> bcc);

	INotificationBuilderContentState content(INotificationContentDescriptor contentDescriptor);

	INotificationBuilderBuildState subjectPrefix(String subject);

	INotificationBuilderBodyState subject(String subject);

	INotificationBuilderTemplateState template(String templateKey);

	INotificationBuilderBuildState except(Collection<? extends INotificationRecipient> except);

	INotificationBuilderBuildState except(INotificationRecipient exceptFirst, INotificationRecipient... exceptOthers);

	INotificationBuilderBuildState exceptAddress(String exceptFirst, String... exceptOthers);

	INotificationBuilderBuildState exceptAddress(Collection<String> except);

}
