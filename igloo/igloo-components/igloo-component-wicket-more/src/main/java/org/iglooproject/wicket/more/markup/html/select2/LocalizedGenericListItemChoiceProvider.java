package org.iglooproject.wicket.more.markup.html.select2;

import java.util.Locale;

import org.apache.wicket.Session;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.generic.model.GenericLocalizedGenericListItem;
import org.iglooproject.jpa.more.business.generic.service.IGenericLocalizedGenericListItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.select2.Response;

public class LocalizedGenericListItemChoiceProvider<E extends GenericLocalizedGenericListItem<?, ?>>
		extends AbstractLongIdGenericEntityChoiceProvider<E> {

	private static final long serialVersionUID = -1596052281032908173L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LocalizedGenericListItemChoiceProvider.class);
	
	private static final int PAGE_LIMIT = 10;
	
	@SpringBean
	private IGenericLocalizedGenericListItemService<? super E, ?> localizedGenericListItemService;
	
	private final Class<E> clazz;
	
	public LocalizedGenericListItemChoiceProvider(Class<E> clazz) {
		super(clazz, new DefaultLocalizedGenericListItemChoiceRenderer());
		this.clazz = clazz;
		Injector.get().inject(this);
	}

	protected Locale getLocale() {
		return Session.get().getLocale();
	}

	@Override
	public void query(String term, int page, Response<E> response) {
		try {
			response.addAll(localizedGenericListItemService.searchAutocomplete(term, clazz, getLocale(), PAGE_LIMIT + 1, page * PAGE_LIMIT));
		} catch (ServiceException e) {
			LOGGER.error("Error while searching for " + clazz.getSimpleName(), e);
			return;
		}
	}

}
