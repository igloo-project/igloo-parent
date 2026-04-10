package org.iglooproject.spring.notification.service.impl;

import com.google.common.collect.ImmutableList;
import java.util.Objects;
import org.apache.commons.lang3.mutable.MutableObject;
import org.iglooproject.mail.api.INotificationRecipient;
import org.iglooproject.spring.notification.exception.NotificationContentRenderingException;
import org.iglooproject.spring.notification.model.INotificationContentBody;
import org.iglooproject.spring.notification.model.INotificationContentDescriptor;
import org.iglooproject.spring.notification.model.NotificationContentBody;

public class FirstNotNullNotificationContentDescriptorImpl
    implements INotificationContentDescriptor {

  private final Iterable<? extends INotificationContentDescriptor> chainedDescriptors;

  public FirstNotNullNotificationContentDescriptorImpl(
      Iterable<? extends INotificationContentDescriptor> chainedDescriptors) {
    super();
    this.chainedDescriptors = ImmutableList.copyOf(chainedDescriptors);
  }

  @Override
  public String renderSubject() throws NotificationContentRenderingException {
    for (INotificationContentDescriptor descriptor : chainedDescriptors) {
      String subject = descriptor.renderSubject();
      if (subject != null) {
        return subject;
      }
    }
    return null;
  }

  @Override
  public INotificationContentBody renderBody() throws NotificationContentRenderingException {
    MutableObject<String> plainText = new MutableObject<>();
    MutableObject<String> htmlText = new MutableObject<>();

    for (INotificationContentDescriptor descriptor : chainedDescriptors) {
      INotificationContentBody body = descriptor.renderBody();

      if (body != null) {
        if (plainText.get() == null) {
          plainText.setValue(body.getPlainText());
        }

        if (htmlText.get() == null) {
          htmlText.setValue(body.getHtmlText());
        }

        if (plainText.get() != null && htmlText.get() != null) {
          break;
        }
      }
    }

    return NotificationContentBody.start()
        .with(
            o -> {
              o.setPlainText(plainText.get());
              o.setHtmlText(htmlText.get());
            })
        .build();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof FirstNotNullNotificationContentDescriptorImpl other)) {
      return false;
    }
    return Objects.equals(chainedDescriptors, other.chainedDescriptors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(chainedDescriptors);
  }

  @Override
  public INotificationContentDescriptor withContext(INotificationRecipient recipient) {
    ImmutableList.Builder<INotificationContentDescriptor> wrapped = ImmutableList.builder();

    for (INotificationContentDescriptor current : chainedDescriptors) {
      wrapped.add(current.withContext(recipient));
    }

    return new FirstNotNullNotificationContentDescriptorImpl(wrapped.build());
  }
}
