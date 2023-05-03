package org.iglooproject.basicapp.core.business.announcement.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.iglooproject.basicapp.core.business.announcement.model.Announcement;
import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;

public interface IAnnouncementDao extends IGenericEntityDao<Long, Announcement> {

	List<Announcement> listEnabled();

	LocalDateTime getMostRecentPublicationStartDate();

}
