package fr.openwide.core.jpa.more.business.generic.dao;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.persistence.metamodel.SingularAttribute;

import com.querydsl.core.types.EntityPath;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.generic.model.EnabledFilter;
import fr.openwide.core.jpa.more.business.generic.model.GenericLocalizedGenericListItem;
import fr.openwide.core.jpa.more.business.localization.model.AbstractLocalizedText;

public interface IGenericLocalizedGenericListItemDao<GE extends GenericLocalizedGenericListItem<?, T>, T extends AbstractLocalizedText> {

	<E extends GE> E getEntity(Class<E> clazz, Long id);

	<E extends GE> E getById(Class<E> clazz, Long id);

	<E extends GE> E getByNaturalId(Class<E> clazz, Object naturalId);

	<E extends GE> void update(E entity);

	<E extends GE> void save(E entity);

	<E extends GE> void delete(E entity);

	<E extends GE> E refresh(E entity);

	<E extends GE> List<E> list(Class<E> clazz, EnabledFilter enabledFilter, Comparator<? super E> comparator);

	<E extends GE> List<E> list(Class<E> clazz, EnabledFilter enabledFilter);

	<E extends GE> List<E> list(Class<E> clazz);

	<E extends GE> Long count(Class<E> clazz, EnabledFilter enabledFilter);

	<E extends GE> Long count(Class<E> clazz);

	/**
	 * @deprecated Utiliser QueryDSL.
	 */
	@Deprecated
	<E extends GE, V extends Comparable<?>> E getByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue);

	/**
	 * @deprecated Utiliser QueryDSL.
	 */
	@Deprecated
	<E extends GE, V extends Comparable<?>> List<E> listByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue,
			EnabledFilter enabledFilter, Comparator<? super E> comparator);

	/**
	 * @deprecated Utiliser QueryDSL.
	 */
	@Deprecated
	<E extends GE, V extends Comparable<?>> List<E> listByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue,
			EnabledFilter enabledFilter);

	/**
	 * @deprecated Utiliser QueryDSL.
	 */
	@Deprecated
	<E extends GE, V extends Comparable<?>> List<E> listByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue);

	/**
	 * @deprecated Utiliser QueryDSL.
	 */
	@Deprecated
	<E extends GE, V extends Comparable<?>> Long countByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue, EnabledFilter enabledFilter);

	/**
	 * @deprecated Utiliser QueryDSL.
	 */
	@Deprecated
	<E extends GE, V extends Comparable<?>> Long countByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue);

	/**
	 * @deprecated Use {@link #listByField(Class, SingularAttribute, Object, EnabledFilter, Comparator)} instead.
	 */
	@Deprecated
	<E extends GE, V extends Comparable<?>> List<E> listEnabledByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue, Comparator<? super E> comparator);

	/**
	 * @deprecated Use {@link #countByField(Class, SingularAttribute, Object, EnabledFilter)} instead.
	 */
	@Deprecated
	<E extends GE, V extends Comparable<?>> Long countEnabledByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue);

	<E extends GE> List<E> listByLocalizedLabel(EntityPath<E> source, Locale locale, String label);

	/**
	 * @deprecated Implement a SearchQuery akin to {@link fr.openwide.core.jpa.more.business.generic.query.IGenericListItemSearchQuery)}.
	 */
	@Deprecated
	<E extends GE> List<E> searchAutocomplete(String searchPattern, Class<E> clazz, Locale locale, Integer limit, Integer offset) throws ServiceException;

	void flush();

	void clear();
}
