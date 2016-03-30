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

package fr.openwide.core.test.business.person.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.test.business.person.dao.IPersonDao;
import fr.openwide.core.test.business.person.model.Person;
import fr.openwide.core.test.business.person.model.QPerson;
import fr.openwide.core.test.business.project.model.Project;

@Service("testPersonService")
public class PersonServiceImpl extends GenericEntityServiceImpl<Long, Person>
		implements IPersonService {
	
	@Autowired
	private IPersonDao personDao;
	
	@Autowired
	public PersonServiceImpl(IPersonDao personDao) {
		super(personDao);
	}
	
	@Override
	public Long countByLastName(String value) {
		return personDao.countByField(QPerson.person, QPerson.person.lastName, value);
	}

	@Override
	public void addProject(Person person, Project project) throws ServiceException, SecurityServiceException {
		person.addWorkedProjects(project);
		update(person);
	}

}
