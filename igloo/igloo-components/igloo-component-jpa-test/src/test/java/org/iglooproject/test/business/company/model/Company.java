package org.iglooproject.test.business.company.model;

import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.test.business.person.model.Person;
import org.iglooproject.test.business.project.model.Project;

@Entity
@Cacheable(true)
public class Company extends GenericEntity<Long, Company> {

	private static final long serialVersionUID = -2003394192412589142L;

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@OneToMany(mappedBy = "company")
	private List<Person> directors = new LinkedList<Person>();
	
	@OneToMany
	@JoinTable(name="company_employees", joinColumns=@JoinColumn(name="company_id"),
			inverseJoinColumns=@JoinColumn(name="employees_id"))
	private List<Person> employees = new LinkedList<Person>();
	
	@OneToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name="company_employees1", joinColumns=@JoinColumn(name="company_id"),
			inverseJoinColumns=@JoinColumn(name="employees1_id"))
	private List<Person> employees1 = new LinkedList<Person>();
	
	@OneToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name="company_employees2", joinColumns=@JoinColumn(name="company_id"),
			inverseJoinColumns=@JoinColumn(name="employees2_id"))
	private List<Person> employees2 = new LinkedList<Person>();
	
	@OneToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name="company_employees3", joinColumns=@JoinColumn(name="company_id"),
			inverseJoinColumns=@JoinColumn(name="employees3_id"))
	private List<Person> employees3 = new LinkedList<Person>();

	@OneToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name="company_employees4", joinColumns=@JoinColumn(name="company_id"),
			inverseJoinColumns=@JoinColumn(name="employees4_id"))
	private List<Person> employees4 = new LinkedList<Person>();
	
	/**
	 * {@link CascadeType#DELETE_ORPHAN} a été remplacé par {@link OneToMany#orphanRemoval()} ou
	 * {@link OneToOne#orphanRemoval()}
	 */
	@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
	@JoinTable(name="company_employees5", joinColumns=@JoinColumn(name="company_id"),
			inverseJoinColumns=@JoinColumn(name="employees5_id"))
	private List<Person> employees5 = new LinkedList<Person>();

	@OneToMany(cascade = CascadeType.MERGE)
	@JoinTable(name="company_employees6", joinColumns=@JoinColumn(name="company_id"),
			inverseJoinColumns=@JoinColumn(name="employees6_id"))
	private List<Person> employees6 = new LinkedList<Person>();
	
	@OneToMany(mappedBy = "company")
	private List<Project> projects = new LinkedList<Project>();
	
	public Company() {
	}

	public Company(String name) {
		this.name = name;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Person> getDirectors() {
		return directors;
	}

	public void setDirectors(List<Person> directors) {
		this.directors = directors;
	}

	public void addDirector(Person person) {
		this.directors.add(person);
		person.setCompany(this);
	}
	
	public void removeDirector(Person person) {
		this.directors.remove(person);
		person.setCompany(null);
	}
	
	public List<Person> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Person> employees) {
		this.employees = employees;
	}
	
	public void addEmployee(Person person) {
		this.employees.add(person);
	}
	
	public void removeEmployee(Person person) {
		this.employees.remove(person);
	}
	
	public List<Person> getEmployees1() {
		return employees1;
	}

	public void setEmployees1(List<Person> employees) {
		this.employees1 = employees;
	}
	
	public void addEmployee1(Person person) {
		this.employees1.add(person);
	}
	
	public void removeEmployee1(Person person) {
		this.employees1.remove(person);
	}
	
	public List<Person> getEmployees2() {
		return employees2;
	}

	public void setEmployees2(List<Person> employees) {
		this.employees2 = employees;
	}
	
	public void addEmployee2(Person person) {
		this.employees2.add(person);
	}
	
	public void removeEmployee2(Person person) {
		this.employees2.remove(person);
	}
	
	public List<Person> getEmployees3() {
		return employees3;
	}

	public void setEmployees3(List<Person> employees) {
		this.employees3 = employees;
	}
	
	public void addEmployee3(Person person) {
		this.employees3.add(person);
	}
	
	public void removeEmployee3(Person person) {
		this.employees3.remove(person);
	}
	
	public List<Person> getEmployees4() {
		return employees4;
	}

	public void setEmployees4(List<Person> employees) {
		this.employees4 = employees;
	}
	
	public void addEmployee4(Person person) {
		this.employees4.add(person);
	}
	
	public void removeEmployee4(Person person) {
		this.employees4.remove(person);
	}
	
	public List<Person> getEmployees5() {
		return employees5;
	}

	public void setEmployees5(List<Person> employees) {
		this.employees5 = employees;
	}
	
	public void addEmployee5(Person person) {
		this.employees5.add(person);
	}
	
	public void removeEmployee5(Person person) {
		this.employees5.remove(person);
	}
	
	public List<Person> getEmployees6() {
		return employees6;
	}

	public void setEmployees6(List<Person> employees) {
		this.employees6 = employees;
	}
	
	public void addEmployee6(Person person) {
		this.employees6.add(person);
	}
	
	public void removeEmployee6(Person person) {
		this.employees6.remove(person);
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public void addProject(Project project) {
		this.projects.add(project);
		project.setCompany(this);
	}
	
	public void removeProject(Project project) {
		this.projects.remove(project);
		project.setCompany(null);
	}

	@Override
	public int compareTo(Company company) {
		if (this == company) {
			return 0;
		}
		return this.getName().compareToIgnoreCase(company.getName());
	}

}
