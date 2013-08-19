package fr.openwide.core.jpa.security.crypto.password;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CoreShaPasswordEncoder extends ShaPasswordEncoder implements PasswordEncoder {
	
	private String salt;

	public CoreShaPasswordEncoder(int strength) {
		super(strength);
	}

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
