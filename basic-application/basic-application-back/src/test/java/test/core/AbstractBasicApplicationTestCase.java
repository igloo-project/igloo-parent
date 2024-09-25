package test.core;

import basicapp.back.business.announcement.service.business.IAnnouncementService;
import basicapp.back.business.history.service.IHistoryLogService;
import basicapp.back.business.referencedata.service.ICityService;
import basicapp.back.business.role.service.IRoleService;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.service.business.IUserService;
import java.util.Set;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.referencedata.model.GenericReferenceData;
import org.iglooproject.jpa.more.business.referencedata.service.IGenericReferenceDataSubService;
import org.iglooproject.jpa.more.business.upgrade.service.IDataUpgradeRecordService;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.test.jpa.junit.AbstractTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class AbstractBasicApplicationTestCase extends AbstractTestCase {

  @Autowired protected IUserService userService;

  @Autowired protected ICityService cityService;

  @Autowired protected IPropertyService propertyService;

  @Autowired protected IDataUpgradeRecordService dataUpgradeRecordService;

  @Autowired private IMutablePropertyDao mutablePropertyDao;

  @Autowired private IHistoryLogService historyLogService;

  @Autowired protected IRoleService roleService;

  @Autowired protected IAnnouncementService announcementService;

  @Autowired protected PasswordEncoder passwordEncoder;

  @Autowired protected TestEntityDatabaseHelper entityDatabaseHelper;

  @Autowired protected PermissionFactory permissionFactory;

  @Autowired
  @Qualifier("authenticationManager")
  protected AuthenticationManager authenticationManager;

  @BeforeEach
  @Override
  public void init() throws ServiceException, SecurityServiceException {
    super.init();
  }

  @Override
  protected void cleanAll() throws ServiceException, SecurityServiceException {
    cleanEntities(historyLogService);
    cleanEntities(userService);
    cleanEntities(dataUpgradeRecordService);
    cleanEntities(roleService);
    cleanEntities(announcementService);

    mutablePropertyDao.cleanInTransaction();
  }

  protected static <E extends GenericReferenceData<?, ?>> void cleanReferenceData(
      IGenericReferenceDataSubService service, Class<E> clazz)
      throws ServiceException, SecurityServiceException {
    for (E entity : service.list(clazz)) {
      service.delete(entity);
    }
  }

  protected User addPermissions(User user, String... permissions)
      throws ServiceException, SecurityServiceException {
    user.addRole(entityDatabaseHelper.createRole(r -> r.setPermissions(Set.of(permissions)), true));
    userService.update(user);
    return user;
  }
}
