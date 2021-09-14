package org.iglooproject.jpa.more.business.referencedata.model;

import javax.persistence.Basic;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.bindgen.Bindable;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.SortableField;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

import com.google.common.base.MoreObjects.ToStringHelper;

import igloo.hibernateconfig.api.HibernateSearchAnalyzer;

@MappedSuperclass
@Bindable
public abstract class GenericReferenceData<E extends GenericReferenceData<?, ?>, T>
		extends GenericEntity<Long, E>
		implements IGenericReferenceDataBindingInterface<T> {

	private static final long serialVersionUID = -4060821429592653558L;

	public static final String POSITION = "position";
	public static final String ENABLED = "enabled";

	@Id
	@DocumentId
	@GeneratedValue
	private Long id;

	public abstract void setLabel(T label);

	@Override
	public abstract T getLabel();
	
	@Basic(optional = false)
	@Field(name = POSITION, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
	@SortableField(forField = POSITION)
	@SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
	private Integer position = 0;

	@Field(name = ENABLED)
	@Basic(optional = false)
	@SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
	private boolean enabled = true;
	
	@Basic(optional = false)
	private boolean editable = true;
	
	@Basic(optional = false)
	private boolean disableable = true;
	
	@Basic(optional = false)
	private boolean deleteable = false;

	protected GenericReferenceData() {
	}

	public GenericReferenceData(T label) {
		this(label, 0);
	}

	public GenericReferenceData(T label, Integer position) {
		setLabel(label);
		this.position = position;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public void setPosition(Integer order) {
		this.position = order;
	}

	@Override
	public Integer getPosition() {
		return position;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	@Override
	public boolean isDisableable() {
		return disableable;
	}

	public void setDisableable(boolean disableable) {
		this.disableable = disableable;
	}

	@Override
	public boolean isDeleteable() {
		return deleteable;
	}

	public void setDeleteable(boolean deleteable) {
		this.deleteable = deleteable;
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper()
			.add("label", getLabel());
	}

}
