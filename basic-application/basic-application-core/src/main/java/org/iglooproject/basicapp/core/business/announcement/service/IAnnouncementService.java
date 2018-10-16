package org.iglooproject.basicapp.core.business.announcement.service;

import java.util.Date;
import java.util.List;

import org.iglooproject.basicapp.core.business.announcement.model.Announcement;
import org.iglooproject.jpa.business.generic.service.IGenericEntityService;

public interface IAnnouncementService extends IGenericEntityService<Long, Announcement> {

	List<Announcement> listActive();

	Date getMostRecentPublicationStartDate();

}
