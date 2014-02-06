package fr.openwide.core.jpa.more.business.generic.dao;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.persistence.metamodel.SingularAttribute;

import com.mysema.query.types.EntityPath;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.generic.model.GenericLocalizedGenericListItem;
import fr.openwide.core.jpa.more.business.localization.model.AbstractLocalizedText;

public interface IGenericLocalizedGenericListItemDao<GE extends GenericLocalizedGenericListItem<?, T>, T extends AbstractLocalizedText> {

	<E extends GE> E getEntity(Class<E> clazz, Long id);

	<E extends GE> E getById(Class<E> clazz, Long id);

	<E extends GE> E getByNaturalId(Class<E> clazz, String naturalId);

	<E extends GE> void update(E entity);

	<E extends GE> void save(E entity);

	<E extends GE> void delete(E entity);

	<E extends GE> E refresh(E entity);

	<E extends GE> List<E> list(Class<E> clazz);

	<E extends GE> Long count(Class<E> clazz);

	<E extends GE, V> E getByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue);

	<E extends GE, V> List<E> listByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue);

	<E extends GE, V> Long countByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue);

	/**
	 * @param comparator A comparator to use when sorting the results. If <code>null</code>, use natural ordering.
	 */
	<E extends GE, V> List<E> listEnabledByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue, Comparator<? super E> comparator);

	<E extends GE, V> Long countEnabledByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue);

	<E extends GE> List<E> listByLocalizedLabel(EntityPath<E> source, Locale locale, String label);

	<E extends GE> List<E> searchAutocomplete(String searchPattern, Class<E> clazz, Locale locale, Integer limit, Integer offset) throws ServiceException;

	void flush();

	void clear();
}
