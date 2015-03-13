package fr.openwide.core.wicket.more.model;

import java.util.List;

import javax.inject.Provider;

import org.apache.lucene.search.SortField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import fr.openwide.core.jpa.more.business.search.query.ISearchQuery;
import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.application.CoreWicketApplication;
import fr.openwide.core.wicket.more.markup.repeater.data.LoadableDetachableDataProvider;

public abstract class AbstractSearchQueryDataProvider<T, S extends ISort<SortField>> extends LoadableDetachableDataProvider<T>
		implements IErrorAwareDataProvider {

	private static final long serialVersionUID = 8767962077258633492L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSearchQueryDataProvider.class);

	private boolean errorFlag;
	
	protected AbstractSearchQueryDataProvider() {
	}
	
	/**
	 * {@code @SpringBean} ne fonctionne pas avec les {@link Provider}. Il est donc nécessaire de récupérer la {@link ISearchQuery}
	 * manuellement depuis le contexte à chaque fois qu'on en veut une nouvelle.
	 */
	protected final <Q extends ISearchQuery<T, S>> Q createSearchQuery(Class<Q> clazz) {
		return CoreWicketApplication.get().getApplicationContext().getBean(clazz);
	}
	
	protected abstract ISearchQuery<T, S> getSearchQuery(); 

	@Override
	protected List<T> loadList(long limit, long offset) {
		try {
			errorFlag |= false;
			return getSearchQuery().list(limit, offset);
		} catch (Exception e) {
			LOGGER.error("Erreur lors de la recherche : {}", e);
			errorFlag |= true;
			return Lists.newArrayList();
		}
	}
	
	@Override
	protected long loadSize() {
		try {
			errorFlag |= false;
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

