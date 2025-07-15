package org.iglooproject.wicket.more.model.search.query;

import com.google.common.primitives.Ints;
import igloo.wicket.model.Detachables;
import java.util.List;
import java.util.Objects;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.search.query.ISearchQuery;
import org.iglooproject.jpa.more.search.query.ISearchQueryData;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.repeater.data.LoadableDetachableDataProvider;
import org.iglooproject.wicket.more.model.GenericEntityModel;

public abstract class SearchQueryDataProvider<
        T extends GenericEntity<Long, ?>,
        S extends ISort<?>,
        D extends ISearchQueryData<? super T>,
        Q extends ISearchQuery<T, S, D>>
    extends LoadableDetachableDataProvider<T> {

  private static final long serialVersionUID = 1L;

  private final IModel<D> dataModel;

  protected SearchQueryDataProvider(IModel<D> dataModel) {
    Injector.get().inject(this);
    this.dataModel = Objects.requireNonNull(dataModel);
  }

  public IModel<D> getDataModel() {
    return dataModel;
  }

  @Override
  public IModel<T> model(T object) {
    return GenericEntityModel.of(object);
  }

  @Override
  protected List<T> loadList(long first, long count) {
    return searchQuery()
        .list(
            dataModel.getObject(),
            getSortModel().getObject(),
            Ints.saturatedCast(first),
            Ints.saturatedCast(count))
        .stream()
        .toList();
  }

  @Override
  protected long loadSize() {
    return searchQuery().size(dataModel.getObject());
  }

  protected abstract Q searchQuery();

  public abstract CompositeSortModel<S> getSortModel();

  @Override
  public void detach() {
    super.detach();
    Detachables.detach(dataModel, getSortModel());
  }
}
