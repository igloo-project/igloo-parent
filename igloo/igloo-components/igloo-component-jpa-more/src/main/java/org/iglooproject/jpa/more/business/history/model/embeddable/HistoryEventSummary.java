package org.iglooproject.jpa.more.business.history.model.embeddable;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.SortableField;

import org.iglooproject.commons.util.CloneUtils;

/**
 * An {@link Embeddable embeddable} for storing limited information about an event:
 * <ul>
 * <li>The subject, i.e. the actor of the event
 * <li>The date/time of the event
 * </ul>
 * 
 * <p>This class generally used for storing information about the creation or last modification of an entity. It is
 * embedded into the entity itself, so the object of the event is implicit and doesn't have to be stored inside the
 * {@link HistoryEventSummary}.
 */
@Embeddable
public class HistoryEventSummary implements Serializable {

	private static final long serialVersionUID = 2512003373453632965L;

	public static final String DATE = "date";
	private static final String SUBJECT = "subject";
	private static final String SUBJECT_PREFIX = SUBJECT + ".";
	public static final String SUBJECT_REFERENCE = SUBJECT_PREFIX + HistoryValue.REFERENCE;

	@Basic(optional = true) // Might be defined as nullable in some places (using @AttributeOverride)
	@Column(nullable = false) // Non-nullable by default
	@Temporal(TemporalType.TIMESTAMP)
	@Field(name = DATE)
	@SortableField(forField = DATE)
	@SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
	private Date date;

	@Embedded
	@IndexedEmbedded(
			prefix = SUBJECT_PREFIX,
			includePaths = {
					HistoryValue.REFERENCE
			}
	)
	@SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
	private HistoryValue subject;

	public Date getDate() {
		return CloneUtils.clone(date);
	}

	public void setDate(Date date) {
		this.date = CloneUtils.clone(date);
	}
	
	public HistoryValue getSubject() {
		return subject;
	}
	
	public void setSubject(HistoryValue subject) {
		this.subject = subject;
	}

}
