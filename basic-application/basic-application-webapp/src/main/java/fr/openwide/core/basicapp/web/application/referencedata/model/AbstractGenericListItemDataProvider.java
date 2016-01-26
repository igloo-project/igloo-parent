package fr.openwide.core.basicapp.web.application.referencedata.model;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.google.common.collect.ImmutableMap;

import fr.openwide.core.jpa.more.business.generic.model.EnabledFilter;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.generic.model.search.GenericListItemSort;
import fr.openwide.core.jpa.more.business.search.query.ISearchQuery;
import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.markup.html.sort.model.CompositeSortModel;
import fr.openwide.core.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;
import fr.openwide.core.wicket.more.model.AbstractSearchQueryDataProvider;

public abstract class AbstractGenericListItemDataProvider<T extends GenericListItem<T>, S extends ISort<?>> extends AbstractSearchQueryDataProvider<T, S> {

	private static final long serialVersionUID = 268769371721921631L;
	
	private final IModel<EnabledFilter> enabledFilterModel = new Model<EnabledFilter>();
	private final IModel<String> labelModel = new Model<String>();
	
	private final CompositeSortModel<GenericListItemSort> sortModel = new CompositeSortModel<GenericListItemSort>(
			CompositingStrategy.LAST_ONLY,
			ImmutableMap.of(
					GenericListItemSort.LABEL, GenericListItemSort.LABEL.getDefaultOrder()
			),
			ImmutableMap.of(
					GenericListItemSort.ID, GenericListItemSort.ID.getDefaultOrder()
			)
	);
	
	public AbstractGenericListItemDataProvider() {
		Injector.get().inject(this);
	}
	
	public AbstractGenericListItemDataProvider(EnabledFilter enabledFilter) {
		this();
		enabledFilterModel.setObject(enabledFilter);
	}
	
	@Override
	public void detach() {
		super.detach();
		enabledFilterModel.detach();
		labelModel.detach();
		sortModel.detach();
	}
	
	public IModel<EnabledFilter> getEnabledFilterModel() {
		return enabledFilterModel;
	}

	public IModel<String> getLabelModel() {
		return labelModel;
	}

	public CompositeSortModel<GenericListItemSort> getSortModel() {
		return sortModel;
	}
	
	@Override
	public ISearchQuery<T, S> getSearchQuery() {
		return createSearchQuery();
	}
	
	protected abstract ISearchQuery<T, S> createSearchQuery();
}
