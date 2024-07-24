package org.iglooproject.jpa.more.business.history.model.embeddable;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import java.io.Serializable;
import java.time.Instant;
import org.bindgen.Bindable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

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

  @Basic(optional = true) // Might be defined as nullable in some places (using @AttributeOverride)
  @Column(nullable = false) // Non-nullable by default
  @GenericField(name = DATE, sortable = Sortable.YES)
  private Instant date;

  @Embedded
  @SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
  private HistoryValue subject;

  public Instant getDate() {
    return date;
  }

  public void setDate(Instant date) {
    this.date = date;
  }

  public HistoryValue getSubject() {
    return subject;
  }

  public void setSubject(HistoryValue subject) {
    this.subject = subject;
  }
}
