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

package org.iglooproject.jpa.hibernate.dialect;

import org.hibernate.jpa.AvailableSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Register custom functions to allow advanced queryDSL queries.
 *
 * <p>Previously (<= 0.13): register custom SequenceStyleGenerator; now configured with {@link
 * AvailableSettings#IDENTIFIER_GENERATOR_STRATEGY_PROVIDER}
 *
 * @author Open Wide
 * @deprecated please switch to {@link org.igloo.hibernate.dialect.PostgreSQLAdvancedDialect}
 */
@Deprecated(since = "3.6.0")
public class PostgreSQLAdvancedDialect
    extends org.igloo.hibernate.dialect
        .PostgreSQLAdvancedDialect { // NOSONAR ignore naming and too many parents

  private static final Logger LOGGER = LoggerFactory.getLogger(PostgreSQLAdvancedDialect.class);

  public PostgreSQLAdvancedDialect() {
    super();
    LOGGER.warn(
        "{} deprecated, please switch to {}",
        PostgreSQLAdvancedDialect.class.getName(),
        org.igloo.hibernate.dialect.PostgreSQLAdvancedDialect.class.getName());
  }
}
