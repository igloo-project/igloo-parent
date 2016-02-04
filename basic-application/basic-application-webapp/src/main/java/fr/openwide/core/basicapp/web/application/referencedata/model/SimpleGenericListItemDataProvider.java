package fr.openwide.core.basicapp.web.application.referencedata.model;

import com.google.common.collect.ImmutableMap;

import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.generic.model.search.GenericListItemSort;
import fr.openwide.core.jpa.more.business.generic.query.IGenericListItemSearchQuery;
import fr.openwide.core.wicket.more.markup.html.sort.model.CompositeSortModel;
import fr.openwide.core.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;

public final class SimpleGenericListItemDataProvider<T extends GenericListItem<? super T>>
		extends AbstractGenericListItemDataProvider<T, GenericListItemSort> {

	private static final long serialVersionUID = 5698542948288856570L;
	
	private final Class<? extends IGenericListItemSearchQuery<T, GenericListItemSort, ?>> queryInterface;

	public SimpleGenericListItemDataProvider(
			Class<? extends IGenericListItemSearchQuery<T, GenericListItemSort, ?>> queryInterface) {
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
		this.queryInterface = queryInterface;
	}
	
	@Override
	protected IGenericListItemSearchQuery<T, GenericListItemSort, ?> createSearchQuery() {
		return createSearchQuery(queryInterface);
	}
}
