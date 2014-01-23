package fr.openwide.core.jpa.more.business.link.dao;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.StringExpression;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.jpa.more.business.link.model.ExternalLinkStatus;
import fr.openwide.core.jpa.more.business.link.model.ExternalLinkWrapper;
import fr.openwide.core.jpa.more.business.link.model.QExternalLinkWrapper;

@Service("externalLinkWrapperDao")
public class ExternalLinkWrapperDaoImpl extends GenericEntityDaoImpl<Long, ExternalLinkWrapper> implements IExternalLinkWrapperDao {
	private static final QExternalLinkWrapper qExternalLinkWrapper = QExternalLinkWrapper.externalLinkWrapper;
	
	@Override
	public List<ExternalLinkWrapper> listByIds(Collection<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return Lists.newArrayList();
		}
		
		return new JPAQuery(getEntityManager()).from(qExternalLinkWrapper)
				.where(qExternalLinkWrapper.id.in(ids))
				.orderBy(qExternalLinkWrapper.id.asc())
				.list(qExternalLinkWrapper);
	}

	@Override
	public List<ExternalLinkWrapper> listActive() {
		JPQLQuery query = new JPAQuery(getEntityManager());
		
		query.from(qExternalLinkWrapper)
				.where(qExternalLinkWrapper.status.notIn(ExternalLinkStatus.DEAD_LINK, ExternalLinkStatus.DELETED))
				.orderBy(qExternalLinkWrapper.url.lower().asc());
		
		return query.list(qExternalLinkWrapper);
	}

	@Override
	public Multimap<String, ExternalLinkWrapper> listNextCheckingBatch(int batchSize) {
		JPQLQuery query = new JPAQuery(getEntityManager());
		
		// Query to list the next <batchsize> URLs
		// Must be a separate query, not a subquery, since JPQL doesn't support limit in subqueries
		List<String> nextUrlsToCheckSubquery = listNextCheckingBatchUrls(batchSize);
		
		// Query to list the matching linkwrappers (may be more than <batchsize>)
		query.from(qExternalLinkWrapper)
				.where(qExternalLinkWrapper.url.lower().in(nextUrlsToCheckSubquery))
				// Only links with the following statuses are to be checked
				.where(qExternalLinkWrapper.status.notIn(ExternalLinkStatus.DEAD_LINK, ExternalLinkStatus.DELETED))
				.orderBy(qExternalLinkWrapper.id.asc());
		
		return Multimaps.index(query.list(qExternalLinkWrapper), new Function<ExternalLinkWrapper, String>() {
			@Override
			public String apply(ExternalLinkWrapper input) {
				return input.getUrl().toLowerCase(Locale.ROOT);
			}
		});
	}
	
	private List<String> listNextCheckingBatchUrls(int batchSize) {
		JPQLQuery query = new JPAQuery(getEntityManager());
		
		StringExpression url = qExternalLinkWrapper.url.lower();
		
		query.from(qExternalLinkWrapper)
				// Only links with the following statuses are to be checked
				.where(qExternalLinkWrapper.status.notIn(ExternalLinkStatus.DEAD_LINK, ExternalLinkStatus.DELETED))
				.groupBy(url)
				.orderBy(
						// NOTE: if, for a given URL, one linkWrapper has a null lastCheckDate and the other has a non-null lastCheckDate, the null one is ignored.
						// This is not really on purpose, but this does not impair functionality and the implementation is simpler that way.
						qExternalLinkWrapper.lastCheckDate.max().asc().nullsFirst()
						, url.asc()
				);
		
		return query.limit(batchSize).list(url);
	}

}