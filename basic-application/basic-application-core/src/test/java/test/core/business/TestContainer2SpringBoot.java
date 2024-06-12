package test.core.business;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import test.core.AbstractBasicApplicationTestCase;
import test.core.PSQLTestContainerConfiguration;
import test.core.config.spring.SpringBootTestBasicApplication;


@SpringBootTestBasicApplication
@Import(PSQLTestContainerConfiguration.class)
class TestContainer2SpringBoot extends AbstractBasicApplicationTestCase {
	
	@Autowired
	IUserService userService;
	
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
