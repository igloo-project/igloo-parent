package basicapp.back.business.announcement.repository;

import basicapp.back.business.announcement.model.Announcement;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface IAnnouncementRepository
    extends JpaRepository<Announcement, Long>, JpaSpecificationExecutor<Announcement> {

  @Query(
      """
      SELECT a FROM Announcement a
      WHERE a.enabled IS TRUE
            AND a.publication.startDateTime <= local_datetime
            AND a.publication.endDateTime >= local_datetime
      ORDER BY a.publication.startDateTime ASC
      """)
  List<Announcement> findAllEnabled();

  @Query(
      """
      SELECT a.publication.startDateTime FROM Announcement a
      WHERE a.enabled IS TRUE
            AND a.publication.startDateTime <= local_datetime
            AND a.publication.endDateTime >= local_datetime
      ORDER BY a.publication.startDateTime DESC
      LIMIT 1
      """)
  LocalDateTime getMostRecentPublicationStartDate();
}
