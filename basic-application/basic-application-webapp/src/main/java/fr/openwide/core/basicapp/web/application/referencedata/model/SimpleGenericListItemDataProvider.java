package fr.openwide.core.basicapp.web.application.referencedata.model;

import org.apache.lucene.search.SortField;

import com.google.common.collect.ImmutableMap;

import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.generic.model.search.GenericListItemSort;
import fr.openwide.core.jpa.more.business.generic.query.IGenericListItemSearchQuery;
import fr.openwide.core.jpa.more.business.generic.query.ISimpleGenericListItemSearchQuery;
import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.application.CoreWicketApplication;
import fr.openwide.core.wicket.more.markup.html.sort.model.CompositeSortModel;
import fr.openwide.core.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;

public abstract class SimpleGenericListItemDataProvider<T extends GenericListItem<? super T>, S extends ISort<SortField>>
		extends AbstractGenericListItemDataProvider<T, S> {

	private static final long serialVersionUID = 5698542948288856570L;

	public static <T extends GenericListItem<? super T>> SimpleGenericListItemDataProvider<T, GenericListItemSort> forQueryType(
			final Class<? extends IGenericListItemSearchQuery<T, GenericListItemSort, ?>> queryType
			) {
		return new SimpleGenericListItemDataProvider<T, GenericListItemSort>(defaultSortModel()) {
			private static final long serialVersionUID = 1L;
			@Override
			protected IGenericListItemSearchQuery<T, GenericListItemSort, ?> createSearchQuery() {
				return createSearchQuery(queryType);
			}
		};
	}
	
	public static <T extends GenericListItem<? super T>> SimpleGenericListItemDataProvider<T, GenericListItemSort>
			forItemType(final Class<T> itemType) {
		return new SimpleGenericListItemDataProvider<T, GenericListItemSort>(defaultSortModel()) {
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("unchecked")
			@Override
			protected IGenericListItemSearchQuery<T, GenericListItemSort, ?> createSearchQuery() {
				return CoreWicketApplication.get().getApplicationContext().getBean(
						ISimpleGenericListItemSearchQuery.class, itemType
				);
			}
		};
	}
	
	private static CompositeSortModel<GenericListItemSort> defaultSortModel() {
		return new CompositeSortModel<GenericListItemSort>(
						CompositingStrategy.LAST_ONLY,
						ImmutableMap.of(
								GenericListItemSort.LABEL, GenericListItemSort.LABEL.getDefaultOrder()
						),
						ImmutableMap.of(
								GenericListItemSort.ID, GenericListItemSort.ID.getDefaultOrder()
						)
				);
	}

	private SimpleGenericListItemDataProvider(CompositeSortModel<S> sortModel) {
		super(sortModel);
	}
	
	@Override
	protected abstract IGenericListItemSearchQuery<T, S, ?> createSearchQuery();
}
