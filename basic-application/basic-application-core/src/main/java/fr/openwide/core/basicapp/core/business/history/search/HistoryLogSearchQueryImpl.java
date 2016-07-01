package fr.openwide.core.basicapp.core.business.history.search;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.EnumUtils;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.openwide.core.basicapp.core.business.history.model.HistoryLog;
import fr.openwide.core.basicapp.core.business.history.model.atomic.HistoryEventType;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.model.GenericEntityReference;
import fr.openwide.core.jpa.more.business.history.model.AbstractHistoryLog;
import fr.openwide.core.jpa.more.business.history.search.HistoryLogSort;
import fr.openwide.core.jpa.more.business.search.query.AbstractHibernateSearchSearchQuery;

@Component
@Scope("prototype")
public class HistoryLogSearchQueryImpl extends AbstractHibernateSearchSearchQuery<HistoryLog, HistoryLogSort>
		implements IHistoryLogSearchQuery {
	
	protected HistoryLogSearchQueryImpl() {
		super(HistoryLog.class);
	}
	
	@Override
	public IHistoryLogSearchQuery differencesMandatoryFor(Set<HistoryEventType> mandatoryDifferencesEventTypes) {
		if (!mandatoryDifferencesEventTypes.isEmpty()) {
			List<HistoryEventType> allowedWithoutDifferenceEventTypes = EnumUtils.getEnumList(HistoryEventType.class);
			allowedWithoutDifferenceEventTypes.removeAll(mandatoryDifferencesEventTypes);
			BooleanJunction<?> junction = getDefaultQueryBuilder().bool();
			shouldIfNotNull(
					junction,
					matchOneIfGiven(Bindings.historyLog().eventType(), allowedWithoutDifferenceEventTypes),
					matchIfGiven(AbstractHistoryLog.HAS_DIFFERENCES, true)
			);
			must(junction.createQuery());
		}
		return this;
	}
	
	@Override
	public IHistoryLogSearchQuery subject(User user) {
		must(matchIfGiven(AbstractHistoryLog.SUBJECT_REFERENCE, GenericEntityReference.of(user)));
		return this;
	}
	
	@Override
	public IHistoryLogSearchQuery date(Date dateMin, Date dateMax) {
		must(matchRange(
				AbstractHistoryLog.DATE,
				dateMin,
				dateMax
		));
		
		return this;
	}

	@Override
	public IHistoryLogSearchQuery object(GenericEntity<?, ?> object) {
		must(matchIfGiven(AbstractHistoryLog.ALL_OBJECTS_REFERENCE, GenericEntityReference.of(object)));
		return this;
	}

	@Override
	public IHistoryLogSearchQuery object1(GenericEntity<?, ?> object) {
		must(matchIfGiven(AbstractHistoryLog.OBJECT1_REFERENCE, GenericEntityReference.of(object)));
		return this;
	}

	@Override
	public IHistoryLogSearchQuery object2(GenericEntity<?, ?> object) {
		must(matchIfGiven(AbstractHistoryLog.OBJECT2_REFERENCE, GenericEntityReference.of(object)));
		return this;
	}

	@Override
	public IHistoryLogSearchQuery object3(GenericEntity<?, ?> object) {
		must(matchIfGiven(AbstractHistoryLog.OBJECT3_REFERENCE, GenericEntityReference.of(object)));
		return this;
	}

	@Override
	public IHistoryLogSearchQuery object4(GenericEntity<?, ?> object) {
		must(matchIfGiven(AbstractHistoryLog.OBJECT4_REFERENCE, GenericEntityReference.of(object)));
		return this;
	}
}
