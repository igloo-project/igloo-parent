package org.iglooproject.jpa.security.crypto.password;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

public class CoreShaPasswordEncoder extends AbstractCoreMessageDigestPasswordEncoder {
	
	public CoreShaPasswordEncoder(int strength) {
		super(new ShaPasswordEncoder(strength));
	}

}
