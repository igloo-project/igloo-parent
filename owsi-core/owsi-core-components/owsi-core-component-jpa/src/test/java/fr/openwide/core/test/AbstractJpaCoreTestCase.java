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

package fr.openwide.core.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.junit.AbstractTestCase;
import fr.openwide.core.test.config.spring.JpaTestConfig;
import fr.openwide.core.test.jpa.example.business.label.service.LabelService;
import fr.openwide.core.test.jpa.example.business.person.service.PersonReferenceService;
import fr.openwide.core.test.jpa.example.business.person.service.PersonService;

@ContextConfiguration(classes = JpaTestConfig.class)
public abstract class AbstractJpaCoreTestCase extends AbstractTestCase {
	
	@Autowired
	protected PersonService personService;

	@Autowired
	protected PersonReferenceService personReferenceService;
	
	@Autowired
	protected LabelService labelService;

	@Override
	protected void cleanAll() throws ServiceException, SecurityServiceException {
		cleanEntities(personReferenceService);
		cleanEntities(personService);
		cleanEntities(labelService);
	}
}
