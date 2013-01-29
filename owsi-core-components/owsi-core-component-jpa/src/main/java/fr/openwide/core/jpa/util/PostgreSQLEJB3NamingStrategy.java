package fr.openwide.core.jpa.util;

import org.hibernate.cfg.EJB3NamingStrategy;
import org.hibernate.cfg.NamingStrategy;

/**
 * @see TruncatingNamingStrategyWrapper
 */
public class PostgreSQLEJB3NamingStrategy extends TruncatingNamingStrategyWrapper {
	public static final NamingStrategy INSTANCE = new PostgreSQLEJB3NamingStrategy();

	public PostgreSQLEJB3NamingStrategy() {
		super(EJB3NamingStrategy.INSTANCE, 63);
	}

}
