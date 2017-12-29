package org.iglooproject.jpa.security.crypto.password;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

public class CoreMd5PasswordEncoder extends AbstractCoreMessageDigestPasswordEncoder {

	public CoreMd5PasswordEncoder() {
		super(new Md5PasswordEncoder());
	}

}
