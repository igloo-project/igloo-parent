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

package org.iglooproject.test;

import org.iglooproject.config.bootstrap.spring.ExtendedTestApplicationContextInitializer;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.business.company.model.Company;
import org.iglooproject.test.business.company.service.ICompanyService;
import org.iglooproject.test.business.label.service.ILabelService;
import org.iglooproject.test.business.person.model.Person;
import org.iglooproject.test.business.person.service.IPersonReferenceService;
import org.iglooproject.test.business.person.service.IPersonService;
import org.iglooproject.test.business.project.model.Project;
import org.iglooproject.test.business.project.service.IProjectService;
import org.iglooproject.test.jpa.junit.AbstractTestCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {}, initializers = ExtendedTestApplicationContextInitializer.class)
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
