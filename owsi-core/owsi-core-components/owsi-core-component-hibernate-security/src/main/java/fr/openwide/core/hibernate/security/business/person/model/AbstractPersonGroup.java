package fr.openwide.core.hibernate.security.business.person.model;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderBy;

import org.bindgen.Bindable;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import fr.openwide.core.hibernate.business.generic.model.GenericEntity;
import fr.openwide.core.hibernate.search.util.HibernateSearchAnalyzer;
import fr.openwide.core.hibernate.security.business.authority.model.Authority;

@MappedSuperclass
@Bindable
public abstract class AbstractPersonGroup<G extends AbstractPersonGroup<G, P>, P extends AbstractPerson<P>>
		extends GenericEntity<Integer, G>
		implements PersonGroup {

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
	private List<P> persons = new LinkedList<P>();
	
	@ManyToMany
	@Cascade({CascadeType.SAVE_UPDATE})
	@OrderBy("name")
	private Set<Authority> authorities = new LinkedHashSet<Authority>();
	
	@Type(type = "org.hibernate.type.StringClobType")
	private String description;
	
	@Column(nullable = false)
	private boolean locked = false;
	
	public AbstractPersonGroup() {
	}

	public AbstractPersonGroup(String name) {
		this.name = name;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getDisplayName() {
		return this.getName();
	}
	
	public List<P> getPersons() {
		return persons;
	}

	public void setPersons(List<P> persons) {
		this.persons = persons;
	}
	
	public void addPerson(P person) {
		this.persons.add(person);
		person.getPersonGroups().add(this);
	}
	
	public void removePerson(P person) {
		this.persons.remove(person);
		person.getPersonGroups().remove(this);
	}
	
	@Override
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
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	@Override
	public int compareTo(G group) {
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