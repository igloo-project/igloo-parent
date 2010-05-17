package fr.openwide.hibernate.example.business.person.service;

import fr.openwide.core.hibernate.business.generic.service.GenericEntityService;
import fr.openwide.core.hibernate.exception.SecurityServiceException;
import fr.openwide.core.hibernate.exception.ServiceException;
import fr.openwide.hibernate.example.business.person.model.Person;
import fr.openwide.hibernate.example.business.project.model.Project;

public interface PersonService extends GenericEntityService<Person> {
	
	void addProject(Person person, Project project) throws ServiceException, SecurityServiceException;
}
