package fr.openwide.core.spring.notification.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.IDN;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;

import fr.openwide.core.spring.util.StringUtils;

public class NotificationRecipient implements Serializable {
	
	private static final long serialVersionUID = -1021739893358432630L;

	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationRecipient.class);
	
	private static final Function<String, NotificationRecipient> EMAIL_TO_NOTIFICATION_RECIPIENT_FUNCTION = new Function<String, NotificationRecipient>() {
		@Override
		public NotificationRecipient apply(String email) {
			if (!StringUtils.hasText(email)) {
				return null;
			}
			try {
				return NotificationRecipient.of(email);
			} catch (AddressException e) {
				LOGGER.warn(String.format("Ignoring address %1$s", email), e);
				return null;
			}
		}
	};
	
	private InternetAddress address;
	
	private NotificationRecipient(InternetAddress address) {
		this.address = address;
	}

	public InternetAddress getAddress() {
		return address;
	}
	
	public static NotificationRecipient of(String email) throws AddressException {
		return new NotificationRecipient(normalizeEmail(email));
	}
	
	public static List<NotificationRecipient> of(Collection<String> emails) {
		return FluentIterable.from(emails).transform(EMAIL_TO_NOTIFICATION_RECIPIENT_FUNCTION).filter(Predicates.notNull()).toList();
	}
	
	public static NotificationRecipient of(INotificationRecipient recipient, Charset charset) throws AddressException {
		return new NotificationRecipient(getInternetAddress(recipient.getEmail(), recipient.getFullName(), charset));
	}
	
	private static InternetAddress normalizeEmail(String email) throws AddressException {
		return getInternetAddress(email, null, null);
	}
	
	private static InternetAddress getInternetAddress(String email, String fullName, Charset charset) throws AddressException {
		if (!StringUtils.hasText(email)) {
			throw new AddressException("Email address is empty");
		}
		
		List<String> emailParts = Splitter.on('@').omitEmptyStrings().trimResults().splitToList(StringUtils.lowerCase(email));
		if (emailParts.size() != 2) {
			throw new AddressException("Invalid email address", email);
		}
		String idnEmail = emailParts.get(0) + '@' + IDN.toASCII(emailParts.get(1));
		
		try {
			InternetAddress internetAddress = new InternetAddress(idnEmail, fullName, charset != null ? charset.name() : null);
			internetAddress.validate();
			return internetAddress;
		} catch (UnsupportedEncodingException e) {
			throw new AddressException(String.format("Unable to parse the address %1$s <$2$s>: invalid encoding", fullName, idnEmail));
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
		if (!(obj instanceof NotificationRecipient)) {
			return false;
		}
		return address.equals(((NotificationRecipient) obj).getAddress());
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(address).toHashCode();
	}
}