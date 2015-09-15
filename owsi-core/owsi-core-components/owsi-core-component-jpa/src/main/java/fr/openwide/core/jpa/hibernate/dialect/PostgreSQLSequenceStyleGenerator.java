package fr.openwide.core.jpa.hibernate.dialect;

import java.util.Properties;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.relational.QualifiedName;
import org.hibernate.boot.model.relational.QualifiedNameParser;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;

public class PostgreSQLSequenceStyleGenerator extends SequenceStyleGenerator {
	
	/**
	 * Séparateur utilisé entre les différentes parties du nom d'une séquence.
	 */
	public static final String SEQUENCE_NAME_SEPARATOR = "_";

	/**
	 * Suffixe ajouté à la fin du nom de la séquence.
	 */
	public static final String SEQUENCE_NAME_SUFFIX = "seq";
	
	@Override
	protected QualifiedName determineSequenceName(Properties params, Dialect dialect, JdbcEnvironment jdbcEnv) {
		String tableName = params.getProperty(PersistentIdentifierGenerator.TABLE);
		String columnName = params.getProperty(PersistentIdentifierGenerator.PK);

		if (tableName != null && columnName != null) {
			StringBuilder sequenceNameBuilder = new StringBuilder();

			sequenceNameBuilder.append(tableName);
			sequenceNameBuilder.append(SEQUENCE_NAME_SEPARATOR);
			sequenceNameBuilder.append(columnName);
			sequenceNameBuilder.append(SEQUENCE_NAME_SEPARATOR);
			sequenceNameBuilder.append(SEQUENCE_NAME_SUFFIX);
			
			// todo : need to incorporate implicit catalog and schema names
			final Identifier catalog = jdbcEnv.getIdentifierHelper().toIdentifier(
					ConfigurationHelper.getString( CATALOG, params )
			);
			final Identifier schema =  jdbcEnv.getIdentifierHelper().toIdentifier(
					ConfigurationHelper.getString( SCHEMA, params )
			);
			return new QualifiedNameParser.NameParts(
					catalog,
					schema,
					jdbcEnv.getIdentifierHelper().toIdentifier( sequenceNameBuilder.toString() )
			);
		}
		throw new IllegalStateException("Unable to build the sequence name");
	}

}
