package fr.openwide.core.wicket.more.markup.html.model;

import java.util.Comparator;
import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.jpa.more.business.generic.model.GenericLocalizedGenericListItem;
import fr.openwide.core.jpa.more.business.generic.service.IGenericLocalizedGenericListItemService;

public class LocalizedGenericListItemListModel<T extends GenericLocalizedGenericListItem<?, ?>> extends LoadableDetachableModel<List<T>> {

	private static final long serialVersionUID = 1385903058801258105L;

	private final Class<T> clazz;
	private final boolean enabled;
	private final Comparator<? super T> comparator;

	@SpringBean
	private IGenericLocalizedGenericListItemService<? super T, ?> listItemService;

	public LocalizedGenericListItemListModel(Class<T> clazz, boolean enabled, Comparator<? super T> comparator) {
		super();
		Injector.get().inject(this);
		
		this.clazz = clazz;
		this.enabled = enabled;
		this.comparator = comparator;
	}

	@Override
	protected List<T> load() {
		if (enabled) {
			return listItemService.listEnabled(clazz, comparator);
		} else {
			return listItemService.list(clazz, comparator);
		}
	}

}
