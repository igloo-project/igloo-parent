package fr.openwide.core.jpa.more.business.generic.dao;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.path.StringPath;

import fr.openwide.core.jpa.business.generic.dao.JpaDaoSupport;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.generic.model.EnabledFilter;
import fr.openwide.core.jpa.more.business.generic.model.GenericLocalizedGenericListItem;
import fr.openwide.core.jpa.more.business.generic.model.GenericLocalizedGenericListItemBinding;
import fr.openwide.core.jpa.more.business.generic.model.GenericLocalizedGenericListItem_;
import fr.openwide.core.jpa.more.business.generic.model.QGenericLocalizedGenericListItem;
import fr.openwide.core.jpa.more.business.localization.model.AbstractLocalizedText;
import fr.openwide.core.jpa.search.service.IHibernateSearchService;
import fr.openwide.core.spring.util.lucene.search.LuceneUtils;

public abstract class GenericLocalizedGenericListItemDaoImpl<GE extends GenericLocalizedGenericListItem<?, T>, T extends AbstractLocalizedText>
		extends JpaDaoSupport implements IGenericLocalizedGenericListItemDao<GE, T> {
	
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
	public <E extends GE> E getByNaturalId(Class<E> clazz, String naturalId) {
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
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<E> criteria = builder.createQuery(clazz);
		Root<E> root = rootCriteriaQuery(builder, criteria, clazz);
		if (EnabledFilter.ENABLED_ONLY.equals(enabledFilter)) {
			criteria.where(builder.equal(root.get(GenericLocalizedGenericListItem_.enabled), true));
		}
		
		List<E> entities = buildTypedQuery(criteria, null, null).getResultList();
		
		Collections.sort(entities, comparator);
		
		return entities;
	}
	
	@Override
	public <E extends GE> Long count(Class<E> clazz) {
		return count(clazz, EnabledFilter.ALL);
	}
	
	@Override
	public <E extends GE> Long count(Class<E> clazz, EnabledFilter enabledFilter) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		
		Root<E> root = rootCriteriaQuery(builder, criteria, clazz);
		criteria.select(builder.count(root));
		
		if (EnabledFilter.ENABLED_ONLY.equals(enabledFilter)) {
			filterCriteriaQuery(criteria, builder.equal(root.get(GenericLocalizedGenericListItem_.enabled), true));
		}
		
		return buildTypedQuery(criteria, null, null).getSingleResult();
	}
	
	@Override
	public <E extends GE,V> E getByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue) {
		return super.getEntityByField(clazz, field, fieldValue);
	}
	
	@Override
	public <E extends GE,V> List<E> listByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue) {
		return listByField(clazz, field, fieldValue, EnabledFilter.ALL, null);
	}
	
	@Override
	public <E extends GE, V> List<E> listByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue,
			EnabledFilter enabledFilter) {
		return listByField(clazz, field, fieldValue, enabledFilter, null);
	}
	
	@Override
	public <E extends GE, V> List<E> listByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue,
			EnabledFilter enabledFilter, Comparator<? super E> comparator) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<E> criteria = builder.createQuery(clazz);
		Root<E> root = rootCriteriaQuery(builder, criteria, clazz);
		filterCriteriaQuery(criteria, builder.equal(root.get(field), fieldValue));
		if (EnabledFilter.ENABLED_ONLY.equals(enabledFilter)) {
			filterCriteriaQuery(criteria, builder.equal(root.get(GenericLocalizedGenericListItem_.enabled), true));
		}
		
		List<E> entities = buildTypedQuery(criteria, null, null).getResultList();
		
		Collections.sort(entities, comparator);
		
		return entities;
	}
	
	@Override
	public <E extends GE, V> Long countByField(Class<E> clazz, SingularAttribute<? super E, V> attribute, V fieldValue) {
		return countByField(clazz, attribute, fieldValue, EnabledFilter.ALL);
	}
	
	@Override
	public <E extends GE, V> Long countByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue,
			EnabledFilter enabledFilter) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		
		Root<E> root = rootCriteriaQuery(builder, criteria, clazz);
		criteria.select(builder.count(root));
		
		filterCriteriaQuery(criteria, builder.equal(root.get(field), fieldValue));
		if (EnabledFilter.ENABLED_ONLY.equals(enabledFilter)) {
			filterCriteriaQuery(criteria, builder.equal(root.get(GenericLocalizedGenericListItem_.enabled), true));
		}
		
		return buildTypedQuery(criteria, null, null).getSingleResult();
	}

	@Override
	public <E extends GE, V> List<E> listEnabledByField(Class<E> clazz,
			SingularAttribute<? super E, V> field, V fieldValue, Comparator<? super E> comparator) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<E> criteria = builder.createQuery(clazz);
		
		Root<E> root = rootCriteriaQuery(builder, criteria, clazz);
		filterCriteriaQuery(criteria, builder.and(
				builder.equal(root.get(GenericLocalizedGenericListItem_.enabled), true),
				builder.equal(root.get(field), fieldValue)
				));
		
		List<E> entities = buildTypedQuery(criteria, null, null).getResultList();
		
		Collections.sort(entities, comparator);
		
		return entities;
	}

	@Override
	public <E extends GE, V> Long countEnabledByField(Class<E> clazz,
			SingularAttribute<? super E, V> field, V fieldValue) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		
		Root<E> root = rootCriteriaQuery(builder, criteria, clazz);
		criteria.select(builder.count(root));
		
		Expression<Boolean> filter = builder.and(
				builder.equal(root.get(GenericLocalizedGenericListItem_.enabled), true),
				builder.equal(root.get(field), fieldValue)
				);
		filterCriteriaQuery(criteria, filter);
		
		return buildTypedQuery(criteria, null, null).getSingleResult();
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
	
	private <E extends GE> List<E> searchAutocomplete(String searchPattern, Class<E> clazz,
			String[] fields, Locale locale, Integer limit, Integer offset) throws ServiceException {
		GenericLocalizedGenericListItemBinding<E, T> binding = new GenericLocalizedGenericListItemBinding<E, T>();
		String localizedLabelBindingPath = binding.label().getPath();
		
		QueryBuilder queryBuilder = Search.getFullTextEntityManager(getEntityManager()).getSearchFactory().buildQueryBuilder()
				.forEntity(clazz).get();
		
		Query luceneQuery = queryBuilder.keyword().onField(binding.enabled().getPath()).matching(true).createQuery();
		
		String[] actualFields = fields;
		if (actualFields == null || actualFields.length == 0) {
			actualFields = new String[] { localizedLabelBindingPath + "." + labelTextAutocompleteFieldName(locale) };
		}
		
		return hibernateSearchService.search(clazz,
				actualFields,
				LuceneUtils.getAutocompleteQuery(searchPattern),
				luceneQuery,
				limit, offset,
				new Sort(new SortField(localizedLabelBindingPath + "." + labelTextSortFieldName(locale), SortField.STRING))
		);
	}
	
	protected abstract String labelTextSortFieldName(Locale locale);
	
	protected abstract String labelTextAutocompleteFieldName(Locale locale);
	
	protected abstract StringPath qLabelTextPath(QGenericLocalizedGenericListItem qLocalizedListItem, Locale locale);
}
