package fr.openwide.core.basicapp.web.application.security.password.page;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.basicapp.web.application.BasicApplicationSession;
import fr.openwide.core.basicapp.web.application.security.password.component.SecurityPasswordIntroPanel;
import fr.openwide.core.basicapp.web.application.security.password.component.SecurityPasswordRecoveryContentPanel;
import fr.openwide.core.basicapp.web.application.security.password.template.SecurityPasswordTemplate;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class SecurityPasswordRecoveryPage extends SecurityPasswordTemplate {

	private static final long serialVersionUID = 547223775134254240L;

	public static final IPageLinkDescriptor linkDescriptor() {
		return LinkDescriptorBuilder.start()
				.page(SecurityPasswordRecoveryPage.class);
	}

	public SecurityPasswordRecoveryPage(PageParameters parameters) {
		super(parameters);

		// Being connected here doesn't make any sense
		BasicApplicationSession.get().signOut();
		
		addHeadPageTitlePrependedElement(
				new BreadCrumbElement(new ResourceModel("security.password.recovery.pageTitle"))
		);
	}

	@Override
	protected IModel<String> getTitleModel() {
		return new ResourceModel("security.password.recovery.pageTitle");
	}

	@Override
	protected Component getIntroComponent(String wicketId) {
		return new SecurityPasswordIntroPanel(wicketId, "security.password.recovery.intro");
	}

	@Override
	protected Component getContentComponent(String wicketId) {
		return new SecurityPasswordRecoveryContentPanel(wicketId);
	}
}
