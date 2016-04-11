package fr.openwide.core.basicapp.web.application.security.password.page;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.security.service.ISecurityManagementService;
import fr.openwide.core.basicapp.web.application.BasicApplicationApplication;
import fr.openwide.core.basicapp.web.application.BasicApplicationSession;
import fr.openwide.core.basicapp.web.application.security.password.component.SecurityPasswordIntroPanel;
import fr.openwide.core.basicapp.web.application.security.password.component.SecurityPasswordResetContentPanel;
import fr.openwide.core.basicapp.web.application.security.password.template.SecurityPasswordTemplate;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.descriptor.parameter.CommonParameters;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class SecurityPasswordResetPage extends SecurityPasswordTemplate {

	private static final long serialVersionUID = 1L;

	public static final IPageLinkDescriptor linkDescriptor(IModel<User> userModel, IModel<String> tokenModel) {
		return new LinkDescriptorBuilder()
				.page(SecurityPasswordResetPage.class)
				.map(CommonParameters.ID, userModel, User.class).mandatory()
				.map(CommonParameters.TOKEN, tokenModel, String.class).mandatory()
				.build();
	}

	private final IModel<User> userModel = new GenericEntityModel<Long, User>();

	@SpringBean
	private ISecurityManagementService securityManagementService;

	public SecurityPasswordResetPage(PageParameters parameters) {
		super(parameters);

		// Being connected here doesn't make any sense
		BasicApplicationSession.get().signOut();
		
		final IModel<String> tokenModel = Model.of("");
		
		linkDescriptor(userModel, tokenModel).extractSafely(
				parameters,
				BasicApplicationApplication.get().getHomePageLinkDescriptor(),
				getString("common.error.unexpected")
		);
		
		if (!tokenModel.getObject().equals(userModel.getObject().getPasswordRecoveryRequest().getToken())) {
			getSession().error(getString("security.password.reset.wrongToken"));
			throw BasicApplicationApplication.get().getHomePageLinkDescriptor().newRestartResponseException();
		}
		
		if (securityManagementService.isPasswordRecoveryRequestExpired(userModel.getObject())) {
			getSession().error(getString("security.password.reset.expired"));
			throw BasicApplicationApplication.get().getHomePageLinkDescriptor().newRestartResponseException();
		}
		
		addHeadPageTitlePrependedElement(
				new BreadCrumbElement(new ResourceModel("security.password.reset.pageTitle"))
		);
	}

	@Override
	protected IModel<String> getTitleModel() {
		return new ResourceModel("security.password.reset.pageTitle");
	}

	@Override
	protected Component getIntroComponent(String wicketId) {
		return new SecurityPasswordIntroPanel(wicketId, "security.password.reset.intro");
	}

	@Override
	protected Component getContentComponent(String wicketId) {
		return new SecurityPasswordResetContentPanel(wicketId, userModel);
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		userModel.detach();
	}

}
