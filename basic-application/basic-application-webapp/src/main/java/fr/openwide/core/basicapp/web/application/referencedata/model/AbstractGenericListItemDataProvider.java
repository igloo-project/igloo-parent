package fr.openwide.core.basicapp.web.application.referencedata.model;

import org.apache.lucene.search.SortField;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import fr.openwide.core.jpa.more.business.generic.model.EnabledFilter;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.generic.query.IGenericListItemSearchQuery;
import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.markup.html.sort.model.CompositeSortModel;
import fr.openwide.core.wicket.more.model.AbstractSearchQueryDataProvider;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public abstract class AbstractGenericListItemDataProvider
		<T extends GenericListItem<? super T>, S extends ISort<SortField>>
		extends AbstractSearchQueryDataProvider<T, S> {

	private static final long serialVersionUID = 268769371721921631L;
	
	private final IModel<EnabledFilter> enabledFilterModel = new Model<EnabledFilter>(EnabledFilter.ENABLED_ONLY);
	private final IModel<String> labelModel = new Model<String>();
	private final IModel<String> codeModel = new Model<String>();
	
	private final CompositeSortModel<S> sortModel;
	
	public AbstractGenericListItemDataProvider(CompositeSortModel<S> sortModel) {
		this.sortModel = sortModel;
		Injector.get().inject(this);
	}
	
	@Override
	public IModel<T> model(T item) {
		return GenericEntityModel.of(item);
	}
	
	@Override
	public void detach() {
		super.detach();
		enabledFilterModel.detach();
		labelModel.detach();
		codeModel.detach();
		sortModel.detach();
	}
	
	public IModel<EnabledFilter> getEnabledFilterModel() {
		return enabledFilterModel;
	}

	public IModel<String> getLabelModel() {
		return labelModel;
	}
	
	public IModel<String> getCodeModel() {
		return codeModel;
	}

	public CompositeSortModel<S> getSortModel() {
		return sortModel;
	}
	
	@Override
	public final IGenericListItemSearchQuery<T, S, ?> getSearchQuery() {
		return createSearchQuery()
				.label(getLabelModel().getObject())
				.code(getCodeModel().getObject())
				.enabled(getEnabledFilterModel().getObject())
				.sort(getSortModel().getObject());
	}
	
	protected abstract IGenericListItemSearchQuery<T, S, ?> createSearchQuery();
}
