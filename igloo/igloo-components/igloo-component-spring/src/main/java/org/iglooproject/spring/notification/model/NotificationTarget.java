package org.iglooproject.spring.notification.model;

import com.google.common.base.Splitter;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.IDN;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.iglooproject.mail.api.INotificationRecipient;
import org.iglooproject.spring.notification.exception.InvalidNotificationTargetException;
import org.iglooproject.spring.util.StringUtils;

public class NotificationTarget implements Serializable {

  private static final long serialVersionUID = -1021739893358432630L;

  private final InternetAddress address;

  private NotificationTarget(InternetAddress address) {
    this.address = address;
  }

  public InternetAddress getAddress() {
    return address;
  }

  public static NotificationTarget of(String email) throws InvalidNotificationTargetException {
    return new NotificationTarget(getInternetAddress(email, null, null));
  }

  /**
   * Accept either a full email (Label <address@domain>) or an email only (address@domain) address.
   * Must be compatible with java {@link InternetAddress} parsing.
   */
  public static NotificationTarget ofInternetAddress(String email)
      throws InvalidNotificationTargetException {
    try {
      return new NotificationTarget(new InternetAddress(email));
    } catch (AddressException e) {
      throw new InvalidNotificationTargetException(
          String.format("Error parsing address: %s", email), e);
    }
  }

  public static NotificationTarget of(INotificationRecipient recipient, Charset charset)
      throws InvalidNotificationTargetException {
    return new NotificationTarget(
        getInternetAddress(recipient.getEmail(), recipient.getFullName(), charset));
  }

  private static InternetAddress getInternetAddress(String email, String fullName, Charset charset)
      throws InvalidNotificationTargetException {
    try {
      if (!StringUtils.hasText(email)) {
        throw new AddressException("Email address is empty");
      }

      List<String> emailParts =
          Splitter.on('@')
              .omitEmptyStrings()
              .trimResults()
              .splitToList(StringUtils.lowerCase(email));
      if (emailParts.size() != 2) {
        throw new AddressException("Invalid email address", email);
      }
      String idnEmail = emailParts.get(0) + '@' + IDN.toASCII(emailParts.get(1));

      try {
        InternetAddress internetAddress =
            new InternetAddress(idnEmail, fullName, charset != null ? charset.name() : null);
        internetAddress.validate();
        return internetAddress;
      } catch (UnsupportedEncodingException e) {
        throw new AddressException(
            String.format(
                "Unable to parse the address %1$s <$2$s>: invalid encoding", fullName, idnEmail));
      }
    } catch (AddressException e) {
      throw new InvalidNotificationTargetException(
          String.format("Invalid notification target: %s / %s / %s", email, fullName, charset), e);
    }
  }

  @Override
  public String toString() {
    if (address == null) {
      return "<null>";
    }
    StringBuilder sb = new StringBuilder();
    if (StringUtils.hasText(address.getPersonal())) {
      sb.append(address.getPersonal()).append(" ");
    }
    sb.append("<").append(address.getAddress()).append(">");
    return sb.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof NotificationTarget)) {
      return false;
    }
    return address.equals(((NotificationTarget) obj).getAddress());
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(address).toHashCode();
  }
}
