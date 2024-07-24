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

package org.iglooproject.test.business.person.service;

import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.business.person.dao.IPersonDao;
import org.iglooproject.test.business.person.model.Person;
import org.iglooproject.test.business.person.model.QPerson;
import org.iglooproject.test.business.project.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("testPersonService")
public class PersonServiceImpl extends GenericEntityServiceImpl<Long, Person>
    implements IPersonService {

  @Autowired private IPersonDao personDao;

  @Autowired
  public PersonServiceImpl(IPersonDao personDao) {
    super(personDao);
  }

  @Override
  public Long countByLastName(String value) {
    return personDao.countByField(QPerson.person, QPerson.person.lastName, value);
  }

  @Override
  public void addProject(Person person, Project project)
      throws ServiceException, SecurityServiceException {
    person.addWorkedProjects(project);
    update(person);
  }
}
