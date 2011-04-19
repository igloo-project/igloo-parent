/*
 * Copyright (C) 2009-2010 Open Wide
 * Contact: contact@openwide.fr
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.openwide.core.hibernate.util;

import java.util.Properties;

import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.id.SequenceGenerator;
import org.hibernate.type.Type;

/**
 * <p>Construit le nom de la séquence PostgreSQL en suivant les conventions de nommage des séquences pour le type
 * serial.</p>
 * 
 * @author Open Wide
 */
public class PostgreSQLSequenceGenerator extends SequenceGenerator {

	/**
	 * Séparateur utilisé entre les différentes parties du nom d'une séquence.
	 */
	public static final String SEQUENCE_NAME_SEPARATOR = "_";

	/**
	 * Suffixe ajouté à la fin du nom de la séquence.
	 */
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