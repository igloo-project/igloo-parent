package fr.openwide.core.basicapp.web.application.profile.template;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.basicapp.web.application.BasicApplicationSession;
import fr.openwide.core.basicapp.web.application.common.template.MainTemplate;
import fr.openwide.core.basicapp.web.application.profile.page.ProfilePage;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;
import fr.openwide.core.wicket.more.model.BindingModel;

public class ProfileTemplate extends MainTemplate {

	private static final long serialVersionUID = 1029271113953538262L;

	protected final IModel<User> userModel = BasicApplicationSession.get().getUserModel();

	public ProfileTemplate(PageParameters parameters) {
		super(parameters);
		
		add(
				new Label("pageTitle", BindingModel.of(userModel, Bindings.user().fullName()))
		);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("navigation.profile")));
	}

	@Override
	protected Class<? extends WebPage> getFirstMenuPage() {
		return ProfilePage.class;
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		userModel.detach();
	}

}
