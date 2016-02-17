package fr.openwide.core.basicapp.web.application.referencedata.model;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.google.common.collect.ImmutableMap;

import fr.openwide.core.basicapp.core.business.referencedata.model.City;
import fr.openwide.core.basicapp.core.business.referencedata.search.ICitySearchQuery;
import fr.openwide.core.jpa.more.business.generic.model.search.GenericListItemSort;
import fr.openwide.core.jpa.more.business.generic.query.IGenericListItemSearchQuery;
import fr.openwide.core.wicket.more.markup.html.sort.model.CompositeSortModel;
import fr.openwide.core.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;

public final class CityDataProvider extends AbstractGenericListItemDataProvider<City, GenericListItemSort> {

	private static final long serialVersionUID = -2635350066114992088L;

	private final IModel<String> codePostalModel = new Model<String>();
	
	public CityDataProvider() {
		super(
				new CompositeSortModel<GenericListItemSort>(
						CompositingStrategy.LAST_ONLY,
						ImmutableMap.of(
								GenericListItemSort.LABEL, GenericListItemSort.LABEL.getDefaultOrder()
						),
						ImmutableMap.of(
								GenericListItemSort.ID, GenericListItemSort.ID.getDefaultOrder()
						)
				)
		);
	}

	public IModel<String> getCodePostalModel() {
		return codePostalModel;
	}

	@Override
	protected IGenericListItemSearchQuery<City, GenericListItemSort, ?> createSearchQuery() {
		return createSearchQuery(ICitySearchQuery.class)
				.codePostal(codePostalModel.getObject());
	}
}
