package org.iglooproject.basicapp.core.business.announcement.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.iglooproject.basicapp.core.business.announcement.model.Announcement;
import org.iglooproject.basicapp.core.business.announcement.model.QAnnouncement;
import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQuery;

@Repository
public class AnnouncementDaoImpl extends GenericEntityDaoImpl<Long, Announcement> implements IAnnouncementDao {

	private QAnnouncement qAnnouncement = QAnnouncement.announcement;

	@Override
	public List<Announcement> listEnabled() {
		LocalDateTime now = LocalDateTime.now();
		return new JPAQuery<>(getEntityManager())
			.select(qAnnouncement)
			.from(qAnnouncement)
			.where(qAnnouncement.publication.startDateTime.loe(now))
			.where(qAnnouncement.publication.endDateTime.goe(now))
			.where(qAnnouncement.enabled.isTrue())
			.orderBy(qAnnouncement.publication.startDateTime.asc())
			.fetch();
	}

	@Override
	public LocalDateTime getMostRecentPublicationStartDate() {
		LocalDateTime now = LocalDateTime.now();
		return new JPAQuery<>(getEntityManager())
			.select(qAnnouncement.publication.startDateTime)
			.from(qAnnouncement)
			.where(qAnnouncement.publication.startDateTime.loe(now))
			.where(qAnnouncement.publication.endDateTime.goe(now))
			.where(qAnnouncement.enabled.isTrue())
			.orderBy(qAnnouncement.publication.startDateTime.desc())
			.fetchFirst();
	}

}
