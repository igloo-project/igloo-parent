package basicapp.back.business.announcement.search;

import basicapp.back.business.announcement.model.Announcement;
import org.bindgen.Bindable;
import org.iglooproject.jpa.more.search.query.ISearchQueryData;

@Bindable
public class AnnouncementSearchQueryData implements ISearchQueryData<Announcement> {}
