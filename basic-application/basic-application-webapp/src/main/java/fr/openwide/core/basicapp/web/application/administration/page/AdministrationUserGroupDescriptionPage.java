package fr.openwide.core.basicapp.web.application.administration.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.basicapp.core.business.user.model.UserGroup;
import fr.openwide.core.basicapp.core.business.user.service.IUserGroupService;
import fr.openwide.core.basicapp.core.util.binding.Binding;
import fr.openwide.core.basicapp.web.application.administration.component.UserGroupDescriptionPanel;
import fr.openwide.core.basicapp.web.application.administration.component.UserGroupMembersPanel;
import fr.openwide.core.basicapp.web.application.administration.template.AdministrationTemplate;
import fr.openwide.core.basicapp.web.application.navigation.util.LinkUtils;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class AdministrationUserGroupDescriptionPage extends AdministrationTemplate {

	private static final long serialVersionUID = -5780326896837623229L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AdministrationUserGroupDescriptionPage.class);

	@SpringBean
	private IUserGroupService userGroupService;

	private IModel<UserGroup> userGroupModel;

	public AdministrationUserGroupDescriptionPage(PageParameters parameters) {
		super(parameters);
		
		try {
			UserGroup userGroup = userGroupService.getById(parameters.get(LinkUtils.ID_PARAMETER).toLongObject());
			
			userGroupModel = new GenericEntityModel<Long, UserGroup>(userGroup);
		} catch (Exception e) {
			LOGGER.error("Error on user loading", e);
			getSession().error(getString("administration.user.error"));
			
			redirect(AdministrationUserGroupPortfolioPage.class);
		}
		
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
