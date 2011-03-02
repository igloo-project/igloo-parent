package fr.openwide.core.hibernate.security.business.authority.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.bindgen.Bindable;

import fr.openwide.core.hibernate.business.generic.model.GenericEntity;

@Entity
@Bindable
public class Authority extends GenericEntity<Integer, Authority> {

	private static final long serialVersionUID = -7704280784189665811L;

	@Id
	@GeneratedValue
	private Integer id;

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