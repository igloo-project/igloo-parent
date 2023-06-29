package org.iglooproject.jpa.hibernate.jpa;

import java.util.Map;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.relational.QualifiedName;
import org.hibernate.boot.model.relational.QualifiedNameParser;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.id.enhanced.StandardNamingStrategy;
import org.hibernate.service.ServiceRegistry;

public class SequenceOverrideImplicitNamingStrategy extends StandardNamingStrategy {

	/**
	 * Séparateur utilisé entre les différentes parties du nom d'une séquence.
	 */
	public static final String SEQUENCE_NAME_SEPARATOR = "_";

	/**
	 * Suffixe ajouté à la fin du nom de la séquence.
	 */
	public static final String SEQUENCE_NAME_SUFFIX = "seq";

	@Override
	public QualifiedName determineSequenceName(Identifier catalogName, Identifier schemaName, Map<?, ?> configValues, ServiceRegistry serviceRegistry) {
		String tableName = (String) configValues.get(PersistentIdentifierGenerator.TABLE);
		String columnName = (String) configValues.get(PersistentIdentifierGenerator.PK);

		if (tableName != null && columnName != null) {
			StringBuilder sequenceNameBuilder = new StringBuilder();

			sequenceNameBuilder.append(tableName);
			sequenceNameBuilder.append(SEQUENCE_NAME_SEPARATOR);
			sequenceNameBuilder.append(columnName);
			sequenceNameBuilder.append(SEQUENCE_NAME_SEPARATOR);
			sequenceNameBuilder.append(SEQUENCE_NAME_SUFFIX);
			
			return new QualifiedNameParser.NameParts(
					catalogName,
					schemaName,
					serviceRegistry.getService(JdbcEnvironment.class).getIdentifierHelper().toIdentifier( sequenceNameBuilder.toString() )
			);
		}
		throw new IllegalStateException("Unable to build the sequence name");
	}

}
