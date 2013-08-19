package fr.openwide.core.jpa.security.crypto.password;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CoreMd5PasswordEncoder extends Md5PasswordEncoder implements PasswordEncoder {
	
	private String salt;
	
	@Override
	public String encode(CharSequence rawPassword) {
		return encodePassword(rawPassword.toString(), salt);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return isPasswordValid(encodedPassword, rawPassword.toString(), salt);
	}
	
	public void setSalt(String salt) {
		this.salt = salt;
	}

}
