package test.web.selenium;

import static test.web.property.SeleniumPropertyIds.GECKODRIVER_PATH;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.EntityType;

import org.eclipse.jetty.server.Server;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.iglooproject.basicapp.core.business.history.service.IHistoryLogService;
import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.basicapp.core.business.user.service.IUserGroupService;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.basicapp.core.business.user.typedescriptor.UserTypeDescriptor;
import org.iglooproject.basicapp.core.security.model.BasicApplicationAuthorityConstants;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.search.service.IHibernateSearchService;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.jpa.security.business.authority.service.IAuthorityService;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.jpa.util.EntityManagerUtils;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.context.ApplicationContextUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import com.google.common.collect.ImmutableSet;

import test.web.spring.EntityManagerSeleniumExecutionListener;
import test.web.spring.JettyTestExecutionListener;
/**
 * Injection pour initialiser la base de données --> 19/12/2018 : apparemment ne servirai pas ???
 */
//@ContextConfiguration(classes = BasicApplicationWebappTestCommonConfig.class)
/**
 * Utilisé pour initialisé le serveur Jetty avant l'initialisation du contexte Spring et qu'il soit encapsulé
 */
@TestExecutionListeners(listeners = { JettyTestExecutionListener.class, EntityManagerSeleniumExecutionListener.class })
public class AbstractSeleniumTestCase {

	/**
	 * Use this instead of SpringJUnit4ClassRunner, so that implementors can choose their own runner
	 */
	@ClassRule
	public static final SpringClassRule SCR = new SpringClassRule();
	/**
	 * Use this instead of SpringJUnit4ClassRunner, so that implementors can choose their own runner
	 */
	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();

	protected static final String USER_PASSWORD = "kobalt";

	protected UserGroup users;

	protected UserGroup administrators;

	protected BasicUser basicUser;

	protected TechnicalUser administrator;

	protected String rootUrl = "http://localhost:8090/";

	protected WebDriver driver;

	protected Server server;

	@Autowired
	protected IUserService userService;

	@Autowired
	protected IUserGroupService userGroupService;

	@Autowired
	protected IAuthorityService authorityService;

	@Autowired
	protected EntityManagerUtils entityManagerUtils;

	@Autowired
	private IHibernateSearchService hibernateSearchService;

	@Autowired
	private IPropertyService propertyService;

	@Autowired
	protected IHistoryLogService historyLogService;

	@Before
	public void init() throws ServiceException, SecurityServiceException {
		// Nécessaire pour l'injection des bean
		ApplicationContextUtils.getInstance().getContext().getAutowireCapableBeanFactory().autowireBean(this);
		
//		System.setProperty("webdriver.gecko.driver", "/home/mpiva/Documents/apps/geckodriver-v0.23.0-linux64/geckodriver");
		System.setProperty("webdriver.gecko.driver", propertyService.get(GECKODRIVER_PATH));
		
		cleanAll();
		checkEmptyDatabase();
		
		initData();
	}

	@After
	public void close() throws Exception {
		if (driver != null) {
			driver.quit();
		}
		
		cleanAll();
		checkEmptyDatabase();
	}

	protected void cleanAll() throws ServiceException, SecurityServiceException {
		entityManagerClear();
		
		cleanEntities(userService);
		cleanEntities(userGroupService);
		cleanEntities(authorityService);
		cleanEntities(historyLogService);
		
		emptyIndexes();
	}

	protected void initData() throws ServiceException, SecurityServiceException {
		initAuthorities();
		initUserGroups();
		initUsers();
	}

	private void initAuthorities() throws ServiceException, SecurityServiceException {
		authorityService.create(new Authority(CoreAuthorityConstants.ROLE_ADMIN));
		authorityService.create(new Authority(CoreAuthorityConstants.ROLE_AUTHENTICATED));
	}

	private void initUserGroups() throws ServiceException, SecurityServiceException {
		users = new UserGroup("Users");
		administrators = new UserGroup("Administrators");
		
		userGroupService.create(users);
		userGroupService.create(administrators);
	}

	private void initUsers() throws ServiceException, SecurityServiceException {
		basicUser = createUser("basicUser", UserTypeDescriptor.BASIC_USER,
			ImmutableSet.of(BasicApplicationAuthorityConstants.ROLE_AUTHENTICATED), ImmutableSet.of(users));
		createUser("basicUser2", UserTypeDescriptor.BASIC_USER,
				ImmutableSet.of(BasicApplicationAuthorityConstants.ROLE_AUTHENTICATED), ImmutableSet.of(users));
		
		administrator = createUser("administrator", UserTypeDescriptor.TECHNICAL_USER,
			ImmutableSet.of(BasicApplicationAuthorityConstants.ROLE_ADMIN), ImmutableSet.of(administrators));
	}

	private <U extends User> U createUser(String username, UserTypeDescriptor<U> type, Set<String> authorities, Set<UserGroup> userGroups)
		throws ServiceException, SecurityServiceException {
		
		U user = type.getSupplier().get();
		user.setUsername(username);
		user.setFirstName(username);
		user.setLastName(username);
		if (authorities != null) {
			for (String authority : authorities) {
				user.addAuthority(authorityService.getByName(authority));
			}
		}
		
		userService.create(user);
		userService.setPasswords(user, USER_PASSWORD);
		
		if (userGroups != null) {
			for (UserGroup userGroup : userGroups) {
				userGroupService.addUser(userGroup, user);
			}
		}
		
		return user;
	}

	/**
	 * This method serves only when the test threads are stopped abruptly (not for the nominal case)
	 * The purpose is to clean Lucene indexes by reindexing an empty database
	 * It takes around 10 milliseconds if the database is empty (except the first time, it can take 200 millisecond)
	 * otherwise it takes 500 milliseconds wheter there is 1000 or 10 000 objets to reindex
	 */
	protected void emptyIndexes() throws ServiceException {
		Set<Class<?>> clazzes = hibernateSearchService.getIndexedRootEntities();
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(getEntityManager());
		for (Class<?> clazz : clazzes) {
			QueryBuilder builder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(clazz).get();
			FullTextQuery query = fullTextEntityManager.createFullTextQuery(builder.all().createQuery(), clazz);
			if (query.getResultSize() > 0) {
				hibernateSearchService.reindexAll();
				break;
			}
		}
	}

	protected static <E extends GenericEntity<?, ? super E>> void cleanEntities(IGenericEntityService<?, E> service) throws ServiceException, SecurityServiceException {
		for (E entity : service.list()) {
			service.delete(entity);
		}
	}

	private void checkEmptyDatabase() {
		Set<EntityType<?>> entityTypes = getEntityManager().getEntityManagerFactory().getMetamodel().getEntities();
		for (EntityType<?> entityType : entityTypes) {
			List<?> entities = listEntities(entityType.getBindableJavaType());
			
			if (entities.size() > 0) {
				Assert.fail(String.format("Il reste des objets de type %1$s", entities.get(0).getClass().getSimpleName()));
			}
		}
	}

	protected <E> List<E> listEntities(Class<E> clazz) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<E> cq = cb.createQuery(clazz);
		cq.from(clazz);
		
		return getEntityManager().createQuery(cq).getResultList();
	}

	protected EntityManager getEntityManager() {
		return entityManagerUtils.getEntityManager();
	}

	protected void entityManagerClear() {
		entityManagerUtils.getEntityManager().clear();
	}

}
