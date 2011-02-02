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

package fr.openwide.core.jpa.util;

import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.id.IdentifierGenerator;

/**
 * <p>Surcharge du dialecte PostgreSQL qui permet de gérer les séquences par table plutôt que d'avoir une seule séquence
 * globale pour tous les objets.</p>
 * 
 * <p>Les séquences sont gérées via des types serial et ce dialect suit donc les conventions de nommage de séquence de
 * PostgreSQL.</p>
 * 
 * <p>Hibernate est assez peu finaud sur la gestion des séquences PostgreSQL.</p>
 * 
 * @author Open Wide
 */
public class PostgreSQLSerialDialect extends PostgreSQLDialect {

	@Override
	public Class<? extends IdentifierGenerator> getNativeIdentifierGeneratorClass() {
		return PostgreSQLSequenceGenerator.class;
	}

}
