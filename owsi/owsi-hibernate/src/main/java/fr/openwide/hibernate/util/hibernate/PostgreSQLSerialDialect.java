package fr.openwide.hibernate.util.hibernate;

import org.hibernate.dialect.PostgreSQLDialect;

public class PostgreSQLSerialDialect extends PostgreSQLDialect {

	@SuppressWarnings("unchecked")
	@Override
	public Class getNativeIdentifierGeneratorClass() {
		return PostgreSQLSequenceGenerator.class;
	}

}
