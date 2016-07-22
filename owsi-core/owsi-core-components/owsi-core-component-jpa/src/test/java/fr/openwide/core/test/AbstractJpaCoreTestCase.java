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
import fr.openwide.core.test.business.company.model.Company;
import fr.openwide.core.test.business.company.service.ICompanyService;
import fr.openwide.core.test.business.label.service.ILabelService;
import fr.openwide.core.test.business.person.model.Person;
import fr.openwide.core.test.business.person.service.IPersonReferenceService;
import fr.openwide.core.test.business.person.service.IPersonService;
import fr.openwide.core.test.business.project.model.Project;
import fr.openwide.core.test.business.project.service.IProjectService;
import fr.openwide.core.test.config.spring.JpaTestConfig;

@ContextConfiguration(classes = JpaTestConfig.class)
public abstract class AbstractJpaCoreTestCase extends AbstractTestCase {
	
	@Autowired
	protected ICompanyService companyService;
	
	@Autowired
	protected IProjectService projectService;
	
	@Autowired
	protected IPersonService personService;

	@Autowired
	protected IPersonReferenceService personReferenceService;
	
	@Autowired
	protected ILabelService labelService;

	@Override
	protected void cleanAll() throws ServiceException, SecurityServiceException {
		cleanEntities(projectService);
		cleanEntities(companyService);
		cleanEntities(personReferenceService);
		cleanEntities(personService);
		cleanEntities(labelService);
	}
	
	protected Company createCompany(String name) throws ServiceException, SecurityServiceException {
		Company company = new Company(name);

		companyService.create(company);
		
		return company;
	}
	
	protected Person createPerson(String firstName, String lastName) throws ServiceException, SecurityServiceException {
		Person person = new Person(firstName, lastName);
		
		personService.create(person);
		
		return person;
	}
	
	protected Project createProject(String name) throws ServiceException, SecurityServiceException {
		Project project = new Project(name);
		
		projectService.create(project);
		
		return project;
	}

}
