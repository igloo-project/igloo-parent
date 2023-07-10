package org.iglooproject.jpa.more.business.history.model;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bindgen.Bindable;
import org.hibernate.Length;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ObjectPath;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.PropertyValue;
import org.iglooproject.commons.util.collections.CollectionUtils;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;

import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Transient;

@Bindable
@SuppressFBWarnings("squid:S00107")
@MappedSuperclass
public abstract class AbstractHistoryLog<
				HL extends AbstractHistoryLog<HL, HET, HD>,
				HET extends Enum<HET>,
				HD extends AbstractHistoryDifference<HD, HL>>
		extends AbstractHistoryElement<HL, HL> {

	private static final long serialVersionUID = -1146280203615151992L;

	public static final String DATE = "date";

	public static final String EVENT_TYPE = "eventType";

	private static final String SUBJECT = "subject";
	private static final String SUBJECT_PREFIX = SUBJECT + ".";

	private static final String ALL_OBJECTS = "allObjects";
	private static final String ALL_OBJECTS_PREFIX = ALL_OBJECTS + ".";

	private static final String MAIN_OBJECT = "mainObject";
	private static final String OBJECT1 = "object1";
	private static final String OBJECT1_PREFIX = OBJECT1 + ".";
	private static final String OBJECT2 = "object2";
	private static final String OBJECT2_PREFIX = OBJECT2 + ".";
	private static final String OBJECT3 = "object3";
	private static final String OBJECT3_PREFIX = OBJECT3 + ".";
	private static final String OBJECT4 = "object4";
	private static final String OBJECT4_PREFIX = OBJECT4 + ".";
	
	public static final String SUBJECT_REFERENCE = SUBJECT_PREFIX + HistoryValue.REFERENCE;
	public static final String ALL_OBJECTS_REFERENCE = ALL_OBJECTS_PREFIX + HistoryValue.REFERENCE;
	public static final String OBJECT1_REFERENCE = OBJECT1_PREFIX + HistoryValue.REFERENCE;
	public static final String OBJECT2_REFERENCE = OBJECT2_PREFIX + HistoryValue.REFERENCE;
	public static final String OBJECT3_REFERENCE = OBJECT3_PREFIX + HistoryValue.REFERENCE;
	public static final String OBJECT4_REFERENCE = OBJECT4_PREFIX + HistoryValue.REFERENCE;

	public static final String HAS_DIFFERENCES = "hasDifferences";
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Basic(optional = false)
	@GenericField(name = DATE, sortable = Sortable.YES)
	private Instant date;

	@Basic(optional = false)
	@Enumerated(EnumType.STRING)
	@GenericField(name = EVENT_TYPE)
	private HET eventType;
	
	@Embedded
	@IndexedEmbedded(name = SUBJECT, includePaths = {HistoryValue.REFERENCE})
	@SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
	private HistoryValue subject;
	
	@Embedded
	@IndexedEmbedded(name = MAIN_OBJECT, includePaths = {HistoryValue.REFERENCE})
	private HistoryValue mainObject;
	
	@Embedded
	@IndexedEmbedded(name = OBJECT1, includePaths = {HistoryValue.REFERENCE})
	@SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
	private HistoryValue object1 = new HistoryValue();

	@Embedded
	@IndexedEmbedded(name = OBJECT2, includePaths = {HistoryValue.REFERENCE})
	@SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
	private HistoryValue object2 = new HistoryValue();

	@Embedded
	@IndexedEmbedded(name = OBJECT3, includePaths = {HistoryValue.REFERENCE})
	@SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
	private HistoryValue object3 = new HistoryValue();

	@Embedded
	@IndexedEmbedded(name = OBJECT4, includePaths = {HistoryValue.REFERENCE})
	@SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
	private HistoryValue object4 = new HistoryValue();
	
	@Basic
	@Column(length = Length.LONG32)
	private String comment;

	@OneToMany(mappedBy = "parentLog", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@OrderColumn
	private List<HD> differences = Lists.newArrayList();

	protected AbstractHistoryLog() {
		// nothing to do
	}
	
	protected AbstractHistoryLog(Instant date, HET eventType, HistoryValue mainObject) {
		this.date = date;
		this.eventType = eventType;
		this.mainObject = mainObject;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transient
	public HL getRootLog() {
		return (HL) this;
	}
	
	@Override
	@Transient
	protected HL getParent() {
		return null;
	}
	
	@Override
	@Transient
	public FieldPath getRelativePath() {
		return FieldPath.ROOT;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public HET getEventType() {
		return eventType;
	}

	public void setEventType(HET action) {
		this.eventType = action;
	}

	public HistoryValue getSubject() {
		return subject;
	}

	public void setSubject(HistoryValue subject) {
		this.subject = subject;
	}

	public HistoryValue getMainObject() {
		return mainObject;
	}

	public void setMainObject(HistoryValue mainObject) {
		this.mainObject = mainObject;
	}

	public HistoryValue getObject1() {
		return object1;
	}

	public void setObject1(HistoryValue object1) {
		this.object1 = object1;
	}

	public HistoryValue getObject2() {
		return object2;
	}

	public void setObject2(HistoryValue object2) {
		this.object2 = object2;
	}

	public HistoryValue getObject3() {
		return object3;
	}

	public void setObject3(HistoryValue object3) {
		this.object3 = object3;
	}

	public HistoryValue getObject4() {
		return object4;
	}

	public void setObject4(HistoryValue object4) {
		this.object4 = object4;
	}
	
	@IndexedEmbedded(name = ALL_OBJECTS, includePaths = {HistoryValue.REFERENCE})
	@IndexingDependency(derivedFrom = {
		@ObjectPath(@PropertyValue(propertyName = MAIN_OBJECT)),
		@ObjectPath(@PropertyValue(propertyName = OBJECT1)),
		@ObjectPath(@PropertyValue(propertyName = OBJECT2)),
		@ObjectPath(@PropertyValue(propertyName = OBJECT3)),
		@ObjectPath(@PropertyValue(propertyName = OBJECT4))
	})
	public Set<HistoryValue> getAllObjects() {
		Set<HistoryValue> result = Sets.newLinkedHashSet();
		for (HistoryValue value : new HistoryValue[] {mainObject, object1, object2, object3, object4}) {
			if (value != null) {
				result.add(value);
			}
		}
		return result;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<HD> getDifferences() {
		return Collections.unmodifiableList(differences);
	}

	public void setDifferences(List<HD> differences) {
		CollectionUtils.replaceAll(this.differences, differences);
	}
	
	@Transient
	//TODO: igloo-boot : dépendent de l'attribut differences, donc dépendent du problème du @OrderColumn
//	@IndexingDependency(derivedFrom = @ObjectPath(@PropertyValue(propertyName = "differences")))
//	@GenericField(name = HAS_DIFFERENCES)
	public boolean isDifferencesNonEmpty() {
		return !differences.isEmpty();
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper()
			.add("object1", object1.getLabel());
	}

}
