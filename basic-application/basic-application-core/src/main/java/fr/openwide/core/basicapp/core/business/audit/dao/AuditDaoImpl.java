package fr.openwide.core.basicapp.core.business.audit.dao;

import static fr.openwide.core.basicapp.core.util.search.BasicApplicationHibernateSearchUtils.matchIfGiven;
import static fr.openwide.core.basicapp.core.util.search.BasicApplicationHibernateSearchUtils.mustIfNotNull;

import java.util.List;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.DatabaseRetrievalMethod;
import org.hibernate.search.query.ObjectLookupMethod;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;

import fr.openwide.core.basicapp.core.business.audit.model.Audit;
import fr.openwide.core.basicapp.core.business.audit.model.search.AuditSearchParametersBean;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.audit.dao.AbstractAuditDaoImpl;
import fr.openwide.core.jpa.util.HibernateUtils;

@Repository("auditDao")
public class AuditDaoImpl extends AbstractAuditDaoImpl<Audit> implements IAuditDao {

//	private static final QAudit qAudit = QAudit.audit;

	private static final Sort SORT = new Sort(
			new SortField(Audit.DATE_SORT_FIELD_NAME, SortField.Type.STRING, true),
			new SortField(Bindings.audit().id().getPath(), SortField.Type.LONG, true)
	);
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Audit> search(AuditSearchParametersBean searchParams, Long limit, Long offset)
			throws ServiceException, SecurityServiceException {
		try {
			FullTextQuery fullTextQuery = getSearchQuery(searchParams);

			if (offset != null) {
				fullTextQuery.setFirstResult(offset.intValue());
			}
			if (limit != null) {
				fullTextQuery.setMaxResults(limit.intValue());
			}
			
			fullTextQuery.setSort(SORT);
			fullTextQuery.initializeObjectsWith(ObjectLookupMethod.SECOND_LEVEL_CACHE, DatabaseRetrievalMethod.QUERY);

			return fullTextQuery.getResultList();
		} catch (Exception e) {
			throw new ServiceException("Error while searching for audits", e);
		}
	}

	@Override
	public long count(AuditSearchParametersBean searchParams) throws ServiceException, SecurityServiceException {
		try {
			FullTextQuery fullTextQuery = getSearchQuery(searchParams);
			return fullTextQuery.getResultSize();
		} catch (Exception e) {
			throw new ServiceException("Error while counting audits", e);
		}
	}

	private FullTextQuery getSearchQuery(AuditSearchParametersBean searchParams) throws ServiceException {
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(getEntityManager());
		
		QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
				.forEntity(Audit.class).get();
		
		BooleanJunction<?> booleanJunction = queryBuilder.bool().must(queryBuilder.all().createQuery());
		
		mustIfNotNull(booleanJunction,
				matchIfGiven(queryBuilder,
						Bindings.audit().objectId(), searchParams.getUser().getId()),
				matchIfGiven(queryBuilder,
						Bindings.audit().objectClass(), HibernateUtils.getClass(searchParams.getUser()).getName())
		);
		
		return fullTextEntityManager.createFullTextQuery(booleanJunction.createQuery(), Audit.class);
	}

}
