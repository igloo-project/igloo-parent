package test.web;

import org.assertj.core.util.Sets;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationBasicUserDetailPage;
import org.iglooproject.basicapp.web.application.administration.template.AdministrationUserDetailTemplate;
import org.iglooproject.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.junit.Test;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@EnableWebSecurity
public class AdministrationBasicUserDetailPageTestCase extends AbstractBasicApplicationWebappTestCase {

	@Test
	public void administrationBasicUserDetailPage() throws ServiceException, SecurityServiceException {
		initBasicUserAndStartPage();
		
		tester.assertRenderedPage(AdministrationBasicUserDetailPage.class);
	}

	@Test
	public void administrationBasicUserDetailPageDesactivate() throws ServiceException, SecurityServiceException {
		initBasicUserAndStartPage();
		
		tester.assertVisible("headerElementsSection:actionsContainer:disable");
		tester.assertEnabled("headerElementsSection:actionsContainer:disable");
		tester.clickLink("headerElementsSection:actionsContainer:disable");
		
//		FormTester formTester = tester.newFormTester(modalFormPath("headerElementsSection:actionsContainer:disable"));
//		formTester.submit();
		
//		tester.assertComponentOnAjaxResponse("headerElementsSection:actionsContainer:disable");
//		tester.assertComponentOnAjaxResponse("headerElementsSection:actionsContainer:enable");
	}

	private void initBasicUserAndStartPage() throws ServiceException, SecurityServiceException {
		User user = createUser("user1", "firstname1", "lastname1", "password1",
				UserTypeDescriptor.BASIC_USER, null, Sets.newTreeSet(CoreAuthorityConstants.ROLE_AUTHENTICATED));
		
		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_ADMIN);
		
		String url = AdministrationUserDetailTemplate.mapper()
			.ignoreParameter2()
			.map(GenericEntityModel.of(user)).url();
		tester.executeUrl(url);
	}
}
