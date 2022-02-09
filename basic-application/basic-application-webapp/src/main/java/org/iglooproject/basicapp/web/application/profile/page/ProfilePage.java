package org.iglooproject.basicapp.web.application.profile.page;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;
import org.iglooproject.basicapp.web.application.profile.component.ProfileDescriptionPanel;
import org.iglooproject.basicapp.web.application.profile.template.ProfileTemplate;
import org.iglooproject.wicket.api.bindgen.BindingModel;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.util.model.Detachables;

public class ProfilePage extends ProfileTemplate {

	private static final long serialVersionUID = -8757939680257114559L;

	public static final IPageLinkDescriptor linkDescriptor() {
		return LinkDescriptorBuilder.start()
				.page(ProfilePage.class);
	}

	protected final IModel<User> userModel = BasicApplicationSession.get().getUserModel();

	public ProfilePage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(
			BindingModel.of(userModel, Bindings.user().fullName())
		));
		
		add(
			new CoreLabel("pageTitle", BindingModel.of(userModel, Bindings.user().fullName()))
		);
		
		add(
			new ProfileDescriptionPanel("description", userModel)
		);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(userModel);
	}

}
