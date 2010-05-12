package fr.openwide.hibernate.example.business.person.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.hibernate.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.hibernate.exception.SecurityServiceException;
import fr.openwide.core.hibernate.exception.ServiceException;
import fr.openwide.hibernate.example.business.person.dao.PersonDao;
import fr.openwide.hibernate.example.business.person.model.Person;
import fr.openwide.hibernate.example.business.project.model.Project;

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
