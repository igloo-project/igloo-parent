package fr.openwide.core.basicapp.web.application.administration.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.basicapp.core.business.user.model.UserGroup;
import fr.openwide.core.basicapp.core.util.binding.Binding;
import fr.openwide.core.basicapp.web.application.administration.component.UserGroupDescriptionPanel;
import fr.openwide.core.basicapp.web.application.administration.component.UserGroupMembersPanel;
import fr.openwide.core.basicapp.web.application.administration.template.AdministrationTemplate;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.descriptor.parameter.CommonParameters;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class AdministrationUserGroupDescriptionPage extends AdministrationTemplate {

	private static final long serialVersionUID = -5780326896837623229L;

	private IModel<UserGroup> userGroupModel;
	
	public static IPageLinkDescriptor linkDescriptor(IModel<UserGroup> userGroupModel) {
		return new LinkDescriptorBuilder()
				.page(AdministrationUserGroupDescriptionPage.class)
				.map(CommonParameters.ID, userGroupModel, UserGroup.class).mandatory()
				.build();
	}

	public AdministrationUserGroupDescriptionPage(PageParameters parameters) {
		super(parameters);
		
		userGroupModel = new GenericEntityModel<Long, UserGroup>(null);
		
		linkDescriptor(userGroupModel).extractSafely(parameters, AdministrationUserGroupPortfolioPage.linkDescriptor(),
				getString("administration.usergroup.error"));
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("navigation.administration.usergroup"),
				AdministrationUserGroupPortfolioPage.linkDescriptor()));
		
		addBreadCrumbElement(new BreadCrumbElement(BindingModel.of(userGroupModel, Binding.userGroup().name()),
				AdministrationUserGroupDescriptionPage.linkDescriptor(userGroupModel)));
		
		add(new Label("pageTitle", BindingModel.of(userGroupModel, Binding.userGroup().name())));
		
		add(new UserGroupDescriptionPanel("description", userGroupModel));
		add(new UserGroupMembersPanel("members", userGroupModel));
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return AdministrationUserGroupPortfolioPage.class;
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		
		if (userGroupModel != null) {
			userGroupModel.detach();
		}
	}
}
