package fr.openwide.hibernate.test.business.person.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.hibernate.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.hibernate.exception.SecurityServiceException;
import fr.openwide.hibernate.exception.ServiceException;
import fr.openwide.hibernate.test.business.person.dao.PersonDao;
import fr.openwide.hibernate.test.business.person.model.Person;
import fr.openwide.hibernate.test.business.project.model.Project;

@Service("personService")
public class PersonServiceImpl extends GenericEntityServiceImpl<Person> implements PersonService {

	@Autowired
	public PersonServiceImpl(PersonDao personDao) {
		super(personDao);
	}

	@Override
	public void addProject(Person person, Project project) throws ServiceException, SecurityServiceException {
		person.addWorkedProjects(project);
		update(person);
	}
}
