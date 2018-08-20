package org.iglooproject.jpa.hibernate.model.naming;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class PostgreSQLPhysicalNamingStrategyImpl implements PhysicalNamingStrategy {

	private static final int IDENTIFIER_MAX_LENGTH = 63;

	@Override
	public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment jdbcEnvironment) {
		return truncateIdentifier(name);
	}

	@Override
	public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment jdbcEnvironment) {
		return truncateIdentifier(name);
	}

	@Override
	public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
		return truncateIdentifier(name);
	}

	@Override
	public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment jdbcEnvironment) {
		return truncateIdentifier(name);
	}

	@Override
	public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment jdbcEnvironment) {
		return truncateIdentifier(name);
	}

	protected Identifier truncateIdentifier(Identifier name) {
		if (name == null) {
			return name;
		}
		
		String text = name.getText();
		if (text == null || text.length() <= IDENTIFIER_MAX_LENGTH) {
			return name;
		} else {
			return Identifier.toIdentifier(text.substring(0, IDENTIFIER_MAX_LENGTH), name.isQuoted());
		}
	}

}
