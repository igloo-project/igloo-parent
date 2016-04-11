package fr.openwide.core.basicapp.web.application.security.login.page;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.basicapp.web.application.common.template.ApplicationAccessTemplate;
import fr.openwide.core.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import fr.openwide.core.basicapp.web.application.security.login.component.SignInContentPanel;
import fr.openwide.core.basicapp.web.application.security.login.component.SignInFooterPanel;
import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class SignInPage extends ApplicationAccessTemplate {

	private static final long serialVersionUID = 7361718534092594202L;
	
	public static ILinkDescriptor linkDescriptor() {
		return new LinkDescriptorBuilder().page(SignInPage.class).build();
	}

	public SignInPage(PageParameters parameters) {
		super(parameters);
		addHeadPageTitlePrependedElement(new BreadCrumbElement(new ResourceModel("signIn.pageTitle")));
	}

	@Override
	protected IModel<String> getTitleModel() {
		return new ResourceModel("signIn.welcomeText");
	}

	@Override
	protected Component getContentComponent(String wicketId) {
		return new SignInContentPanel<>(wicketId, UserTypeDescriptor.USER);
	}

	@Override
	protected Component getFooterComponent(String wicketId) {
		return new SignInFooterPanel<>(wicketId, UserTypeDescriptor.USER);
	}

}
