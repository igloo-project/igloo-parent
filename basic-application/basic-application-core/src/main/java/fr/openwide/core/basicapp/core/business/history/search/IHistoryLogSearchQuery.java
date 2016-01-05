package fr.openwide.core.basicapp.core.business.history.search;

import java.util.Date;
import java.util.Set;

import fr.openwide.core.basicapp.core.business.history.model.HistoryLog;
import fr.openwide.core.basicapp.core.business.history.model.atomic.HistoryEventType;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.more.business.history.search.HistoryLogSort;
import fr.openwide.core.jpa.more.business.search.query.ISearchQuery;

public interface IHistoryLogSearchQuery extends ISearchQuery<HistoryLog, HistoryLogSort> {
	
	IHistoryLogSearchQuery subject(User user);

	IHistoryLogSearchQuery date(Date dateMin, Date dateMax);
	
	IHistoryLogSearchQuery object(GenericEntity<?, ?> object);
	
	IHistoryLogSearchQuery object1(GenericEntity<?, ?> object);
	
	IHistoryLogSearchQuery object2(GenericEntity<?, ?> object);
	
	IHistoryLogSearchQuery object3(GenericEntity<?, ?> object);
	
	IHistoryLogSearchQuery object4(GenericEntity<?, ?> object);

	IHistoryLogSearchQuery differencesMandatoryFor(Set<HistoryEventType> mandatoryDifferencesEventTypes);
	
}
