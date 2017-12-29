package org.iglooproject.wicket.more.markup.html.model;

import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import org.iglooproject.jpa.more.business.generic.model.EnabledFilter;
import org.iglooproject.jpa.more.business.generic.model.GenericListItem;
import org.iglooproject.jpa.more.business.generic.service.IGenericListItemService;

public class GenericListItemListModel<T extends GenericListItem<?>> extends LoadableDetachableModel<List<T>> {

	private static final long serialVersionUID = -8014868217254919305L;
	
	private Class<T> clazz;
	private final Comparator<? super T> comparator;
	private final EnabledFilter enabledFilter;

	@SpringBean(name = "genericListItemService")
	private IGenericListItemService genericListItemService;

	/**
	 * @deprecated Use {@link #GenericListItemListModel(Class, EnabledFilter)} instead
	 */
	@Deprecated
	public GenericListItemListModel(Class<T> clazz, boolean enabledOnly) {
		this(clazz, null, enabledOnly);
	}

	public GenericListItemListModel(Class<T> clazz, EnabledFilter enabledFilter) {
		this(clazz, null, enabledFilter);
	}

	public GenericListItemListModel(Class<T> clazz, @Nullable Comparator<? super T> comparator) {
		this(clazz, comparator, EnabledFilter.ENABLED_ONLY);
	}

	/**
	 * @deprecated Use {@link #GenericListItemListModel(Class, Comparator, EnabledFilter)} instead
	 */
	@Deprecated
	public GenericListItemListModel(Class<T> clazz, @Nullable Comparator<? super T> comparator, boolean enabledOnly) {
		this(clazz, comparator, enabledOnly ? EnabledFilter.ENABLED_ONLY : EnabledFilter.ALL);
	}

	public GenericListItemListModel(Class<T> clazz, @Nullable Comparator<? super T> comparator, EnabledFilter enabledFilter) {
		super();
		Injector.get().inject(this);
		
		this.clazz = clazz;
		this.enabledFilter = enabledFilter;
		this.comparator = comparator;
	}

	@Override
	protected List<T> load() {
		return genericListItemService.list(clazz, enabledFilter, comparator);
	}

}
