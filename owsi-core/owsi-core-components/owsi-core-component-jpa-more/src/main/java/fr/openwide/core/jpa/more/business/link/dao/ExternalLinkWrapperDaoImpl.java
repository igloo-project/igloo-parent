package fr.openwide.core.jpa.more.business.link.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.jpa.more.business.link.model.ExternalLinkStatus;
import fr.openwide.core.jpa.more.business.link.model.ExternalLinkWrapper;
import fr.openwide.core.jpa.more.business.link.model.QExternalLinkWrapper;

@Service("externalLinkWrapperDao")
public class ExternalLinkWrapperDaoImpl extends GenericEntityDaoImpl<Long, ExternalLinkWrapper> implements IExternalLinkWrapperDao {

	@Override
	public List<ExternalLinkWrapper> listActive() {
		QExternalLinkWrapper qExternalLinkWrapper = QExternalLinkWrapper.externalLinkWrapper;
		
		JPQLQuery query = new JPAQuery(getEntityManager());
		
		query.from(qExternalLinkWrapper)
				.where(qExternalLinkWrapper.status.notIn(Lists.newArrayList(ExternalLinkStatus.DEAD_LINK)))
				.orderBy(qExternalLinkWrapper.url.lower().asc());
		
		return query.list(qExternalLinkWrapper);
	}

}