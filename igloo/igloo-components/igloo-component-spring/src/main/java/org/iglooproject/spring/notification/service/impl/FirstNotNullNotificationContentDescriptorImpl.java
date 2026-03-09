package org.iglooproject.spring.notification.service.impl;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
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
    if (obj instanceof FirstNotNullNotificationContentDescriptorImpl) {
      if (obj == this) {
        return true;
      }
      FirstNotNullNotificationContentDescriptorImpl other =
          (FirstNotNullNotificationContentDescriptorImpl) obj;
      return new EqualsBuilder().append(chainedDescriptors, other.chainedDescriptors).build();
    }
    return false;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(chainedDescriptors).build();
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
