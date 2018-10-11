package org.iglooproject.basicapp.core.business.message.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.bindgen.Bindable;
import org.hibernate.search.annotations.Indexed;
import org.iglooproject.basicapp.core.business.common.model.embeddable.LocalizedText;
import org.iglooproject.basicapp.core.business.message.model.atomic.GeneralMessageType;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEventSummary;

@Entity
@Bindable
@Cacheable
@Indexed
public class GeneralMessage extends GenericEntity<Long, GeneralMessage> {

	private static final long serialVersionUID = 3430831126687319860L;

	@Id
	@GeneratedValue
	private Long id;

	@Basic(optional = false)
	@Enumerated(EnumType.STRING)
	private GeneralMessageType type;

	@Embedded
	private LocalizedText title;

	@Embedded
	private LocalizedText description;

	@Basic(optional = false)
	private boolean active = true;

	@Embedded
	private HistoryEventSummary creation;

	@Embedded
	private HistoryEventSummary modification;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "startDateTime", column = @Column(nullable = false)),
		@AttributeOverride(name = "endDateTime", column = @Column(nullable = false))
	})
	private GeneralMessageDate publication;

	@Embedded
	private GeneralMessageDate interruption;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public GeneralMessageType getType() {
		return type;
	}

	public void setType(GeneralMessageType type) {
		this.type = type;
	}

	public LocalizedText getTitle() {
		if (title == null) {
			title = new LocalizedText();
		}
		return title;
	}

	public void setTitle(LocalizedText title) {
		this.title = (title == null ? null : new LocalizedText(title));
	}

	public LocalizedText getDescription() {
		if (description == null) {
			description = new LocalizedText();
		}
		return description;
	}

	public void setDescription(LocalizedText description) {
		this.description = (description == null ? null : new LocalizedText(description));
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public HistoryEventSummary getCreation() {
		if (creation == null) {
			creation = new HistoryEventSummary();
		}
		return creation;
	}

	public void setCreation(HistoryEventSummary creation) {
		this.creation = creation;
	}

	public HistoryEventSummary getModification() {
		if (modification == null) {
			modification = new HistoryEventSummary();
		}
		return modification;
	}

	public void setModification(HistoryEventSummary modification) {
		this.modification = modification;
	}

	public GeneralMessageDate getPublication() {
		if (publication == null) {
			publication = new GeneralMessageDate();
		}
		return publication;
	}

	public void setPublication(GeneralMessageDate publication) {
		this.publication = publication;
	}

	public GeneralMessageDate getInterruption() {
		if (interruption == null) {
			interruption = new GeneralMessageDate();
		}
		return interruption;
	}

	public void setInterruption(GeneralMessageDate interruption) {
		this.interruption = interruption;
	}

	@Override
	public String getDisplayName() {
		return toString();
	}

	@Override
	public String getNameForToString() {
		return getDisplayName();
	}

}
