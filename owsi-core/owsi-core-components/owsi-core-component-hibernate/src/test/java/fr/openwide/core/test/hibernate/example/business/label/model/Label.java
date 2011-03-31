package fr.openwide.core.test.hibernate.example.business.label.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import fr.openwide.core.hibernate.business.generic.model.GenericEntity;

@Entity
public class Label extends GenericEntity<String, Label> {

	private static final long serialVersionUID = -2200458949166945096L;
	
	@Id
	private String id;
	
	private String value;

	public Label() {
	}
	
	public Label(String id, String value) {
		this.id = id;
		this.value = value;
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String getNameForToString() {
		return id;
	}

	@Override
	public String getDisplayName() {
		return value;
	}

}
