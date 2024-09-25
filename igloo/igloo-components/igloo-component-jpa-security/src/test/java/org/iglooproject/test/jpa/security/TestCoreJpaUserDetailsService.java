package org.iglooproject.test.jpa.security;

import org.assertj.core.api.Assertions;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.AbstractJpaSecurityTestCase;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;
import org.iglooproject.test.jpa.security.config.spring.SpringBootTestJpaSecurity;
import org.junit.jupiter.api.Test;

@SpringBootTestJpaSecurity
class TestCoreJpaUserDetailsService extends AbstractJpaSecurityTestCase {

  @Test
  void testCountUserEnable() throws ServiceException, SecurityServiceException {

    createMockTechnicalUser();
    createMockBasicUser();
    createMockUser("newUser", "newUser", "newUser");

    // Test reimplemented QueryDSL methods
    MockUser userInactive = createMockUser("inactive", "inactive", "inactive");
    userInactive.setEnabled(false);
    mockUserService.update(userInactive);

    Assertions.assertThat(mockUserService.count()).isEqualTo(4);
    Assertions.assertThat(mockUserService.countEnabled()).isEqualTo(3);
  }
}
