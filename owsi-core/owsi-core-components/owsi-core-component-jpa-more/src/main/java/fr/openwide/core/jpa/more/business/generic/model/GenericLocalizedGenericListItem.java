package fr.openwide.core.jpa.more.business.generic.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.bindgen.Bindable;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.more.business.localization.model.AbstractLocalizedText;

@MappedSuperclass
@Bindable
public abstract class GenericLocalizedGenericListItem<E extends GenericLocalizedGenericListItem<?, ?>, T extends AbstractLocalizedText>
		extends GenericEntity<Long, E> {

	private static final long serialVersionUID = 3909665325202378602L;

	@Id
	@DocumentId
	@GeneratedValue
	private Long id;

	public abstract void setLabel(T label);

	public abstract T getLabel();

	@Field
	@Column(nullable = false)
	private boolean enabled = true;
	
	@Column(nullable = false)
	private boolean editable = true;
	
	@Column(nullable = false)
	private boolean disableable = true;
	
	@Column(nullable = false)
	private boolean deleteable = false;

	public GenericLocalizedGenericListItem() {
		super();
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isDisableable() {
		return disableable;
	}

	public void setDisableable(boolean disableable) {
		this.disableable = disableable;
	}

	public boolean isDeleteable() {
		return deleteable;
	}

	public void setDeleteable(boolean deleteable) {
		this.deleteable = deleteable;
	}
	
	public String getCode() {
		return null;
	}

	@Override
	@JsonIgnore
	public String getNameForToString() {
		T text = getLabel();
		return text == null ? "" : text.toString(); // NOSONAR
	}

	@Override
	@JsonIgnore
	public String getDisplayName() {
		return toString();
	}

}