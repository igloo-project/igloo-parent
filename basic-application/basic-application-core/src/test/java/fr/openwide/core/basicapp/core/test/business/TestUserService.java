package fr.openwide.core.basicapp.core.test.business;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.test.AbstractBasicApplicationTestCase;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;

public class TestUserService extends AbstractBasicApplicationTestCase {

	@Test
	public void testUser() throws ServiceException, SecurityServiceException {
		
		{
			User user = new User();
			user.setUserName("test");
			user.setFirstName("firstname");
			user.setLastName("lastname");
			
			userService.create(user);
		}
		
		List<User> userList = userService.list();
		
		assertEquals(1, userList.size());
		assertEquals("test", userList.get(0).getUserName());
		assertEquals("firstname", userList.get(0).getFirstName());
		assertEquals("lastname", userList.get(0).getLastName());
	}
}
