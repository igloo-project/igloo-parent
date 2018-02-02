package org.iglooproject.basicapp.web.application.administration.component;

import static org.iglooproject.basicapp.web.application.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE_DESCRIPTION;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.basicapp.core.business.user.service.IUserGroupService;
import org.iglooproject.basicapp.web.application.administration.form.UserGroupDropDownSingleChoice;
import org.iglooproject.basicapp.web.application.administration.model.UserGroupDataProvider;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationUserGroupDescriptionPage;
import org.iglooproject.basicapp.web.application.common.renderer.ActionRenderers;
import org.iglooproject.basicapp.web.application.common.util.CssClassConstants;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.markup.html.panel.GenericPanel;
import org.iglooproject.wicket.more.markup.html.action.AbstractOneParameterAjaxAction;
import org.iglooproject.wicket.more.markup.html.bootstrap.label.model.BootstrapColor;
import org.iglooproject.wicket.more.markup.html.factory.AbstractDetachableFactory;
import org.iglooproject.wicket.more.markup.html.factory.AbstractParameterizedComponentFactory;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.AddInPlacement;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserMembershipsPanel extends GenericPanel<User> {

	private static final long serialVersionUID = -517286662347263793L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserMembershipsPanel.class);

	@SpringBean
	private IUserGroupService userGroupService;
	
	@SpringBean
	private IPropertyService propertyService;
	
	private final UserGroupDataProvider dataProvider;
	
	private IModel<? extends User> userModel;
	
	public UserMembershipsPanel(String id, final IModel<? extends User> userModel) {
		super(id, userModel);
		
		this.userModel = userModel;
		this.dataProvider = new UserGroupDataProvider(userModel);
		
		add(
				DataTableBuilder.start(dataProvider, dataProvider.getSortModel())
						.addLabelColumn(new ResourceModel("administration.usergroup.field.name"))
							.withLink(AdministrationUserGroupDescriptionPage.MAPPER)
							.withClass("text text-md")
						.addActionColumn()
								.addConfirmAction(ActionRenderers.constant("administration.usergroup.members.delete", "fa fa-fw fa-times", BootstrapColor.DANGER))
										.title(new ResourceModel("administration.usergroup.members.delete.confirmation.title"))
										.content(new AbstractDetachableFactory<IModel<UserGroup>, IModel<String>>() {
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
								.withClassOnElements(CssClassConstants.BTN_XS)
								.end()
								.withClass("actions")
						.bootstrapCard()
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
						.build("userMemberships", propertyService.get(PORTFOLIO_ITEMS_PER_PAGE_DESCRIPTION))
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
			
			IModel<UserGroup> userGroupModel = new GenericEntityModel<Long, UserGroup>();
			
			add(
					new Form<UserGroup>("form", userGroupModel)
							.add(
									new UserGroupDropDownSingleChoice("userGroup", userGroupModel)
											.setRequired(true)
											.setLabel(new ResourceModel("administration.usergroup.group"))
											.add(new LabelPlaceholderBehavior()),
									new AjaxButton("submit") {
										private static final long serialVersionUID = 1L;
										@Override
										protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
											try {
												User user = UserMembershipsPanel.this.getModelObject();
												UserGroup userGroup = userGroupModel.getObject();
												
												if (!user.getGroups().contains(userGroup)) {
													userGroupService.addUser(userGroup, user);
													getSession().success(getString("administration.usergroup.members.add.success"));
												} else {
													LOGGER.warn("User already added to this group.");
													getSession().warn(getString("administration.usergroup.members.add.alreadyMember"));
												}
												
												userGroupModel.setObject(null);
												userGroupModel.detach();
												target.add(getPage());
											} catch (Exception e) {
												LOGGER.error("Error when adding user to user group.", e);
												getSession().error(getString("common.error.unexpected"));
											}
											FeedbackUtils.refreshFeedback(target, getPage());
										}
										@Override
										protected void onError(AjaxRequestTarget target,Form<?> form) {
											FeedbackUtils.refreshFeedback(target, getPage());
										}
									}
							)
			);
		}
	}
}
