package fr.openwide.core.jpa.security.business.authority.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.bindgen.Bindable;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.search.util.HibernateSearchAnalyzer;

@Entity
@Bindable
public class Authority extends GenericEntity<Integer, Authority> {

	private static final long serialVersionUID = -7704280784189665811L;

	@Id
	@GeneratedValue
	private Integer id;

	@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
	private String name;

	public Authority() {
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Authority(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(Authority authority) {
		if(this == authority) {
			return 0;
		}
		return this.getName().compareToIgnoreCase(authority.getName());
	}

	@Override
	public String getNameForToString() {
		return getName();
	}
	
	@Override
	public String getDisplayName() {
		return getName();
	}
}