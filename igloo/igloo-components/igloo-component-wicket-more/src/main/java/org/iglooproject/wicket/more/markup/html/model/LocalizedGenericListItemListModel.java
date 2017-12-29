package org.iglooproject.wicket.more.markup.html.model;

import java.util.Comparator;
import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import org.iglooproject.jpa.more.business.generic.model.EnabledFilter;
import org.iglooproject.jpa.more.business.generic.model.GenericLocalizedGenericListItem;
import org.iglooproject.jpa.more.business.generic.service.IGenericLocalizedGenericListItemService;

public class LocalizedGenericListItemListModel<T extends GenericLocalizedGenericListItem<?, ?>> extends LoadableDetachableModel<List<T>> {

	private static final long serialVersionUID = 1385903058801258105L;

	private final Class<T> clazz;
	private final Comparator<? super T> comparator;
	private final EnabledFilter enabledFilter;

	@SpringBean
	private IGenericLocalizedGenericListItemService<? super T, ?> listItemService;

	public LocalizedGenericListItemListModel(Class<T> clazz, Comparator<? super T> comparator) {
		this(clazz, comparator, true);
	}

	/**
	 * @deprecated Use {@link #LocalizedGenericListItemListModel(Class, Comparator, EnabledFilter)} instead
	 */
	@Deprecated
	public LocalizedGenericListItemListModel(Class<T> clazz, Comparator<? super T> comparator, boolean enabledOnly) {
		this(clazz, comparator, enabledOnly ? EnabledFilter.ENABLED_ONLY : EnabledFilter.ALL);
	}

	public LocalizedGenericListItemListModel(Class<T> clazz, Comparator<? super T> comparator, EnabledFilter enabledFilter) {
		super();
		Injector.get().inject(this);
		
		this.clazz = clazz;
		this.enabledFilter = enabledFilter;
		this.comparator = comparator;
	}

	@Override
	protected List<T> load() {
		return listItemService.list(clazz, enabledFilter, comparator);
	}

}
