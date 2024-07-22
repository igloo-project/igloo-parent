package test.core;

import basicapp.back.business.history.service.IHistoryLogService;
import basicapp.back.business.referencedata.service.ICityService;
import basicapp.back.business.user.service.IUserGroupService;
import basicapp.back.business.user.service.IUserService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.upgrade.service.IDataUpgradeRecordService;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.jpa.security.business.authority.service.IAuthorityService;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.test.jpa.junit.AbstractTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractBasicApplicationTestCase extends AbstractTestCase {

  @Autowired protected IUserService userService;

  @Autowired protected ICityService cityService;

  @Autowired protected IUserGroupService userGroupService;

  @Autowired protected IAuthorityService authorityService;

  @Autowired protected IPropertyService propertyService;

  @Autowired protected IDataUpgradeRecordService dataUpgradeRecordService;

  @Autowired private IMutablePropertyDao mutablePropertyDao;

  @Autowired private IHistoryLogService historyLogService;

  @BeforeEach
  @Override
  public void init() throws ServiceException, SecurityServiceException {
    super.init();
    initAuthorities();
  }

  @Override
  protected void cleanAll() throws ServiceException, SecurityServiceException {
    cleanEntities(historyLogService);
    cleanEntities(userService);
    cleanEntities(userGroupService);
    cleanEntities(authorityService);
    cleanEntities(dataUpgradeRecordService);
    mutablePropertyDao.cleanInTransaction();
  }

  private void initAuthorities() throws ServiceException, SecurityServiceException {
    authorityService.create(new Authority(CoreAuthorityConstants.ROLE_AUTHENTICATED));
  }
}
