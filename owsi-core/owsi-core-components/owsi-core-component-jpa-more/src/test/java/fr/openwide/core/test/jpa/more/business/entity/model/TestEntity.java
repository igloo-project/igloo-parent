package fr.openwide.core.test.jpa.more.business.entity.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import fr.openwide.core.commons.util.CloneUtils;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.search.util.HibernateSearchAnalyzer;

@Entity
@Indexed
public class TestEntity extends GenericEntity<Long, TestEntity> {
	private static final long serialVersionUID = 3827488123984866455L;

	@Id
	@DocumentId
	@GeneratedValue
	private Long id;
	
	@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT))
	@Basic(optional = false)
	private String label;
	
	@Column
	private String simplePropertyUpdate;
	
	@Column
	private String simplePropertyUpdateInterceptor;
	
	@Field
	@Column
	private String classicInterceptorSave;
	
	@Column
	private String classicInterceptorFlushDirty;

	@Field
	@Column
	private Date dateCreation;

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

	public Date getDateCreation() {
		return CloneUtils.clone(dateCreation);
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = CloneUtils.clone(dateCreation);
	}


}
