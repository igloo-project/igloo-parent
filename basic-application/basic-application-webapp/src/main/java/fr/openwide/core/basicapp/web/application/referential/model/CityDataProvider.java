package fr.openwide.core.basicapp.web.application.referential.model;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.google.common.collect.ImmutableMap;

import fr.openwide.core.basicapp.core.business.referential.model.City;
import fr.openwide.core.basicapp.core.business.referential.model.search.CitySort;
import fr.openwide.core.basicapp.core.business.referential.model.search.ICitySearchQuery;
import fr.openwide.core.jpa.more.business.generic.model.EnabledFilter;
import fr.openwide.core.jpa.more.business.search.query.ISearchQuery;
import fr.openwide.core.wicket.more.markup.html.sort.model.CompositeSortModel;
import fr.openwide.core.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;
import fr.openwide.core.wicket.more.model.AbstractSearchQueryDataProvider;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class CityDataProvider extends AbstractSearchQueryDataProvider<City, CitySort> {

	private static final long serialVersionUID = 3936199873078187365L;
	
	private final IModel<EnabledFilter> enabledFilterModel = new Model<EnabledFilter>();
	private final IModel<String> labelModel = new Model<String>();
	
	private final CompositeSortModel<CitySort> sortModel = new CompositeSortModel<CitySort>(
			CompositingStrategy.LAST_ONLY,
			ImmutableMap.of(
					CitySort.LABEL, CitySort.LABEL.getDefaultOrder()
			),
			ImmutableMap.of(
					CitySort.ID, CitySort.ID.getDefaultOrder()
			)
	);
	
	public CityDataProvider() {
		Injector.get().inject(this);
	}
	
	public CityDataProvider(EnabledFilter enabledFilter) {
		this();
		enabledFilterModel.setObject(enabledFilter);
	}
	
	@Override
	public void detach() {
		super.detach();
		enabledFilterModel.detach();
		labelModel.detach();
	}
	
	@Override
	public IModel<City> model(City ville) {
		return GenericEntityModel.of(ville);
	}
	
	public IModel<EnabledFilter> getEnabledFilterModel() {
		return enabledFilterModel;
	}

	public IModel<String> getLabelModel() {
		return labelModel;
	}

	public CompositeSortModel<CitySort> getSortModel() {
		return sortModel;
	}

	@Override
	public ISearchQuery<City, CitySort> getSearchQuery() {
		return createSearchQuery(ICitySearchQuery.class)
				.label(labelModel.getObject())
				.enabled(enabledFilterModel.getObject())
				.sort(sortModel.getObject());
	}
}
