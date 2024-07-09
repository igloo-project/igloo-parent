package basicapp.back.business.announcement.service;

import java.time.LocalDateTime;
import java.util.List;

import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.transaction.annotation.Transactional;

import basicapp.back.business.announcement.model.Announcement;

public interface IAnnouncementService extends IGenericEntityService<Long, Announcement> {

	void saveAnnouncement(Announcement announcement) throws ServiceException, SecurityServiceException;

	@Transactional(readOnly = true)
	void cleanWithoutSaving(Announcement announcement);

	List<Announcement> listEnabled();

	LocalDateTime getMostRecentPublicationStartDate();

}
