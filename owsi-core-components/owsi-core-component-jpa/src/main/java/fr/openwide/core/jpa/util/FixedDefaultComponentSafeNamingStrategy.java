package fr.openwide.core.jpa.util;

import org.hibernate.cfg.DefaultComponentSafeNamingStrategy;

/**
 * Fixes bug <a href="https://hibernate.onjira.com/browse/HHH-6005">HHH-6005</a> in {@link DefaultComponentSafeNamingStrategy}.
 */
public class FixedDefaultComponentSafeNamingStrategy extends DefaultComponentSafeNamingStrategy {

	private static final long serialVersionUID = -2382165043375385406L;

	@Override
	public String propertyToColumnName(String propertyName) {
		return super.propertyToColumnName(propertyName.replace(".collection&&element.", "."));
	}

}
