package fr.openwide.core.jpa.security.crypto.password;

import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AbstractCoreMessageDigestPasswordEncoder implements PasswordEncoder {
	
	private MessageDigestPasswordEncoder delegate;
	
	private String salt;
	
	protected AbstractCoreMessageDigestPasswordEncoder(MessageDigestPasswordEncoder delegate) {
		this.delegate = delegate;
	}

	@Override
	public String encode(CharSequence rawPassword) {
		return delegate.encodePassword(rawPassword.toString(), salt);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return delegate.isPasswordValid(encodedPassword, rawPassword.toString(), salt);
	}
	
	public void setSalt(String salt) {
		this.salt = salt;
	}

}
