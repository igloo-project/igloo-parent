package org.iglooproject.basicapp.core.business.announcement.search;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.iglooproject.basicapp.core.business.announcement.model.Announcement;
import org.iglooproject.jpa.more.business.search.query.ISearchQuery;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class AnnouncementSearchQueryImpl implements IAnnouncementSearchQuery {

	protected AnnouncementSearchQueryImpl() {
		//TODO: igloo-boot
//		super(QAnnouncement.announcement);
	}

	@Override
	public ISearchQuery<Announcement, AnnouncementSort> sort(Map<AnnouncementSort, SortOrder> sortMap) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public List<Announcement> fullList() {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	@Override
	public List<Announcement> list(long offset, long limit) {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

}
