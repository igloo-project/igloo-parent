package fr.openwide.jpa.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.junit.AbstractTestCase;
import fr.openwide.core.spring.config.CoreConfigurer;
import fr.openwide.jpa.example.business.company.model.Company;
import fr.openwide.jpa.example.business.company.service.CompanyService;
import fr.openwide.jpa.example.business.person.model.Person;
import fr.openwide.jpa.example.business.person.service.PersonService;
import fr.openwide.jpa.example.business.project.model.Project;
import fr.openwide.jpa.example.business.project.service.ProjectService;
import fr.openwide.jpa.example.config.spring.CoreJpaExampleConfig;

@ContextConfiguration(classes = { CoreJpaExampleConfig.class })
public abstract class AbstractJpaTestCase extends AbstractTestCase {
	
	@Autowired
	protected CoreConfigurer configurer;
	
	@Autowired
	protected CompanyService companyService;
	
	@Autowired
	protected PersonService personService;
	
	@Autowired
	protected ProjectService projectService;
	
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
	
	protected void cleanCompanies() throws ServiceException, SecurityServiceException {
		List<Company> companies = companyService.list();
		for (Company company : companies) {
			companyService.delete(company);
		}
	}
	
	protected void cleanPersons() throws ServiceException, SecurityServiceException {
		List<Person> persons = personService.list();
		for (Person person : persons) {
			personService.delete(person);
		}
	}
	
	protected void cleanProjects() throws ServiceException, SecurityServiceException {
		List<Project> projects = projectService.list();
		for (Project project : projects) {
			projectService.delete(project);
		}
	}

	@Override
	protected void cleanAll() throws ServiceException, SecurityServiceException {
		cleanProjects();
		cleanCompanies();
		cleanPersons();
	}
}
