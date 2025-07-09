package org.iglooproject.jpa.more.business.history.model.embeddable;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import java.io.Serializable;
import java.time.Instant;
import org.bindgen.Bindable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;

/**
 * An {@link Embeddable embeddable} for storing limited information about an event:
 *
 * <ul>
 *   <li>The subject, i.e. the actor of the event
 *   <li>The date/time of the event
 * </ul>
 *
 * <p>This class generally used for storing information about the creation or last modification of
 * an entity. It is embedded into the entity itself, so the object of the event is implicit and
 * doesn't have to be stored inside the {@link HistoryEventSummary}.
 */
@Embeddable
@Bindable
public class HistoryEventSummary implements Serializable {

  private static final long serialVersionUID = 2512003373453632965L;

  public static final String DATE = "date";

  private static final String SUBJECT = "subject";
  private static final String SUBJECT_EMBEDDED = SUBJECT + "Embedded";
  public static final String SUBJECT_REFERENCE = SUBJECT_EMBEDDED + "." + HistoryValue.REFERENCE;

  @Basic(optional = true) // Might be defined as nullable in some places (using @AttributeOverride)
  @Column(nullable = false) // Non-nullable by default
  @GenericField(name = DATE, sortable = Sortable.YES)
  private Instant date;

  @Embedded
  @IndexedEmbedded(
      name = SUBJECT_EMBEDDED,
      includePaths = {HistoryValue.REFERENCE})
  @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
  @SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
  private HistoryEventValue subject;

  public Instant getDate() {
    return date;
  }

  public void setDate(Instant date) {
    this.date = date;
  }

  public IHistoryValue getSubject() {
    return subject;
  }

  public void setSubject(IHistoryValue subject) {
    if (subject != null) {
      this.subject = subject.<HistoryEventValue>toValue(HistoryEventValue::build);
    } else {
      this.subject = null;
    }
  }
}
