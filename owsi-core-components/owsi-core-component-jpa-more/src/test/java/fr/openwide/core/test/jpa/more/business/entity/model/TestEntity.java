package fr.openwide.core.test.jpa.more.business.entity.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

@Entity
public class TestEntity extends GenericEntity<Integer, TestEntity> {
	private static final long serialVersionUID = 3827488123984866455L;

	@Id
	@GeneratedValue
	private Integer id;
	
	@Basic(optional = false)
	private String label;

	public TestEntity(String label) {
		super();
		
		this.label = label;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String getNameForToString() {
		return label;
	}

	@Override
	public String getDisplayName() {
		return label;
	}

}
