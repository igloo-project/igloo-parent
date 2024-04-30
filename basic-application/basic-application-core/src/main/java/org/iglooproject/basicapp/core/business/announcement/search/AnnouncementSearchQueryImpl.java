package org.iglooproject.basicapp.core.business.announcement.search;

import java.util.List;
import java.util.Map;

import org.iglooproject.basicapp.core.business.announcement.model.Announcement;
import org.iglooproject.basicapp.core.business.announcement.model.QAnnouncement;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;
import org.springframework.stereotype.Service;

import com.querydsl.jpa.impl.JPAQuery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class AnnouncementSearchQueryImpl implements IAnnouncementSearchQuery {

	private static final QAnnouncement qAnnouncement = QAnnouncement.announcement;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Announcement> list(AnnouncementSearchQueryData data, Map<AnnouncementSort, SortOrder> sorts, Integer offset, Integer limit) {
		if (!checkLimit(limit)) {
			return List.of();
		}
		
		JPAQuery<Announcement> query = new JPAQuery<>(entityManager)
			.select(qAnnouncement)
			.from(qAnnouncement);
		
		predicateContributor(query, data);
		sortContributor(query, sorts);
		hitsContributor(query, offset, limit);
		
		return query.fetch();
	}

	@Override
	public long size(AnnouncementSearchQueryData data) {
		JPAQuery<Announcement> query = new JPAQuery<>(entityManager)
			.select(qAnnouncement)
			.from(qAnnouncement);
		
		predicateContributor(query, data);
		
		@SuppressWarnings("deprecation")
		long size = query.fetchCount();
		
		return size;
	}

	private void predicateContributor(JPAQuery<Announcement> query, AnnouncementSearchQueryData data) {
		// nothing to do
	}

}
