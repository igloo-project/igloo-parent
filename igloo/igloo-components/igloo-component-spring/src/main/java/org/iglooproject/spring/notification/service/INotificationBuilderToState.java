package org.iglooproject.spring.notification.service;

import java.util.Collection;

import org.iglooproject.mail.api.INotificationRecipient;

public interface INotificationBuilderToState {

	INotificationBuilderBuildState toAddress(String toFirst, String... toOthers);

	INotificationBuilderBuildState toAddress(Collection<String> to);

	INotificationBuilderBuildState to(INotificationRecipient toFirst, INotificationRecipient... toOthers);

	INotificationBuilderBuildState to(Collection<? extends INotificationRecipient> to);

	INotificationBuilderBuildState bypassDisabledRecipients();
}
