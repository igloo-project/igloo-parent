package test.jpa.more.business.history.model;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import org.bindgen.Bindable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ObjectPath;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.PropertyValue;
import org.iglooproject.commons.util.collections.CollectionUtils;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;

import com.google.common.collect.Lists;

import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Transient;
import test.jpa.more.business.history.model.atomic.TestHistoryEventType;

@Entity
@Bindable
@Cacheable
@Indexed
public class TestHistoryLog extends AbstractHistoryLog<TestHistoryLog, TestHistoryEventType, TestHistoryDifference> {

	private static final long serialVersionUID = -8557932643510393694L;

	// TODO igloo-boot: moved to concrete class; waiting for https://hibernate.atlassian.net/browse/HHH-16641
	@OneToMany(mappedBy = "parentLog", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@OrderColumn
	private List<TestHistoryDifference> differences = Lists.newArrayList();
	
	public TestHistoryLog() {
		// nothing to do
	}
	
	public TestHistoryLog(Instant date, TestHistoryEventType eventType, HistoryValue mainObject) {
		super(date, eventType, mainObject);
	}
	
	@Override
	@Transient
	@IndexingDependency(derivedFrom = @ObjectPath(@PropertyValue(propertyName = "differences")))
	@GenericField(name = HAS_DIFFERENCES)
	public boolean isDifferencesNonEmpty() {
		return !differences.isEmpty();
	}

	@Override
	public List<TestHistoryDifference> getDifferences() {
		return Collections.unmodifiableList(differences);
	}

	@Override
	public void setDifferences(List<TestHistoryDifference> differences) {
		CollectionUtils.replaceAll(this.differences, differences);
	}


}
