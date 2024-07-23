package org.iglooproject.basicapp.core.business.announcement.search;

import org.iglooproject.basicapp.core.business.announcement.model.Announcement;
import org.iglooproject.basicapp.core.business.announcement.model.QAnnouncement;
import org.iglooproject.jpa.more.business.search.query.AbstractJpaSearchQuery;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class AnnouncementSearchQueryImpl
    extends AbstractJpaSearchQuery<Announcement, AnnouncementSort>
    implements IAnnouncementSearchQuery {

  protected AnnouncementSearchQueryImpl() {
    super(QAnnouncement.announcement);
  }
}
