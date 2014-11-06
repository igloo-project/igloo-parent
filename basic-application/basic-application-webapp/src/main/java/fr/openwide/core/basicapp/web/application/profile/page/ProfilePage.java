package fr.openwide.core.basicapp.web.application.profile.page;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.basicapp.web.application.profile.component.ProfileInformationPanel;
import fr.openwide.core.basicapp.web.application.profile.template.ProfileTemplate;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;
import fr.openwide.core.wicket.more.model.BindingModel;

public class ProfilePage extends ProfileTemplate {

	private static final long serialVersionUID = -8757939680257114559L;

	public static IPageLinkDescriptor linkDescriptor() {
		return new LinkDescriptorBuilder()
				.page(ProfilePage.class)
				.build();
	}

	public ProfilePage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(BindingModel.of(userModel, Bindings.user().fullName())));
		
		add(new ProfileInformationPanel("description", userModel));
	}

}
