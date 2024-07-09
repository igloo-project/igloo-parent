package basicapp.back.business.announcement.search;

import org.iglooproject.jpa.more.search.query.IJpaSearchQuery;

import basicapp.back.business.announcement.model.Announcement;

public interface IAnnouncementSearchQuery extends IJpaSearchQuery<Announcement, AnnouncementSort, AnnouncementSearchQueryData> {

}
