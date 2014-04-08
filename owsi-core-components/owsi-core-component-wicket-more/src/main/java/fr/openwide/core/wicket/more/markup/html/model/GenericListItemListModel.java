package fr.openwide.core.wicket.more.markup.html.model;

import java.util.Comparator;
import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.generic.service.IGenericListItemService;

public class GenericListItemListModel<T extends GenericListItem<?>> extends LoadableDetachableModel<List<T>> {

	private static final long serialVersionUID = -8014868217254919305L;
	
	private Class<T> clazz;
	private final Comparator<? super T> comparator;
	private boolean enabledOnly;

	@SpringBean(name = "genericListItemService")
	private IGenericListItemService genericListItemService;

	public GenericListItemListModel(Class<T> clazz, boolean enabledOnly) {
		this(clazz, null, enabledOnly);
	}

	public GenericListItemListModel(Class<T> clazz, Comparator<? super T> comparator) {
		this(clazz, comparator, true);
	}

	public GenericListItemListModel(Class<T> clazz, Comparator<? super T> comparator, boolean enabledOnly) {
		super();
		Injector.get().inject(this);
		
		this.clazz = clazz;
		this.enabledOnly = enabledOnly;
		this.comparator = comparator;
	}

	@Override
	protected List<T> load() {
		if (enabledOnly) {
			return genericListItemService.listEnabled(clazz, comparator);
		} else {
			return genericListItemService.list(clazz, comparator);
		}
	}

}
