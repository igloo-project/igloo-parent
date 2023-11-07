package org.iglooproject.test.business.project.model;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.test.business.company.model.Company;
import org.iglooproject.test.business.person.model.Person;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

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
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Project)) {
			return false;
		}
		Project other = (Project) obj;
		return new EqualsBuilder()
			.append(StringUtils.removeAccents(getName()), StringUtils.removeAccents(other.getName()))
			.appendSuper(super.equals(other))
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(StringUtils.removeAccents(getName()))
			.appendSuper(super.hashCode())
			.toHashCode();
	}

	@Override
	public int compareTo(Project project) {
		return new CompareToBuilder()
			.append(StringUtils.removeAccents(getName()), StringUtils.removeAccents(project.getName()))
			.appendSuper(super.compareTo(project))
			.toComparison();
	}

}
