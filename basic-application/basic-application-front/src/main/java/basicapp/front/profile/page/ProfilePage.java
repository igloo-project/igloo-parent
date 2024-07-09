package basicapp.front.profile.page;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

import basicapp.back.business.user.model.User;
import basicapp.back.util.binding.Bindings;
import basicapp.front.BasicApplicationSession;
import basicapp.front.profile.component.ProfileDescriptionPanel;
import basicapp.front.profile.template.ProfileTemplate;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.model.BindingModel;
import igloo.wicket.model.Detachables;

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
