package org.iglooproject.spring.notification.service;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import org.iglooproject.jpa.exception.ServiceException;
import org.javatuples.LabelValue;
import org.springframework.util.MultiValueMap;

public interface INotificationBuilderSendState {

  INotificationBuilderSendState attach(String attachmentFilename, File file);

  INotificationBuilderSendState attach(Map<String, File> attachments);

  INotificationBuilderSendState attach(Collection<LabelValue<String, File>> attachments);

  INotificationBuilderSendState inline(String contentId, File file);

  INotificationBuilderSendState header(String name, String value);

  INotificationBuilderSendState headers(MultiValueMap<String, String> headers);

  INotificationBuilderSendState priority(int priority);

  void send() throws ServiceException;
}
