package org.iglooproject.basicapp.core.business.message.dao;

import java.util.Date;
import java.util.List;

import org.iglooproject.basicapp.core.business.message.model.GeneralMessage;
import org.iglooproject.basicapp.core.business.message.model.QGeneralMessage;
import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQuery;

@Repository
public class GeneralMessageDaoImpl extends GenericEntityDaoImpl<Long, GeneralMessage> implements IGeneralMessageDao {

	private QGeneralMessage qGeneralMessage = QGeneralMessage.generalMessage;

	@Override
	public List<GeneralMessage> listActiveMessages() {
		Date now = new Date();
		return new JPAQuery<>(getEntityManager())
				.select(qGeneralMessage)
				.from(qGeneralMessage)
				.where(qGeneralMessage.publication.startDateTime.before(now))
				.where(qGeneralMessage.publication.endDateTime.after(now))
				.where(qGeneralMessage.active.eq(true))
				.orderBy(qGeneralMessage.publication.startDateTime.asc())
				.fetch();
	}

	@Override
	public Date getMostRecentPublicationStartDate() {
		Date now = new Date();
		return new JPAQuery<>(getEntityManager())
				.select(qGeneralMessage.publication.startDateTime)
				.from(qGeneralMessage)
				.where(qGeneralMessage.publication.startDateTime.before(now))
				.where(qGeneralMessage.publication.endDateTime.after(now))
				.where(qGeneralMessage.active.eq(true))
				.orderBy(qGeneralMessage.publication.startDateTime.desc())
				.fetchFirst();
	}
}
