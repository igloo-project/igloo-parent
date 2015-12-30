package fr.openwide.core.basicapp.web.application.administration.component;

import static fr.openwide.core.basicapp.web.application.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE_DESCRIPTION;

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
import fr.openwide.core.basicapp.core.business.user.search.UserGroupSort;
import fr.openwide.core.basicapp.core.business.user.service.IUserGroupService;
import fr.openwide.core.basicapp.web.application.administration.model.UserGroupDataProvider;
import fr.openwide.core.basicapp.web.application.administration.page.AdministrationUserGroupDescriptionPage;
import fr.openwide.core.basicapp.web.application.common.form.UserGroupAutocompleteAjaxComponent;
import fr.openwide.core.basicapp.web.application.common.util.CssClassConstants;
import fr.openwide.core.spring.property.service.IPropertyService;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.markup.html.action.AbstractOneParameterAjaxAction;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.model.BootstrapColor;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRendererInformation;
import fr.openwide.core.wicket.more.markup.html.factory.AbstractOneParameterModelFactory;
import fr.openwide.core.wicket.more.markup.html.factory.AbstractParameterizedComponentFactory;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;
import fr.openwide.core.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.DecoratedCoreDataTablePanel;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.DecoratedCoreDataTablePanel.AddInPlacement;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.DataTableBuilder;
import fr.openwide.core.wicket.more.model.GenericEntityModel;
import fr.openwide.core.wicket.more.util.model.Detachables;

public class UserMembershipsPanel extends GenericPanel<User> {

	private static final long serialVersionUID = -517286662347263793L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserMembershipsPanel.class);

	@SpringBean
	private IUserGroupService userGroupService;
	
	@SpringBean
	private IPropertyService propertyService;
	
	private final UserGroupDataProvider dataProvider;
	
	private IModel<User> userModel;
	
	public UserMembershipsPanel(String id, final IModel<User> userModel) {
		super(id, userModel);

		this.userModel = userModel;
		
		dataProvider = new UserGroupDataProvider(userModel);
		DecoratedCoreDataTablePanel<UserGroup, UserGroupSort> userMemberships = 
				DataTableBuilder.start(dataProvider, dataProvider.getSortModel())
						.addLabelColumn(new ResourceModel("administration.usergroup.field.name"))
							.withLink(AdministrationUserGroupDescriptionPage.MAPPER)
							.withClass("text text-md")
						.addActionColumn()
								.addConfirmAction(
										BootstrapRenderer.with(
												BootstrapRendererInformation.builder()
														.tooltip("administration.usergroup.members.delete")
														.icon("fa fa-fw fa-trash-o")
														.color(BootstrapColor.DANGER)
														.build()
										)
								)
										.title(new ResourceModel("administration.usergroup.members.delete.confirmation.title"))
										.content(new AbstractOneParameterModelFactory<IModel<UserGroup>, String>() {
											private static final long serialVersionUID = 1L;
											@Override
											public IModel<String> create(IModel<UserGroup> parameter) {
												return new StringResourceModel("administration.usergroup.members.delete.confirmation.text")
														.setParameters(UserMembershipsPanel.this.getModelObject().getFullName(), parameter.getObject().getName());
											}
										})
										.confirm()
										.onClick(new AbstractOneParameterAjaxAction<IModel<UserGroup>>() {
											private static final long serialVersionUID = 1L;
											@Override
											public void execute(AjaxRequestTarget target, IModel<UserGroup> parameter) {
												try {
													UserGroup userGroup = parameter.getObject();
													User user = userModel.getObject();
													
													userGroupService.removeUser(userGroup, user);
													Session.get().success(getString("administration.usergroup.members.delete.success"));
													throw new RestartResponseException(getPage());
												} catch (RestartResponseException e) {
													throw e;
												} catch (Exception e) {
													LOGGER.error("Unknown error occured while removing a user from a usergroup", e);
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
						.addIn(AddInPlacement.FOOTER_RIGHT,  new AbstractParameterizedComponentFactory<Component, Component>() {
							private static final long serialVersionUID = 1L;
							@Override
							public Component create(String wicketId, final Component table ) {
								return new UserGroupAddFragment(wicketId)
									.add(new ClassAttributeAppender("add-in-quick-add"));
							}
						})
						.ajaxPager(AddInPlacement.HEADING_RIGHT)
						.count("administration.user.groups.count")
						.build("userMemberships", propertyService.get(PORTFOLIO_ITEMS_PER_PAGE_DESCRIPTION));
		add(
				userMemberships
		);
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(userModel, dataProvider);
	}
	
	private class UserGroupAddFragment extends Fragment {
		
		private static final long serialVersionUID = 1L;
		
		public UserGroupAddFragment(String id) {
			super(id, "addGroup", UserMembershipsPanel.this);
			// Add group form
			IModel<UserGroup> emptyUserGroupModel = new GenericEntityModel<Long, UserGroup>();
			
			final UserGroupAutocompleteAjaxComponent userGroupAutocomplete = new UserGroupAutocompleteAjaxComponent(
					"userGroupAutocomplete", emptyUserGroupModel);
			userGroupAutocomplete.setAutoUpdate(true);
			IModel<String> autocompleteLabelModel = new ResourceModel("administration.usergroup.group");
			userGroupAutocomplete.getAutocompleteField()
				.setLabel(new ResourceModel("administration.usergroup.group"))
				.add(new LabelPlaceholderBehavior());
			
			final Form<UserGroup> addGroupForm = new Form<UserGroup>("addGroupForm", emptyUserGroupModel);
			addGroupForm.add(
					userGroupAutocomplete
						.setRequired(true)
						.setLabel(autocompleteLabelModel)
			);
			addGroupForm.add(new AjaxSubmitLink("addGroupLink", addGroupForm) {
				private static final long serialVersionUID = 6935376642872117563L;
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					User user = UserMembershipsPanel.this.getModelObject();
					UserGroup selectedUserGroup = userGroupAutocomplete.getModelObject();
				
					if (selectedUserGroup != null) {
						if (!user.getGroups().contains(selectedUserGroup)) {
							try {
								userGroupService.addUser(selectedUserGroup, user);
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
}
