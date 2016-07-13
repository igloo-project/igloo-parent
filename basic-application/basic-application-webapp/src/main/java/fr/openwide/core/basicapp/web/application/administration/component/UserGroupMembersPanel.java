package fr.openwide.core.basicapp.web.application.administration.component;

import static fr.openwide.core.basicapp.web.application.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE_DESCRIPTION;

import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.model.UserGroup;
import fr.openwide.core.basicapp.core.business.user.search.UserSort;
import fr.openwide.core.basicapp.core.business.user.service.IUserGroupService;
import fr.openwide.core.basicapp.web.application.administration.model.UserDataProvider;
import fr.openwide.core.basicapp.web.application.administration.template.AdministrationUserDescriptionTemplate;
import fr.openwide.core.basicapp.web.application.common.form.UserAutocompleteAjaxComponent;
import fr.openwide.core.basicapp.web.application.common.renderer.ActionRenderers;
import fr.openwide.core.basicapp.web.application.common.util.CssClassConstants;
import fr.openwide.core.basicapp.web.application.navigation.link.LinkFactory;
import fr.openwide.core.spring.property.service.IPropertyService;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.link.model.ComponentPageModel;
import fr.openwide.core.wicket.more.markup.html.action.AbstractAjaxAction;
import fr.openwide.core.wicket.more.markup.html.action.AbstractOneParameterAjaxAction;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.model.BootstrapColor;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRendererInformation;
import fr.openwide.core.wicket.more.markup.html.factory.AbstractDetachableFactory;
import fr.openwide.core.wicket.more.markup.html.factory.AbstractParameterizedComponentFactory;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;
import fr.openwide.core.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import fr.openwide.core.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import fr.openwide.core.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.AddInPlacement;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class UserGroupMembersPanel extends GenericPanel<UserGroup> {

	private static final long serialVersionUID = 1955579250974258074L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserGroupMembersPanel.class);

	@SpringBean
	private IUserGroupService userGroupService;
	
	@SpringBean
	private IPropertyService propertyService;
	
	private final UserDataProvider dataProvider;
	
	public UserGroupMembersPanel(String id, IModel<UserGroup> userGroupModel) {
		super(id, userGroupModel);
		setOutputMarkupId(true);
		
		// Members list
		dataProvider = new UserDataProvider();
		dataProvider.getGroupModel().setObject(userGroupModel.getObject());
		
		LinkFactory.get();
		DecoratedCoreDataTablePanel<User, UserSort> groupMemberships = 
				DataTableBuilder.start(dataProvider, dataProvider.getSortModel())
					.addLabelColumn(new ResourceModel("administration.usergroup.field.name"))
							.withLink(AdministrationUserDescriptionTemplate.mapper().setParameter2(new ComponentPageModel(this)))
							.withClass("text text-md")
					.addActionColumn()
							.addConfirmAction(ActionRenderers.constant("administration.usergroup.members.delete", "fa fa-fw fa-times", BootstrapColor.DANGER))
									.title(new ResourceModel("administration.usergroup.members.delete.confirmation.title"))
									.content(new AbstractDetachableFactory<IModel<User>, IModel<String>>() {
										private static final long serialVersionUID = 1L;
										@Override
										public IModel<String> create(IModel<User> userModel) {
											return new StringResourceModel("administration.usergroup.members.delete.confirmation.text")
													.setParameters(userModel.getObject().getFullName(), UserGroupMembersPanel.this.getModelObject().getName());
										}
									})
									.confirm()
									.onClick(new AbstractOneParameterAjaxAction<IModel<User>>() {
										private static final long serialVersionUID = 1L;
										@Override
										public void execute(AjaxRequestTarget target, IModel<User> parameter) {
											try {
												UserGroup userGroup = UserGroupMembersPanel.this.getModelObject();
												User user = parameter.getObject();
												
												userGroupService.removeUser(userGroup, user);
												Session.get().success(getString("administration.usergroup.members.delete.success"));
												throw new RestartResponseException(getPage());
											} catch (RestartResponseException e) {
												throw e;
											} catch (Exception e) {
												LOGGER.error("Unknown error occured while removing a group from the user", e);
												getSession().error(getString("common.error.unexpected"));
												FeedbackUtils.refreshFeedback(target, getPage());
											}
										}
									})
									.hideLabel()
							.withClassOnElements(CssClassConstants.BTN_SM)
							.end()
							.withClass("actions")
					.bootstrapPanel()
					.actions(AddInPlacement.HEADING_MAIN, new GenericEntityModel<Long, UserGroup>(new UserGroup()))
								.addAction(ActionRenderers.add(), new AbstractOneParameterAjaxAction<IModel<UserGroup>>() {
									private static final long serialVersionUID = 1L;
									@Override
									public void execute(AjaxRequestTarget target, IModel<UserGroup> parameter) {
									}
								})
								.end()
					.actions(AddInPlacement.BODY_BOTTOM)
							.addAction(ActionRenderers.remove(), new AbstractAjaxAction() {
								private static final long serialVersionUID = 1L;
								@Override
								public void execute(AjaxRequestTarget target) {
								}
							})
							.addAction(new BootstrapRenderer<Void>() {
								private static final long serialVersionUID = 1L;
								@Override
								protected BootstrapRendererInformation doRender(Void value, Locale locale) {
									return BootstrapRendererInformation.builder().color(BootstrapColor.PRIMARY).label("hi").build();
								}
							}, new AbstractAjaxAction() {
								private static final long serialVersionUID = 1L;
								@Override
								public void execute(AjaxRequestTarget target) {
									System.out.println("");
								}
							})
							.end()
					.addIn(AddInPlacement.FOOTER_RIGHT, new AbstractParameterizedComponentFactory<Component, Component>() {
						private static final long serialVersionUID = 1L;
						@Override
						public Component create(String wicketId, final Component table ) {
							return new UserGroupAddUserFragment(wicketId)
								.add(new ClassAttributeAppender("add-in-quick-add"));
						}
					})
					.ajaxPager(AddInPlacement.HEADING_RIGHT)
					.count("administration.usergroup.members.count")
					.build("groupMemberships", propertyService.get(PORTFOLIO_ITEMS_PER_PAGE_DESCRIPTION));
		add(
				groupMemberships
		);
	}

	private class UserGroupAddUserFragment extends Fragment {
		
		private static final long serialVersionUID = 1L;
		
		public UserGroupAddUserFragment(String id) {
			super(id, "addUser", UserGroupMembersPanel.this);
			// Add member form
			IModel<User> emptyUserModel = new GenericEntityModel<Long, User>();
			
			final UserAutocompleteAjaxComponent userAutocomplete = new UserAutocompleteAjaxComponent("userAutocomplete",
					emptyUserModel);
			userAutocomplete.setAutoUpdate(true);
			IModel<String> autocompleteLabelModel = new ResourceModel("administration.user.user");
			userAutocomplete.getAutocompleteField()
				.setLabel(autocompleteLabelModel)
				.add(new LabelPlaceholderBehavior());
			
			final Form<User> addMemberForm = new Form<User>("addMemberForm", emptyUserModel);
			addMemberForm.add(
					userAutocomplete
						.setRequired(true)
						.setLabel(autocompleteLabelModel)
			);
			addMemberForm.add(new AjaxSubmitLink("addMemberLink", addMemberForm) {
				private static final long serialVersionUID = 6935376642872117563L;
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					UserGroup userGroup = UserGroupMembersPanel.this.getModelObject();
					User selectedUser = userAutocomplete.getModelObject();
					
					if (selectedUser != null) {
						if (!selectedUser.getGroups().contains(userGroup)) {
							try {
								userGroupService.addUser(userGroup, selectedUser);
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
					userAutocomplete.setModelObject(null);
					target.add(getPage());
					FeedbackUtils.refreshFeedback(target, getPage());
				}
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					FeedbackUtils.refreshFeedback(target, getPage());
				}
			});
			add(addMemberForm);
			
		}
	}
}
