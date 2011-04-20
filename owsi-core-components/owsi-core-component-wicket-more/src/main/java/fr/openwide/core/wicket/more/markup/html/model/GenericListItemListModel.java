package fr.openwide.core.wicket.more.markup.html.model;

import java.util.List;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.generic.service.IGenericListItemService;

public class GenericListItemListModel<T extends GenericListItem<? super T>> extends LoadableDetachableModel<List<T>> {

	private static final long serialVersionUID = 1385903058801258105L;

	private Class<T> clazz;

	private boolean enabled;

	@SpringBean(name = "genericListItemService")
	private IGenericListItemService genericListItemService;

	public GenericListItemListModel(Class<T> clazz, boolean enabled) {
		super();
		InjectorHolder.getInjector().inject(this);
		
		this.clazz = clazz;
		this.enabled = enabled;
	}

	@Override
	protected List<T> load() {
		if (enabled) {
			return genericListItemService.listEnabled(clazz);
		} else {
			return genericListItemService.list(clazz);
		}
	}

}
