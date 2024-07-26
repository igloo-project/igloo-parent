package org.iglooproject.basicapp.web.application.referencedata.model;

import com.google.common.collect.ImmutableMap;
import org.apache.lucene.search.SortField;
import org.iglooproject.basicapp.core.business.referencedata.model.ReferenceData;
import org.iglooproject.basicapp.core.business.referencedata.search.IBasicReferenceDataSearchQuery;
import org.iglooproject.basicapp.core.business.referencedata.search.IReferenceDataSearchQuery;
import org.iglooproject.basicapp.core.business.referencedata.search.ReferenceDataSort;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.more.application.CoreWicketApplication;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;

public abstract class BasicReferenceDataDataProvider<
        T extends ReferenceData<? super T>, S extends ISort<SortField>>
    extends AbstractReferenceDataDataProvider<T, S> {

  private static final long serialVersionUID = -1391750059166474629L;

  @SuppressWarnings("unchecked")
  public static <T extends ReferenceData<? super T>, S extends ISort<SortField>>
      BasicReferenceDataDataProvider<T, S> forQueryType(
          final Class<? extends IReferenceDataSearchQuery<T, S, ?>> queryType) {
    return new BasicReferenceDataDataProvider<T, S>((CompositeSortModel<S>) defaultSortModel()) {
      private static final long serialVersionUID = 1L;

      @Override
      protected IReferenceDataSearchQuery<T, S, ?> createSearchQuery() {
        return createSearchQuery(queryType);
      }
    };
  }

  @SuppressWarnings("unchecked")
  public static <T extends ReferenceData<? super T>, S extends ISort<SortField>>
      BasicReferenceDataDataProvider<T, S> forItemType(final Class<T> itemType) {
    return forItemType(itemType, (CompositeSortModel<S>) defaultSortModel());
  }

  public static <T extends ReferenceData<? super T>, S extends ISort<SortField>>
      BasicReferenceDataDataProvider<T, S> forItemType(
          final Class<T> itemType, CompositeSortModel<S> sortModel) {
    return new BasicReferenceDataDataProvider<T, S>(sortModel) {
      private static final long serialVersionUID = 1L;

      @SuppressWarnings("unchecked")
      @Override
      protected IReferenceDataSearchQuery<T, S, ?> createSearchQuery() {
        return CoreWicketApplication.get()
            .getApplicationContext()
            .getBean(IBasicReferenceDataSearchQuery.class, itemType);
      }
    };
  }

  private static CompositeSortModel<ReferenceDataSort> defaultSortModel() {
    return new CompositeSortModel<>(
        CompositingStrategy.LAST_ONLY,
        ImmutableMap.of(
            ReferenceDataSort.POSITION, ReferenceDataSort.POSITION.getDefaultOrder(),
            ReferenceDataSort.LABEL_FR, ReferenceDataSort.LABEL_FR.getDefaultOrder(),
            ReferenceDataSort.LABEL_EN, ReferenceDataSort.LABEL_EN.getDefaultOrder()),
        ImmutableMap.of(ReferenceDataSort.ID, ReferenceDataSort.ID.getDefaultOrder()));
  }

  private BasicReferenceDataDataProvider(CompositeSortModel<S> sortModel) {
    super(sortModel);
  }
}
