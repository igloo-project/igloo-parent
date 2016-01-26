package fr.openwide.core.basicapp.web.application.referencedata.model;

import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.more.business.generic.model.EnabledFilter;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.generic.model.search.GenericListItemSort;
import fr.openwide.core.jpa.more.business.generic.query.IGenericListItemSearchQuery;
import fr.openwide.core.jpa.more.business.search.query.ISearchQuery;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class SimpleGenericListItemDataProvider<T extends GenericListItem<T>> extends AbstractGenericListItemDataProvider<T, GenericListItemSort> {

	private static final long serialVersionUID = 5698542948288856570L;

	public SimpleGenericListItemDataProvider() {
		super();
	}
	
	public SimpleGenericListItemDataProvider(EnabledFilter enabledFilter) {
		super(enabledFilter);
	}
	
	@Override
	public IModel<T> model(T item) {
		return GenericEntityModel.of(item);
	}
	
	@Override
	protected ISearchQuery<T, GenericListItemSort> createSearchQuery() {
		return createSearchQuery(IGenericListItemSearchQuery.class)
				.label(getLabelModel().getObject())
				.enabled(getEnabledFilterModel().getObject())
				.sort(getSortModel().getObject());
	}
}
