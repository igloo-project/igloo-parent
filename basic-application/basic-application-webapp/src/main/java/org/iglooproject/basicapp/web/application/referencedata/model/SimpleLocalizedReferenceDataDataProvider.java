package org.iglooproject.basicapp.web.application.referencedata.model;

import org.apache.lucene.search.SortField;
import org.iglooproject.basicapp.core.business.referencedata.model.LocalizedReferenceData;
import org.iglooproject.basicapp.core.business.referencedata.search.ISimpleLocalizedReferenceDataSearchQuery;
import org.iglooproject.basicapp.core.business.referencedata.search.LocalizedReferenceDataSort;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.more.application.CoreWicketApplication;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;

import com.google.common.collect.ImmutableMap;

public abstract class SimpleLocalizedReferenceDataDataProvider<T extends LocalizedReferenceData<? super T>, S extends ISort<SortField>>
		extends AbstractLocalizedReferenceDataDataProvider<T, S> {

	private static final long serialVersionUID = -1391750059166474629L;

	public static <T extends LocalizedReferenceData<? super T>> SimpleLocalizedReferenceDataDataProvider<T, LocalizedReferenceDataSort> forQueryType(
			final Class<? extends ISimpleLocalizedReferenceDataSearchQuery<T, LocalizedReferenceDataSort>> queryType
	) {
		return new SimpleLocalizedReferenceDataDataProvider<T, LocalizedReferenceDataSort>(defaultSortModel()) {
			private static final long serialVersionUID = 1L;
			@Override
			protected ISimpleLocalizedReferenceDataSearchQuery<T, LocalizedReferenceDataSort> createSearchQuery() {
				return createSearchQuery(queryType);
			}
		};
	}

	public static <T extends LocalizedReferenceData<? super T>> SimpleLocalizedReferenceDataDataProvider<T, LocalizedReferenceDataSort> forItemType(
			final Class<T> itemType
	) {
		return forItemType(itemType, defaultSortModel());
	}

	public static <T extends LocalizedReferenceData<? super T>, S extends ISort<SortField>> SimpleLocalizedReferenceDataDataProvider<T, S> forItemType(
			final Class<T> itemType, CompositeSortModel<S> sortModel
	) {
		return new SimpleLocalizedReferenceDataDataProvider<T, S>(sortModel) {
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("unchecked")
			@Override
			protected ISimpleLocalizedReferenceDataSearchQuery<T, S> createSearchQuery() {
				return CoreWicketApplication.get().getApplicationContext().getBean(
						ISimpleLocalizedReferenceDataSearchQuery.class, itemType
				);
			}
		};
	}

	private static CompositeSortModel<LocalizedReferenceDataSort> defaultSortModel() {
		return new CompositeSortModel<LocalizedReferenceDataSort>(
						CompositingStrategy.LAST_ONLY,
						ImmutableMap.of(
								LocalizedReferenceDataSort.POSITION, LocalizedReferenceDataSort.POSITION.getDefaultOrder(),
								LocalizedReferenceDataSort.LABEL_FR, LocalizedReferenceDataSort.LABEL_FR.getDefaultOrder(),
								LocalizedReferenceDataSort.LABEL_EN, LocalizedReferenceDataSort.LABEL_EN.getDefaultOrder()
						),
						ImmutableMap.of(
								LocalizedReferenceDataSort.ID, LocalizedReferenceDataSort.ID.getDefaultOrder()
						)
				);
	}

	private SimpleLocalizedReferenceDataDataProvider(CompositeSortModel<S> sortModel) {
		super(sortModel);
	}

}
