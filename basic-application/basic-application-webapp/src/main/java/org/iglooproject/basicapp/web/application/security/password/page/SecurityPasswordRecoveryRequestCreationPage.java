package org.iglooproject.basicapp.web.application.security.password.page;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.basicapp.web.application.security.password.component.SecurityPasswordRecoveryRequestCreationContentPanel;
import org.iglooproject.basicapp.web.application.security.password.template.SecurityPasswordTemplate;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

public class SecurityPasswordRecoveryRequestCreationPage extends SecurityPasswordTemplate {

	private static final long serialVersionUID = 8849956364562663727L;

	public static final IPageLinkDescriptor linkDescriptor() {
		return LinkDescriptorBuilder.start()
			.page(SecurityPasswordRecoveryRequestCreationPage.class);
	}

	public SecurityPasswordRecoveryRequestCreationPage(PageParameters parameters) {
		super(parameters);
		
		addHeadPageTitlePrependedElement(new BreadCrumbElement(
			new ResourceModel("security.password.recovery.request.creation.pageTitle")
		));
	}

	@Override
	protected IModel<String> getTitleModel() {
		return new ResourceModel("security.password.recovery.request.creation.pageTitle");
	}

	@Override
	protected Component getContentComponent(String wicketId) {
		return new SecurityPasswordRecoveryRequestCreationContentPanel(wicketId);
	}

	@Override
	public Condition keepSignedIn() {
		return Condition.alwaysFalse();
	}

}
