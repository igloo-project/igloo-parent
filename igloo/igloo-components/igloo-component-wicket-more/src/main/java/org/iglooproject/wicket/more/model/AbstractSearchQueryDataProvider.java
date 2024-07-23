package org.iglooproject.wicket.more.model;

import com.google.common.collect.Lists;
import java.util.List;
import javax.inject.Provider;
import org.iglooproject.jpa.more.business.search.query.ISearchQuery;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.more.application.CoreWicketApplication;
import org.iglooproject.wicket.more.markup.repeater.data.LoadableDetachableDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSearchQueryDataProvider<T, S extends ISort<?>>
    extends LoadableDetachableDataProvider<T> implements IErrorAwareDataProvider {

  private static final long serialVersionUID = 8767962077258633492L;

  private static final Logger LOGGER =
      LoggerFactory.getLogger(AbstractSearchQueryDataProvider.class);

  private boolean errorFlag = false;

  protected AbstractSearchQueryDataProvider() {}

  /**
   * {@code @SpringBean} ne fonctionne pas avec les {@link Provider}. Il est donc nécessaire de
   * récupérer la {@link ISearchQuery} manuellement depuis le contexte à chaque fois qu'on en veut
   * une nouvelle.
   */
  protected final <Q extends ISearchQuery<T, S>> Q createSearchQuery(Class<Q> clazz) {
    return CoreWicketApplication.get().getApplicationContext().getBean(clazz);
  }

  protected abstract ISearchQuery<T, S> getSearchQuery();

  @Override
  protected List<T> loadList(long offset, long limit) {
    try {
      return getSearchQuery().list(offset, limit);
    } catch (Exception e) {
      LOGGER.error("Erreur lors de la recherche : {}", e);
      errorFlag |= true;
      return Lists.newArrayList();
    }
  }

  @Override
  protected long loadSize() {
    try {
      return getSearchQuery().count();
    } catch (Exception e) {
      LOGGER.error("Erreur lors de la recherche : {}", e);
      errorFlag |= true;
      return 0;
    }
  }

  @Override
  public void detach() {
    super.detach();
    errorFlag = false;
  }

  @Override
  public boolean hasError() {
    return errorFlag;
  }
}
