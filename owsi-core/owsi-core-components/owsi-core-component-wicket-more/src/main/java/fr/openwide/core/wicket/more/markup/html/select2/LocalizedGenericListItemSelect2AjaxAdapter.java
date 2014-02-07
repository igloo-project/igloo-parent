package fr.openwide.core.wicket.more.markup.html.select2;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.Session;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.generic.model.GenericLocalizedGenericListItem;
import fr.openwide.core.jpa.more.business.generic.service.IGenericLocalizedGenericListItemService;

public class LocalizedGenericListItemSelect2AjaxAdapter<E extends GenericLocalizedGenericListItem<?, ?>>
		extends AbstractLongIdGenericEntitySelect2AjaxAdapter<E> {

	private static final long serialVersionUID = -1596052281032908173L;
	
	private static Logger LOGGER = LoggerFactory.getLogger(LocalizedGenericListItemSelect2AjaxAdapter.class);
	
	@SpringBean
	private IGenericLocalizedGenericListItemService<? super E, ?> localizedGenericListItemService;
	
	private final Class<E> clazz;
	
	public LocalizedGenericListItemSelect2AjaxAdapter(Class<E> clazz) {
		super(clazz, new DefaultLocalizedGenericListItemChoiceRenderer());
		this.clazz = clazz;
		Injector.get().inject(this);
	}

	@Override
	public List<E> getChoices(int start, int count, String filter) {
		try {
			return localizedGenericListItemService.searchAutocomplete(filter, clazz, getLocale(), count, start);
		} catch (ServiceException e) {
			LOGGER.error("Error while searching for " + clazz.getSimpleName(), e);
			return Lists.newArrayList();
		}
	}
	
	protected Locale getLocale() {
		return Session.get().getLocale();
	}

}
