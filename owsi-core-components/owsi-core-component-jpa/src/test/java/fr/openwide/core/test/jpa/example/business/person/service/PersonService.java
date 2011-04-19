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

package fr.openwide.core.test.jpa.example.business.person.service;

import javax.persistence.metamodel.SingularAttribute;

import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.test.jpa.example.business.person.model.Person;

public interface PersonService extends IGenericEntityService<Integer, Person> {

	Long count(SingularAttribute<Person, String> attribute, String value);
}
