package org.iglooproject.basicapp.core.business.announcement.search;

import org.iglooproject.basicapp.core.business.announcement.model.Announcement;
import org.iglooproject.jpa.more.business.search.query.ISearchQuery;

public interface IAnnouncementSearchQuery extends ISearchQuery<Announcement, AnnouncementSort> {}
