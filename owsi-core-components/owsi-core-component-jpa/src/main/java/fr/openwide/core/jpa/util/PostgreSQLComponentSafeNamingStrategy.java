package fr.openwide.core.jpa.util;

import org.hibernate.cfg.NamingStrategy;

/**
 * @see TruncatingNamingStrategyWrapper
 */
public class PostgreSQLComponentSafeNamingStrategy extends TruncatingNamingStrategyWrapper {
	public static final NamingStrategy INSTANCE = new PostgreSQLComponentSafeNamingStrategy();

	public PostgreSQLComponentSafeNamingStrategy() {
		super(FixedDefaultComponentSafeNamingStrategy.INSTANCE, 63);
	}

}
