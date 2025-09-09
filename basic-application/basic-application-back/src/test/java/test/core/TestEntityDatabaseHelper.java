package test.core;

import basicapp.back.business.announcement.model.Announcement;
import basicapp.back.business.announcement.model.atomic.AnnouncementType;
import basicapp.back.business.announcement.repository.IAnnouncementRepository;
import basicapp.back.business.common.model.EmailAddress;
import basicapp.back.business.history.service.IHistoryEventSummaryService;
import basicapp.back.business.role.model.Role;
import basicapp.back.business.role.repository.IRoleRepository;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.business.user.repository.IUserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

/**
 * This common class is intended to be used to set up the test data in the case of using BDD.<br>
 * Only one method per entity must be created, but it has to respect the following structure:
 *
 * <ul>
 *   <li>Method naming type{@code Entity createEntite(Consumer<Entity>, boolean database)}
 *   <li>parameter consumer to modify entity
 *   <li>parameter boolean <b>database</b> to indicates if the entity must be inserted in the
 *       database
 * </ul>
 *
 * the following order must be followed :
 *
 * <ol>
 *   <li>Setup of <b>mandatory</b> fields (<b>Warning</b> : do not take into account foreign keys)
 *   <li>In unique keys case, add a UUID to the field
 *   <li>Apply the consumer if not null
 *   <li>Setup if necessary null <b>mandatory</b> foreign keys by passing a null consumer as a
 *       parameter and a cascade call of the BDD boolean
 *   <li>Call the service method {@code entiteService.create(entite)} if the database boolean is
 *       <b>true</b>
 *   <li>Return the entity
 * </ol>
 *
 * <i>Note : methods are sorted in alphabetical order</i>
 */
@Transactional
public class TestEntityDatabaseHelper {

  public static final String USER_PASSWORD = "USER_PASSWORD";

  @Autowired private IAnnouncementRepository announcementService;

  @Autowired private IUserRepository userRepository;

  @Autowired private IRoleRepository roleRepository;

  @Autowired protected PasswordEncoder passwordEncoder;

  @Autowired protected IHistoryEventSummaryService historyEventSummaryService;

  private static int uniqueToken = 0;

  public Announcement createAnnouncement(
      Consumer<Announcement> announcementConsumer, boolean database) {

    Announcement announcement = new Announcement();
    announcement.setType(AnnouncementType.SERVICE_INTERRUPTION);
    announcement.getInterruption().setStartDateTime(LocalDateTime.of(2024, 1, 1, 10, 0));
    announcement.getInterruption().setEndDateTime(LocalDateTime.of(2024, 1, 2, 10, 0));
    announcement.getPublication().setStartDateTime(LocalDateTime.of(2024, 1, 1, 10, 0));
    announcement.getPublication().setEndDateTime(LocalDateTime.of(2024, 1, 2, 10, 0));

    Optional.ofNullable(announcementConsumer).ifPresent(consumer -> consumer.accept(announcement));

    if (database) {
      historyEventSummaryService.refresh(announcement.getCreation());
      historyEventSummaryService.refresh(announcement.getModification());
      announcementService.save(announcement);
    }

    return announcement;
  }

  public Role createRole(Consumer<Role> roleConsumer, boolean database) {

    Role role = new Role();
    role.setTitle("role" + (++uniqueToken));

    Optional.ofNullable(roleConsumer).ifPresent(consumer -> consumer.accept(role));

    if (database) {
      roleRepository.save(role);
    }

    return role;
  }

  public User createUser(Consumer<User> userConsumer, boolean database) {

    User user = new User();
    user.setType(UserType.TECHNICAL);
    String token = "user" + (++uniqueToken);
    user.setEnabled(true);
    user.setUsername(token);
    user.setFirstName(token);
    user.setLastName(token);
    user.setEmailAddress(new EmailAddress(token + "@kobalt.fr"));
    user.setPasswordHash(passwordEncoder.encode(USER_PASSWORD));

    Optional.ofNullable(userConsumer).ifPresent(consumer -> consumer.accept(user));

    if (database) {
      historyEventSummaryService.refresh(user.getCreation());
      historyEventSummaryService.refresh(user.getModification());
      userRepository.save(user);
    }

    return user;
  }
}
