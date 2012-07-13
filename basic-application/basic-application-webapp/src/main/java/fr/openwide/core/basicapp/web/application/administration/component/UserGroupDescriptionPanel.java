package fr.openwide.core.basicapp.web.application.administration.component;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

import fr.openwide.core.basicapp.core.business.user.model.UserGroup;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;

public class UserGroupDescriptionPanel extends GenericPanel<UserGroup> {

	private static final long serialVersionUID = 4372823586880908316L;

//	@SpringBean
//	private IAuthorityService authorityService;

	public UserGroupDescriptionPanel(String id, final IModel<UserGroup> userGroupModel) {
		super(id, userGroupModel);
		
		add(new WebMarkupContainer("lockedWarningContainer") {
			private static final long serialVersionUID = -6522648858912041466L;
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(userGroupModel.getObject().getLocked());
			}
		});
		
//		add(new Label("description", BindingModel.of(userGroupModel, Binding.userGroup().description())));
		
//		add(new ListView<Authority>("authorities", Model.ofList(Lists.newArrayList(authorityService.list()))) {
//			private static final long serialVersionUID = -4307272691513553800L;
//			
//			@Override
//			protected void populateItem(ListItem<Authority> item) {
//				Authority authority = item.getModelObject();
//				item.add(new Label("authorityName", new ResourceModel("administration.userGroup.form.authority." + authority.getName())));
//				item.add(new BooleanImage("authorityCheck", Model.of(userGroupModel.getObject().getAuthorities().contains(authority))));
//			}
//		});
	}
}
