package fr.openwide.hibernate.util;

import java.util.Properties;

import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.id.SequenceGenerator;
import org.hibernate.type.Type;

public class PostgreSQLSequenceGenerator extends SequenceGenerator {

	public static final String SEQUENCE_NAME_SEPARATOR = "_";

	public static final String SEQUENCE_NAME_SUFFIX = "seq";

	@Override
	public void configure(Type type, Properties params, Dialect dialect) throws MappingException {

		String tableName = params.getProperty(PersistentIdentifierGenerator.TABLE);
		String columnName = params.getProperty(PersistentIdentifierGenerator.PK);

		if (tableName != null && columnName != null) {
			StringBuilder sequenceNameBuilder = new StringBuilder();

			sequenceNameBuilder.append(tableName);
			sequenceNameBuilder.append(SEQUENCE_NAME_SEPARATOR);
			sequenceNameBuilder.append(columnName);
			sequenceNameBuilder.append(SEQUENCE_NAME_SEPARATOR);
			sequenceNameBuilder.append(SEQUENCE_NAME_SUFFIX);

			params.setProperty(SEQUENCE, sequenceNameBuilder.toString());
		}

		super.configure(type, params, dialect);
	}

}