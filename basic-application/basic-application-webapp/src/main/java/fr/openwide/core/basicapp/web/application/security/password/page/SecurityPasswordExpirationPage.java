package fr.openwide.core.basicapp.web.application.security.password.page;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.basicapp.core.security.service.ISecurityManagementService;
import fr.openwide.core.basicapp.web.application.security.password.component.SecurityPasswordExpirationContentPanel;
import fr.openwide.core.basicapp.web.application.security.password.component.SecurityPasswordIntroPanel;
import fr.openwide.core.basicapp.web.application.security.password.template.SecurityPasswordTemplate;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class SecurityPasswordExpirationPage extends SecurityPasswordTemplate {

	private static final long serialVersionUID = 547223775134254240L;

	public static final IPageLinkDescriptor linkDescriptor() {
		return new LinkDescriptorBuilder()
				.page(SecurityPasswordExpirationPage.class)
				.build();
	}

	@SpringBean
	private ISecurityManagementService securityManagementService;

	public SecurityPasswordExpirationPage(PageParameters parameters) {
		super(parameters);
		
		addHeadPageTitlePrependedElement(
				new BreadCrumbElement(new ResourceModel("security.password.expiration.pageTitle"))
		);
	}

	@Override
	protected IModel<String> getTitleModel() {
		return new ResourceModel("security.password.expiration.pageTitle");
	}

	@Override
	protected Component getIntroComponent(String wicketId) {
		return new SecurityPasswordIntroPanel(wicketId, "security.password.expiration.intro");
	}

	@Override
	protected Component getContentComponent(String wicketId) {
		return new SecurityPasswordExpirationContentPanel(wicketId);
	}

}
