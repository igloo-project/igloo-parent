package test.core.business;

import static org.junit.jupiter.api.Assertions.assertEquals;

import basicapp.back.business.user.model.User;
import java.util.List;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.junit.jupiter.api.Test;
import test.core.AbstractBasicApplicationTestCase;
import test.core.config.spring.SpringBootTestBasicApplication;

@SpringBootTestBasicApplication
class TestUserService extends AbstractBasicApplicationTestCase {

  @Test
  void testUser() throws ServiceException, SecurityServiceException {

    {
      User user = new User();
      user.setUsername("test");
      user.setFirstName("firstname");
      user.setLastName("lastname");

      userService.create(user);
    }

    List<User> userList = userService.list();

    assertEquals(1, userList.size());
    assertEquals("test", userList.get(0).getUsername());
    assertEquals("firstname", userList.get(0).getFirstName());
    assertEquals("lastname", userList.get(0).getLastName());
  }
}
