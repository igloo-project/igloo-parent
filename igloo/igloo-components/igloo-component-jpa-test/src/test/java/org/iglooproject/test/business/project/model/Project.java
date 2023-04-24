package org.iglooproject.test.business.project.model;

import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.test.business.company.model.Company;
import org.iglooproject.test.business.person.model.Person;

@Entity
public class Project extends GenericEntity<Long, Project> {
	
	private static final long serialVersionUID = -5735420033376178883L;

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@ManyToOne
	private Company company;

	@ManyToMany(mappedBy = "workedProjects")
	private List<Person> team = new LinkedList<Person>();

	public Project() {
	}

	public Project(String name) {
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

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public List<Person> getTeam() {
		return team;
	}

	public void setTeam(List<Person> team) {
		this.team = team;
	}

	@Override
	public int compareTo(Project project) {
		if (this == project) {
			return 0;
		}
		return StringUtils.removeAccents(this.getName()).compareToIgnoreCase(StringUtils.removeAccents(project.getName()));
	}

}
