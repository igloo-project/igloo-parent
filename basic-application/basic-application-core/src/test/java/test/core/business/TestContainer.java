//package test.core.business;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import java.util.List;
//
//import org.iglooproject.basicapp.core.business.user.model.User;
//import org.iglooproject.jpa.exception.SecurityServiceException;
//import org.iglooproject.jpa.exception.ServiceException;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import test.core.AbstractBasicApplicationTestCase;
//import test.core.config.spring.SpringBootTestBasicApplication;
//
//
//@Testcontainers
//@SpringBootTestBasicApplication
//class TestContainer extends AbstractBasicApplicationTestCase {
//
//	@Container
//	static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.12")
//		.withDatabaseName("test")
//		.withUsername("root")
//		.withPassword("root");
//	
//	
//	// lancé une seule fois pour la class de test
////	@Container
////	@ServiceConnection
////	static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.2-alpine");
//
//	@DynamicPropertySource
//	static void registerProperties(DynamicPropertyRegistry registry) {
//		registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
//		registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
//		registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
//	}
//
//	@Disabled
//	@Test
//	void testUser() throws ServiceException, SecurityServiceException {
//
//		{
//			User user = new User();
//			user.setUsername("test");
//			user.setFirstName("firstname");
//			user.setLastName("lastname");
//
//			userService.create(user);
//		}
//
//		List<User> userList = userService.list();
//
//		assertEquals(1, userList.size());
//		assertEquals("test", userList.get(0).getUsername());
//		assertEquals("firstname", userList.get(0).getFirstName());
//		assertEquals("lastname", userList.get(0).getLastName());
//	}
//
//	@Disabled
//	@Test
//	void testUser2() throws ServiceException, SecurityServiceException {
//
//		{
//			User user = new User();
//			user.setUsername("test");
//			user.setFirstName("firstname");
//			user.setLastName("lastname");
//
//			userService.create(user);
//		}
//
//		List<User> userList = userService.list();
//
//		assertEquals(1, userList.size());
//		assertEquals("test", userList.get(0).getUsername());
//		assertEquals("firstname", userList.get(0).getFirstName());
//		assertEquals("lastname", userList.get(0).getLastName());
//	}
//}
