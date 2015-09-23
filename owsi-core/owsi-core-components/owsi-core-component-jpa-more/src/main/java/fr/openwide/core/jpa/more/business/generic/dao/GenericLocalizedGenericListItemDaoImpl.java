package fr.openwide.core.jpa.more.business.generic.dao;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.persistence.metamodel.SingularAttribute;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;

import fr.openwide.core.jpa.business.generic.dao.AbstractEntityDaoImpl;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.generic.model.EnabledFilter;
import fr.openwide.core.jpa.more.business.generic.model.GenericLocalizedGenericListItem;
import fr.openwide.core.jpa.more.business.generic.model.GenericLocalizedGenericListItemBinding;
import fr.openwide.core.jpa.more.business.generic.model.QGenericLocalizedGenericListItem;
import fr.openwide.core.jpa.more.business.localization.model.AbstractLocalizedText;
import fr.openwide.core.jpa.search.service.IHibernateSearchService;
import fr.openwide.core.spring.util.lucene.search.LuceneUtils;

public abstract class GenericLocalizedGenericListItemDaoImpl<GE extends GenericLocalizedGenericListItem<?, T>, T extends AbstractLocalizedText>
		extends AbstractEntityDaoImpl<GE> implements IGenericLocalizedGenericListItemDao<GE, T> {
	
	@Autowired
	private IHibernateSearchService hibernateSearchService;
	
	public GenericLocalizedGenericListItemDaoImpl() {
	}
	
	@Override
	public <E extends GE> E getEntity(Class<E> clazz, Long id) {
		return super.getEntity(clazz, id);
	}
	
	@Override
	public <E extends GE> E getById(Class<E> clazz, Long id) {
		return super.getEntity(clazz, id);
	}
	
	@Override
	public <E extends GE> E getByNaturalId(Class<E> clazz, Object naturalId) {
		return super.getEntityByNaturalId(clazz, naturalId);
	}
	
	@Override
	public <E extends GE> void update(E entity) { // NOSONAR
		super.update(entity);
	}
	
	@Override
	public <E extends GE> void save(E entity) { // NOSONAR
		super.save(entity);
	}
	
	@Override
	public <E extends GE> void delete(E entity) { // NOSONAR
		super.delete(entity);
	}
	
	@Override
	public <E extends GE> E refresh(E entity) { // NOSONAR
		return super.refresh(entity);
	}
	
	@Override
	public <E extends GE> List<E> list(Class<E> clazz) {
		return super.listEntity(clazz);
	}
	
	@Override
	public <E extends GE> List<E> list(Class<E> clazz, EnabledFilter enabledFilter) {
		return list(clazz, enabledFilter, null);
	}
	
	@Override
	public <E extends GE> List<E> list(Class<E> clazz, EnabledFilter enabledFilter, Comparator<? super E> comparator) {
		PathBuilder<E> pathBuilder = new PathBuilder<E>(clazz, "rootAlias");
		QGenericLocalizedGenericListItem entityPath = new QGenericLocalizedGenericListItem(pathBuilder);
		
		JPAQuery query;
		if (EnabledFilter.ENABLED_ONLY.equals(enabledFilter)) {
			query = queryByPredicate(entityPath, entityPath.enabled.isTrue());
		} else {
			query = queryByPredicate(entityPath, null);
		}
		
		@SuppressWarnings("unchecked")
		List<E> entities = (List<E>) query.list(entityPath);
		
		Collections.sort(entities, comparator);
		
		return entities;
	}
	
	@Override
	public <E extends GE> Long count(Class<E> clazz) {
		return count(clazz, EnabledFilter.ALL);
	}

	@Override
	public <E extends GE> Long count(Class<E> clazz, EnabledFilter enabledFilter) {
		Pair<EntityPath<E>, JPAQuery> queryItem = queryByField(clazz, null, null, enabledFilter);
		return queryItem.getValue1().distinct().count();
	}
	
	/**
	 * @deprecated Utiliser QueryDSL
	 */
	@Deprecated
	@Override
	public <E extends GE,V extends Comparable<?>> E getByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue) {
		return super.getEntityByField(clazz, field, fieldValue);
	}

	/**
	 * @deprecated Utiliser QueryDSL
	 */
	@Deprecated
	@Override
	public <E extends GE,V extends Comparable<?>> List<E> listByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue) {
		return listByField(clazz, field, fieldValue, EnabledFilter.ALL, null);
	}

	/**
	 * @deprecated Utiliser QueryDSL
	 */
	@Deprecated
	@Override
	public <E extends GE, V extends Comparable<?>> List<E> listByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue,
			EnabledFilter enabledFilter) {
		return listByField(clazz, field, fieldValue, enabledFilter, null);
	}

	/**
	 * @deprecated Utiliser QueryDSL
	 */
	@Deprecated
	@Override
	public <E extends GE, V extends Comparable<?>> List<E> listByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue,
			EnabledFilter enabledFilter, Comparator<? super E> comparator) {
		Pair<EntityPath<E>, JPAQuery> queryItem = queryByField(clazz, field, fieldValue, enabledFilter);
		List<E> entities = queryItem.getValue1().list(queryItem.getValue0());
		Collections.sort(entities, comparator);
		
		return entities;
	}

	/**
	 * @deprecated Utiliser QueryDSL
	 */
	@Deprecated
	@Override
	public <E extends GE, V extends Comparable<?>> Long countByField(Class<E> clazz, SingularAttribute<? super E, V> attribute, V fieldValue) {
		return countByField(clazz, attribute, fieldValue, EnabledFilter.ALL);
	}

	/**
	 * @deprecated Utiliser QueryDSL
	 */
	@Deprecated
	@Override
	public <E extends GE, V extends Comparable<?>> Long countByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue,
			EnabledFilter enabledFilter) {
		Pair<EntityPath<E>, JPAQuery> queryItem = queryByField(clazz, field, fieldValue, enabledFilter);
		return queryItem.getValue1().distinct().count();
	}

	/**
	 * @deprecated Utiliser QueryDSL
	 */
	@Deprecated
	private <E extends GE, V extends Comparable<?>> Pair<EntityPath<E>, JPAQuery> queryByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue,
			EnabledFilter enabledFilter) {
		PathBuilder<E> pathBuilder = new PathBuilder<E>(clazz, "entityAlias");
		QGenericLocalizedGenericListItem entityPath = new QGenericLocalizedGenericListItem(pathBuilder);
		JPAQuery query;
		if (field != null) {
			query = queryEntityByField(entityPath, field.getBindableJavaType(), field.getName(), fieldValue);
		} else {
			query = queryByPredicate(entityPath, null);
		}
		if (EnabledFilter.ENABLED_ONLY.equals(enabledFilter)) {
			// l'appel au where ajoute la condition aux conditions précédentes
			query = query.where(entityPath.enabled.isTrue());
		}
		
		return Pair.with((EntityPath<E>) pathBuilder, query);
	}

	/**
	 * @deprecated Utiliser QueryDSL
	 */
	@Deprecated
	@Override
	public <E extends GE, V extends Comparable<?>> List<E> listEnabledByField(Class<E> clazz,
			SingularAttribute<? super E, V> field, V fieldValue, Comparator<? super E> comparator) {
		return listByField(clazz, field, fieldValue, EnabledFilter.ENABLED_ONLY);
	}

	/**
	 * @deprecated Utiliser QueryDSL
	 */
	@Deprecated
	@Override
	public <E extends GE, V extends Comparable<?>> Long countEnabledByField(Class<E> clazz,
			SingularAttribute<? super E, V> field, V fieldValue) {
		return countByField(clazz, field, fieldValue, EnabledFilter.ENABLED_ONLY);
	}

	@Override
	public <E extends GE> List<E> listByLocalizedLabel(EntityPath<E> source, Locale locale, String label) {
		JPQLQuery query = new JPAQuery(getEntityManager());
		
		QGenericLocalizedGenericListItem qLocalizedGenericListItem = new QGenericLocalizedGenericListItem(source);
		
		query.from(qLocalizedGenericListItem)
			.where(qLabelTextPath(qLocalizedGenericListItem, locale).eq(label));
		
		return query.list(source);
	}

	@Override
	public <E extends GE> List<E> searchAutocomplete(String searchPattern, Class<E> clazz,
			Locale locale, Integer limit, Integer offset) throws ServiceException {
		return searchAutocomplete(searchPattern, clazz, null, locale, limit, offset);
	}
	
	protected final <E extends GE> List<E> searchAutocomplete(String searchPattern, Class<E> clazz,
			String[] fields, Locale locale, Integer limit, Integer offset) throws ServiceException {
		GenericLocalizedGenericListItemBinding<E, T> binding = new GenericLocalizedGenericListItemBinding<E, T>();
		String localizedLabelBindingPath = binding.label().getPath();
		
		QueryBuilder queryBuilder = Search.getFullTextEntityManager(getEntityManager()).getSearchFactory().buildQueryBuilder()
				.forEntity(clazz).get();
		
		Query luceneQuery = queryBuilder.keyword().onField(binding.enabled().getPath()).matching(true).createQuery();
		
		String[] actualFields = fields;
		if (actualFields == null || actualFields.length == 0) {
			actualFields = new String[] {
					localizedLabelBindingPath + "." + labelTextAutocompleteFieldName(locale),
					binding.code().getPath()
			};
		}
		
		return hibernateSearchService.search(clazz,
				actualFields,
				LuceneUtils.getAutocompleteQuery(searchPattern),
				luceneQuery,
				limit, offset,
				new Sort(new SortField(localizedLabelBindingPath + "." + labelTextSortFieldName(locale), SortField.Type.STRING))
		);
	}
	
	protected abstract String labelTextSortFieldName(Locale locale);
	
	protected abstract String labelTextAutocompleteFieldName(Locale locale);
	
	protected abstract StringPath qLabelTextPath(QGenericLocalizedGenericListItem qLocalizedListItem, Locale locale);
}
