package fr.openwide.hibernate.example.business.project.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import fr.openwide.hibernate.business.generic.model.GenericEntity;
import fr.openwide.hibernate.example.business.company.model.Company;
import fr.openwide.hibernate.example.business.person.model.Person;
import fr.openwide.hibernate.util.StringUtils;

@Entity
public class Project extends GenericEntity<Project> {
	
	private static final long serialVersionUID = -5735420033376178883L;

	@Id
	@GeneratedValue
	private Integer id;

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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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
	public String getNameForToString() {
		return getName();
	}
	
	@Override
	public int compareTo(Project project) {
		if (this == project) {
			return 0;
		}
		return StringUtils.removeAccents(this.getName()).compareToIgnoreCase(StringUtils.removeAccents(project.getName()));
	}

	@Override
	public String getDisplayName() {
		return getName();
	}
}
