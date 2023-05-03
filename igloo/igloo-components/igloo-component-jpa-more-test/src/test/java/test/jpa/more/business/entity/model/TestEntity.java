package test.jpa.more.business.entity.model;

import java.time.Instant;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

import igloo.hibernateconfig.api.HibernateSearchAnalyzer;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
@Indexed
public class TestEntity extends GenericEntity<Long, TestEntity> {
	private static final long serialVersionUID = 3827488123984866455L;

	@Id
	@GeneratedValue
	private Long id;
	
	@FullTextField(analyzer = HibernateSearchAnalyzer.TEXT)
	@Basic(optional = false)
	private String label;
	
	@Column
	private String simplePropertyUpdate;
	
	@Column
	private String simplePropertyUpdateInterceptor;
	
	@Column
	@GenericField
	private String classicInterceptorSave;
	
	@Column
	private String classicInterceptorFlushDirty;

	@Column
	@GenericField
	private Instant dateCreation;

	public TestEntity() {
		super();
	}

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

	public Instant getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Instant dateCreation) {
		this.dateCreation = dateCreation;
	}


}
