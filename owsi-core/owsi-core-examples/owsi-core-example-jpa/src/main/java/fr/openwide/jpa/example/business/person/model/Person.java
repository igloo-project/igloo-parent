package fr.openwide.hibernate.example.business.person.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import fr.openwide.core.hibernate.business.generic.model.GenericEntity;
import fr.openwide.core.spring.util.StringUtils;
import fr.openwide.hibernate.example.business.company.model.Company;
import fr.openwide.hibernate.example.business.project.model.Project;

@Entity
public class Person extends GenericEntity<Integer, Person> {

	private static final long serialVersionUID = -2471930493134125282L;

	@Id
	@GeneratedValue
	private Integer id;

	private String firstName;

	private String lastName;

	@ManyToOne
	private Company company;

	@ManyToMany
	@JoinTable(joinColumns=@JoinColumn(name="persons_id"), inverseJoinColumns=@JoinColumn(name="project_id"))
	private List<Project> workedProjects = new LinkedList<Project>();

	public Person() {
	}

	public Person(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public List<Project> getWorkedProjects() {
		return workedProjects;
	}

	public void setWorkedProjects(List<Project> workedProjects) {
		this.workedProjects = workedProjects;
	}
	
	public void addWorkedProjects(Project project) {
		this.workedProjects.add(project);
		project.getTeam().add(this);
	}
	
	public void removeWorkedProjects(Project project) {
		this.workedProjects.remove(project);
		project.getTeam().remove(this);
	}

	@Override
	public int compareTo(Person person) {
		if(this.equals(person)) {
			return 0;
		}
		if(StringUtils.removeAccents(this.getLastName()).equalsIgnoreCase(StringUtils.removeAccents(person.getLastName()))) {
			return StringUtils.removeAccents(this.getFirstName()).compareToIgnoreCase(StringUtils.removeAccents(person.getFirstName()));
		}
		return StringUtils.removeAccents(this.getLastName()).compareToIgnoreCase(StringUtils.removeAccents(person.getLastName()));
	}

	@Override
	public String getNameForToString() {
		return getDisplayName();
	}

	@Override
	public String getDisplayName() {
		return getLastName() + " " + getFirstName();
	}
}
