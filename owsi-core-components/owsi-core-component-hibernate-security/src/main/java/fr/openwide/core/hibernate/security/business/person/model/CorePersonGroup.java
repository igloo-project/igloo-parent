package fr.openwide.core.hibernate.security.business.person.model;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import fr.openwide.core.hibernate.business.generic.model.GenericEntity;
import fr.openwide.core.hibernate.search.util.HibernateSearchAnalyzer;
import fr.openwide.core.hibernate.security.business.authority.model.Authority;

@Entity
@Indexed
@Inheritance(strategy=InheritanceType.JOINED)
public class CorePersonGroup extends GenericEntity<Integer, CorePersonGroup> {

	private static final long serialVersionUID = 2156717229285615454L;
	
	@Id
	@DocumentId
	@GeneratedValue
	private Integer id;

	@Field(index=Index.TOKENIZED, store=Store.NO)
	@Analyzer(definition = HibernateSearchAnalyzer.TEXT)
	private String name;
	
	@ManyToMany
	@Cascade({CascadeType.SAVE_UPDATE})
	@OrderBy("lastName")
	private List<CorePerson> persons = new LinkedList<CorePerson>();
	
	@ManyToMany
	@Cascade({CascadeType.SAVE_UPDATE})
	@OrderBy("name")
	private Set<Authority> authorities = new LinkedHashSet<Authority>();
	
	public CorePersonGroup() {
	}

	public CorePersonGroup(String name) {
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
	
	public String getDisplayName() {
		return this.getName();
	}
	
	public List<CorePerson> getPersons() {
		return persons;
	}

	public void setPersons(List<CorePerson> persons) {
		this.persons = persons;
	}
	
	public void addPerson(CorePerson person) {
		this.persons.add(person);
		person.getGroups().add(this);
	}
	
	public void removePerson(CorePerson person) {
		this.persons.remove(person);
		person.getGroups().remove(this);
	}
	
	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}
	
	public void addAuthority(Authority authority) {
		this.authorities.add(authority);
	}
	
	public void removeAuthority(Authority authority) {
		this.authorities.remove(authority);
	}

	@Override
	public int compareTo(CorePersonGroup group) {
		if(this == group) {
			return 0;
		}
		return DEFAULT_STRING_COLLATOR.compare(this.getName(), group.getName());
	}

	@Override
	public String getNameForToString() {
		return getName();
	}

}