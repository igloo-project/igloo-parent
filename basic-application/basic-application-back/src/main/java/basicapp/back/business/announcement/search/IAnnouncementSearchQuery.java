package basicapp.back.business.announcement.search;

import basicapp.back.business.announcement.model.Announcement;
import org.iglooproject.jpa.more.search.query.IJpaSearchQuery;

public interface IAnnouncementSearchQuery
    extends IJpaSearchQuery<Announcement, AnnouncementSort, AnnouncementSearchQueryData> {}
