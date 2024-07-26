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

package org.igloo.hibernate.dialect;

import org.hibernate.dialect.PostgreSQL10Dialect;
import org.hibernate.jpa.AvailableSettings;
import org.igloo.hibernate.function.PostgreSQLIntervalFunction;
import org.igloo.hibernate.function.PostgreSQLRegexpOperatorFunction;

/**
 * Register custom functions to allow advanced queryDSL queries.
 *
 * <p>Previously (<= 0.13): register custom SequenceStyleGenerator; now configured with {@link
 * AvailableSettings#IDENTIFIER_GENERATOR_STRATEGY_PROVIDER}
 *
 * @author Open Wide
 */
public class PostgreSQLAdvancedDialect
    extends PostgreSQL10Dialect { // NOSONAR ignore too many parents (10)

  public PostgreSQLAdvancedDialect() {
    registerFunction("interval", new PostgreSQLIntervalFunction());
    registerFunction("regexp_operator", new PostgreSQLRegexpOperatorFunction());
  }
}
