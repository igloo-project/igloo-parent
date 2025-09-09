package basicapp.back.business.announcement.search;

import basicapp.back.business.announcement.model.Announcement;
import org.iglooproject.jpa.more.search.query.ISearchQuery;

public interface IAnnouncementSearchQuery
    extends ISearchQuery<Announcement, AnnouncementSort, AnnouncementSearchQueryData> {}
