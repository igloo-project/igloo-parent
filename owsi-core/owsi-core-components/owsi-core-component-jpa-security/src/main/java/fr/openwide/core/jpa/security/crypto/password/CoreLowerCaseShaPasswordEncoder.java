package fr.openwide.core.jpa.security.crypto.password;

import fr.openwide.core.spring.util.StringUtils;

public class CoreLowerCaseShaPasswordEncoder extends CoreShaPasswordEncoder {

	public CoreLowerCaseShaPasswordEncoder(int strength) {
		super(strength);
	}
	
	@Override
	public String encode(CharSequence rawPassword) {
		return super.encode(StringUtils.lowerCase(rawPassword.toString()));
	}

}
