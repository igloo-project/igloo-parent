package fr.openwide.hibernate.test.business.person.service;

import fr.openwide.hibernate.business.generic.service.GenericEntityService;
import fr.openwide.hibernate.exception.SecurityServiceException;
import fr.openwide.hibernate.exception.ServiceException;
import fr.openwide.hibernate.test.business.person.model.Person;
import fr.openwide.hibernate.test.business.project.model.Project;

public interface PersonService extends GenericEntityService<Person> {
	
	void addProject(Person person, Project project) throws ServiceException, SecurityServiceException;
}
