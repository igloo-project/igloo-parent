package fr.openwide.hibernate.test;

import java.util.List;

import javax.sql.DataSource;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import fr.openwide.commons.spring.OwPropertyPlaceholderConfigurer;
import fr.openwide.hibernate.example.business.company.model.Company;
import fr.openwide.hibernate.example.business.company.service.CompanyService;
import fr.openwide.hibernate.example.business.person.model.Person;
import fr.openwide.hibernate.example.business.person.service.PersonService;
import fr.openwide.hibernate.example.business.project.model.Project;
import fr.openwide.hibernate.example.business.project.service.ProjectService;
import fr.openwide.hibernate.exception.SecurityServiceException;
import fr.openwide.hibernate.exception.ServiceException;
import fr.openwide.hibernate.util.hibernate.HibernateSessionUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@ContextConfiguration(locations = {
		"classpath:database-initialization-context.xml",
		"classpath:application-context.xml"
})
public abstract class AbstractHibernateTestCase extends AbstractJUnit38SpringContextTests {
	
	@Autowired
	OwPropertyPlaceholderConfigurer configurer;
	
	@Autowired
	protected HibernateSessionUtils hibernateSessionUtils;
	
	@Autowired
	protected CompanyService companyService;
	
	@Autowired
	protected PersonService personService;
	
	@Autowired
	protected ProjectService projectService;
	
	protected DataSource hibernateDataSource;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.hibernateDataSource = dataSource;
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
	
	protected void cleanCompanies() {
		List<Company> companies = companyService.list();
		for (Company company : companies) {
			companyService.delete(company);
		}
	}
	
	protected void cleanPersons() {
		List<Person> persons = personService.list();
		for (Person person : persons) {
			personService.delete(person);
		}
	}
	
	protected void cleanProjects() {
		List<Project> projects = projectService.list();
		for (Project project : projects) {
			projectService.delete(project);
		}
	}

	protected void cleanAll() {
		cleanProjects();
		cleanCompanies();
		cleanPersons();
	}
	
	public void init() throws ServiceException, SecurityServiceException {
		hibernateSessionUtils.initSession();
		
		cleanAll();
	}
	
	public void close() {
		cleanAll();
		
		hibernateSessionUtils.closeSession();
	}
}
