package fr.openwide.hibernate.example.business.person.service;

import fr.openwide.hibernate.business.generic.service.GenericEntityService;
import fr.openwide.hibernate.example.business.person.model.Person;
import fr.openwide.hibernate.example.business.project.model.Project;
import fr.openwide.hibernate.exception.SecurityServiceException;
import fr.openwide.hibernate.exception.ServiceException;

public interface PersonService extends GenericEntityService<Person> {
	
	void addProject(Person person, Project project) throws ServiceException, SecurityServiceException;
}
