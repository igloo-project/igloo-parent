package org.iglooproject.jpa.more.business.generic.service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.persistence.NonUniqueResultException;

import org.hibernate.search.annotations.Indexed;

import com.querydsl.core.types.EntityPath;

import org.iglooproject.jpa.business.generic.service.ITransactionalAspectAwareService;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.generic.model.EnabledFilter;
import org.iglooproject.jpa.more.business.generic.model.GenericLocalizedGenericListItem;
import org.iglooproject.jpa.more.business.localization.model.AbstractLocalizedText;

public interface IGenericLocalizedGenericListItemService<GE extends GenericLocalizedGenericListItem<?, T>, T extends AbstractLocalizedText>
		extends ITransactionalAspectAwareService {
	
	<E extends GE> E getById(Class<E> clazz, Long id);

	<E extends GE> void create(E entity);

	<E extends GE> void update(E entity);

	<E extends GE> void delete(E entity);

	<E extends GE> List<E> list(Class<E> clazz, Comparator<? super E> comparator);

	<E extends GE> List<E> list(Class<E> clazz, EnabledFilter enabledFilter, Comparator<? super E> comparator);

	<E extends GE> long count(Class<E> clazz);

	<E extends GE> long count(Class<E> clazz, EnabledFilter enabledFilter);

	/**
	 * @deprecated Use {@link #list(Class, EnabledFilter, Comparator)} instead.
	 */
	@Deprecated
	<E extends GE> List<E> listEnabled(Class<E> clazz, Comparator<? super E> comparator);

	<E extends GE> List<E> listByLocalizedLabel(EntityPath<E> source, Locale locale, String label);

	/**
	 * WARNING: only use this if unique constraints were set on the localized label columns of {@code source}.
	 */
	<E extends GE> E getByLocalizedLabel(EntityPath<E> source, Locale locale, String label) throws NonUniqueResultException;

	/**
	 * WARNING: only works on classes that were annotated with {@link Indexed}.
	 * 
	 * @deprecated Implement a SearchQuery akin to {@link org.iglooproject.jpa.more.business.generic.query.IGenericListItemSearchQuery)}.
	 */
	@Deprecated
	<E extends GE> List<E> searchAutocomplete(String searchPattern, Class<E> clazz, Locale locale, int limit, int offset)
			throws ServiceException;

	void flush();
	
	void clear();
}
