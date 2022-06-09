package org.iglooproject.basicapp.web.application.history.model;

import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.basicapp.core.business.history.model.HistoryLog;
import org.iglooproject.basicapp.core.business.history.model.atomic.HistoryEventType;
import org.iglooproject.basicapp.core.business.history.search.IHistoryLogSearchQuery;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.more.business.history.search.HistoryLogSort;
import org.iglooproject.jpa.more.business.search.query.ISearchQuery;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;
import org.iglooproject.wicket.more.model.AbstractSearchQueryDataProvider;
import org.iglooproject.wicket.more.model.GenericEntityModel;

import igloo.wicket.model.Detachables;

public class HistoryLogDataProvider extends AbstractSearchQueryDataProvider<HistoryLog, HistoryLogSort> {

	private static final long serialVersionUID = 1604966591810765209L;

	private final IModel<? extends User> subjectModel;

	private final IModel<Date> dateMinModel = new Model<>();
	private final IModel<Date> dateMaxModel = new Model<>();

	private final IModel<? extends GenericEntity<?, ?>> objectModel;

	private final Set<HistoryEventType> mandatoryDifferencesEventTypes = EnumSet.noneOf(HistoryEventType.class);

	private final CompositeSortModel<HistoryLogSort> sortModel = new CompositeSortModel<>(CompositingStrategy.LAST_ONLY, HistoryLogSort.DATE);

	public static HistoryLogDataProvider subject(IModel<? extends User> subjectModel) {
		return new HistoryLogDataProvider(subjectModel, new GenericEntityModel<>());
	}

	public static HistoryLogDataProvider object(IModel<? extends GenericEntity<?, ?>> objectModel) {
		return new HistoryLogDataProvider(new GenericEntityModel<Long, User>(), objectModel);
	}

	public HistoryLogDataProvider(IModel<? extends User> subjectModel, IModel<? extends GenericEntity<?, ?>> objectModel) {
		this.subjectModel = subjectModel;
		this.objectModel = objectModel;
	}

	@Override
	public IModel<HistoryLog> model(HistoryLog object) {
		return GenericEntityModel.of(object);
	}

	@Override
	protected ISearchQuery<HistoryLog, HistoryLogSort> getSearchQuery() {
		return createSearchQuery(IHistoryLogSearchQuery.class)
				.subject(subjectModel.getObject())
				.date(dateMinModel.getObject(), dateMaxModel.getObject())
				.object(objectModel.getObject())
				.mandatoryDifferencesEventTypes(mandatoryDifferencesEventTypes)
				.sort(sortModel.getObject());
	}

	public IModel<Date> getDateMinModel() {
		return dateMinModel;
	}

	public IModel<Date> getDateMaxModel() {
		return dateMaxModel;
	}

	public HistoryLogDataProvider addMandatoryDifferencesEventType(HistoryEventType eventType) {
		mandatoryDifferencesEventTypes.add(eventType);
		return this;
	}

	public CompositeSortModel<HistoryLogSort> getSortModel() {
		return sortModel;
	}

	@Override
	public void detach() {
		super.detach();
		Detachables.detach(
			subjectModel,
			dateMinModel,
			dateMaxModel,
			objectModel,
			sortModel
		);
	}

}
