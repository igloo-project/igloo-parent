package basicapp.back.business.announcement.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;

import basicapp.back.business.announcement.model.Announcement;

public interface IAnnouncementDao extends IGenericEntityDao<Long, Announcement> {

	List<Announcement> listEnabled();

	LocalDateTime getMostRecentPublicationStartDate();

}
