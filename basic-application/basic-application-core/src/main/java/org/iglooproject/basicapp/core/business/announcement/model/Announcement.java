package org.iglooproject.basicapp.core.business.announcement.model;

import org.bindgen.Bindable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.iglooproject.basicapp.core.business.announcement.model.atomic.AnnouncementType;
import org.iglooproject.basicapp.core.business.announcement.model.embeddable.AnnouncementDate;
import org.iglooproject.basicapp.core.business.common.model.embeddable.LocalizedText;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEventSummary;

import com.google.common.base.MoreObjects.ToStringHelper;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Basic;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
@Bindable
@Cacheable
@Indexed
public class Announcement extends GenericEntity<Long, Announcement> {

	private static final long serialVersionUID = 3430831126687319860L;

	@Id
	@GeneratedValue
	private Long id;

	@Basic(optional = false)
	@Enumerated(EnumType.STRING)
	private AnnouncementType type;

	@Embedded
	private LocalizedText title;

	@Embedded
	private LocalizedText description;

	@Basic(optional = false)
	private boolean enabled = true;

	@Embedded
	private HistoryEventSummary creation;

	@Embedded
	private HistoryEventSummary modification;

	@Embedded
	@AttributeOverride(name = "startDateTime", column = @Column(nullable = false))
	@AttributeOverride(name = "endDateTime", column = @Column(nullable = false))
	private AnnouncementDate publication;

	@Embedded
	private AnnouncementDate interruption;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public AnnouncementType getType() {
		return type;
	}

	public void setType(AnnouncementType type) {
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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

	public AnnouncementDate getPublication() {
		if (publication == null) {
			publication = new AnnouncementDate();
		}
		return publication;
	}

	public void setPublication(AnnouncementDate publication) {
		this.publication = publication;
	}

	public AnnouncementDate getInterruption() {
		if (interruption == null) {
			interruption = new AnnouncementDate();
		}
		return interruption;
	}

	public void setInterruption(AnnouncementDate interruption) {
		this.interruption = interruption;
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper()
			.add("type", getType())
			.add("publicationStartDateTime", getPublication().getStartDateTime())
			.add("publicationEndDateTime", getPublication().getEndDateTime());
	}

}
