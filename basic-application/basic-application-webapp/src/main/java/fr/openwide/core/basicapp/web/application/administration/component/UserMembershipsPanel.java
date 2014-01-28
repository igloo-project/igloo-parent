package fr.openwide.core.basicapp.web.application.administration.component;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.model.UserGroup;
import fr.openwide.core.basicapp.core.business.user.service.IUserGroupService;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.basicapp.web.application.administration.page.AdministrationUserGroupDescriptionPage;
import fr.openwide.core.basicapp.web.application.common.component.UserGroupAutocompleteAjaxComponent;
import fr.openwide.core.commons.util.functional.SerializableFunction;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.markup.html.basic.PlaceholderContainer;
import fr.openwide.core.wicket.more.markup.html.collection.GenericEntitySetView;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.component.AjaxConfirmLink;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class UserMembershipsPanel extends GenericPanel<User> {

	private static final long serialVersionUID = -517286662347263793L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserMembershipsPanel.class);

	@SpringBean
	private IUserGroupService userGroupService;
	
	public UserMembershipsPanel(String id, IModel<User> userModel) {
		super(id, userModel);
		
		// Groups list
		Component userGroupListView = new GenericEntitySetView<UserGroup>("groups", BindingModel.of(getModel(), Bindings.user().userGroups())) {
			private static final long serialVersionUID = -6489746843440088695L;
			
			@Override
			protected void populateItem(final Item<UserGroup> item) {
				item.add(AdministrationUserGroupDescriptionPage.linkGenerator(item.getModel()).link("groupLink")
						.setBody(BindingModel.of(item.getModel(), Bindings.userGroup().name())));
				
				IModel<String> confirmationTextModel = new StringResourceModel(
						"administration.usergroup.members.delete.confirmation.text",
						null, new Object[] {
								UserMembershipsPanel.this.getModelObject().getFullName(),
								item.getModelObject().getName()
						}
				);
				
				item.add(AjaxConfirmLink.build("deleteLink", item.getModel())
						.title(new ResourceModel("administration.usergroup.members.delete.confirmation.title"))
						.content(confirmationTextModel)
						.confirm()
						.onClick(new SerializableFunction<AjaxRequestTarget, Void>() {
							private static final long serialVersionUID = 1L;
							@Override
							public Void apply(AjaxRequestTarget target) {
								try {
									UserGroup userGroup = item.getModelObject();
									User user = UserMembershipsPanel.this.getModelObject();
									
									userGroupService.removePerson(userGroup, user);
									Session.get().success(getString("administration.usergroup.members.delete.success"));
								} catch (Exception e) {
									LOGGER.error("Error occured while removing user from user group", e);
									Session.get().error(getString("administration.usergroup.members.delete.error"));
								}
								target.add(getPage());
								FeedbackUtils.refreshFeedback(target, getPage());
								return null;
							}
						})
						.create()
				);
			}
		};
		add(userGroupListView);
		
		add(new PlaceholderContainer("emptyList").collectionModel(BindingModel.of(getModel(), Bindings.user().userGroups())));
		
		// Add group form
		IModel<UserGroup> emptyUserGroupModel = new GenericEntityModel<Long, UserGroup>(null);
		
		final UserGroupAutocompleteAjaxComponent userGroupAutocomplete = new UserGroupAutocompleteAjaxComponent(
				"userGroupAutocomplete", emptyUserGroupModel);
		userGroupAutocomplete.setAutoUpdate(true);
		
		final Form<UserGroup> addGroupForm = new Form<UserGroup>("addGroupForm", emptyUserGroupModel);
		addGroupForm.add(userGroupAutocomplete);
		addGroupForm.add(new AjaxSubmitLink("addGroupLink", addGroupForm) {
			private static final long serialVersionUID = 6935376642872117563L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				User user = UserMembershipsPanel.this.getModelObject();
				UserGroup selectedUserGroup = userGroupAutocomplete.getModelObject();
				
				if (selectedUserGroup != null) {
					if (!selectedUserGroup.getPersons().contains(user)) {
						try {
							userGroupService.addPerson(selectedUserGroup, user);
							getSession().success(getString("administration.usergroup.members.add.success"));
						} catch (Exception e) {
							LOGGER.error("Unknown error occured while adding a user to a usergroup", e);
							getSession().error(getString("administration.usergroup.members.add.error"));
						}
					} else {
						LOGGER.error("User already added to this group");
						getSession().warn(getString("administration.usergroup.members.add.alreadyMember"));
					}
				}
				userGroupAutocomplete.setModelObject(null);
				target.add(getPage());
				FeedbackUtils.refreshFeedback(target, getPage());
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				FeedbackUtils.refreshFeedback(target, getPage());
			}
		});
		add(addGroupForm);
	}
}
