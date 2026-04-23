package basicapp.back.business.announcement.model;

import basicapp.back.business.announcement.model.atomic.AnnouncementType;
import basicapp.back.business.announcement.model.embeddable.AnnouncementDate;
import basicapp.back.business.common.model.embeddable.LocalizedText;
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
import org.bindgen.Bindable;
import org.hibernate.Length;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEventSummary;

@Entity
@Bindable
@Cacheable
@Indexed
public class Announcement extends GenericEntity<Long, Announcement> {

  private static final long serialVersionUID = 3430831126687319860L;

  @Id @GeneratedValue private Long id;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private AnnouncementType type;

  @Embedded
  @AttributeOverride(name = "fr", column = @Column(length = Length.LONG32))
  @AttributeOverride(name = "en", column = @Column(length = Length.LONG32))
  private LocalizedText content;

  @Embedded private AnnouncementDate unavailability;

  @Embedded
  @AttributeOverride(name = "startDateTime", column = @Column(nullable = false))
  @AttributeOverride(name = "endDateTime", column = @Column(nullable = false))
  private AnnouncementDate publication;

  @Basic(optional = false)
  private boolean enabled = true;

  @Embedded private HistoryEventSummary creation;

  @Embedded private HistoryEventSummary modification;

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

  public LocalizedText getContent() {
    if (content == null) {
      content = new LocalizedText();
    }
    return content;
  }

  public void setContent(LocalizedText content) {
    this.content = (content == null ? null : new LocalizedText(content));
  }

  public AnnouncementDate getUnavailability() {
    if (unavailability == null) {
      unavailability = new AnnouncementDate();
    }
    return unavailability;
  }

  public void setUnavailability(AnnouncementDate unavailability) {
    this.unavailability = unavailability;
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

  @Override
  protected ToStringHelper toStringHelper() {
    return super.toStringHelper()
        .add("type", getType())
        .add("publicationStartDateTime", getPublication().getStartDateTime())
        .add("publicationEndDateTime", getPublication().getEndDateTime());
  }
}
