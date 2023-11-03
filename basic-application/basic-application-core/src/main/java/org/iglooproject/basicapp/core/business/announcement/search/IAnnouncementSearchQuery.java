package org.iglooproject.basicapp.core.business.announcement.search;

import org.iglooproject.basicapp.core.business.announcement.model.Announcement;
import org.iglooproject.jpa.more.search.query.IJpaSearchQuery;

public interface IAnnouncementSearchQuery extends IJpaSearchQuery<Announcement, AnnouncementSort, AnnouncementSearchQueryData> {

}
