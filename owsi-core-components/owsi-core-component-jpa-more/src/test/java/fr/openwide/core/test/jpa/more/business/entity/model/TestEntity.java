package fr.openwide.core.test.jpa.more.business.entity.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

@Entity
public class TestEntity extends GenericEntity<Long, TestEntity> {
	private static final long serialVersionUID = 3827488123984866455L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Basic(optional = false)
	private String label;
	
	@Column
	private String simplePropertyUpdate;
	
	@Column
	private String simplePropertyUpdateInterceptor;
	
	@Column
	private String classicInterceptorSave;
	
	@Column
	private String classicInterceptorFlushDirty;

	public TestEntity(String label) {
		super();
		
		this.label = label;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
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

	public String getSimplePropertyUpdate() {
		return simplePropertyUpdate;
	}

	public void setSimplePropertyUpdate(String simplePropertyUpdate) {
		this.simplePropertyUpdate = simplePropertyUpdate;
	}

	public String getSimplePropertyUpdateInterceptor() {
		return simplePropertyUpdateInterceptor;
	}

	public void setSimplePropertyUpdateInterceptor(String simplePropertyUpdateInterceptor) {
		this.simplePropertyUpdateInterceptor = simplePropertyUpdateInterceptor;
	}

	public String getClassicInterceptorSave() {
		return classicInterceptorSave;
	}

	public void setClassicInterceptorSave(String classicInterceptorSave) {
		this.classicInterceptorSave = classicInterceptorSave;
	}

	public String getClassicInterceptorFlushDirty() {
		return classicInterceptorFlushDirty;
	}

	public void setClassicInterceptorFlushDirty(String classicInterceptorFlushDirty) {
		this.classicInterceptorFlushDirty = classicInterceptorFlushDirty;
	}


}
