package org.iglooproject.basicapp.web.application.administration.component;

import java.util.List;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.basicapp.core.business.user.service.IUserGroupService;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationUserGroupDescriptionPage;
import org.iglooproject.basicapp.web.application.common.renderer.ActionRenderers;
import org.iglooproject.basicapp.web.application.common.util.CssClassConstants;
import org.iglooproject.commons.util.functional.SerializablePredicate;
import org.iglooproject.wicket.more.link.model.ComponentPageModel;
import org.iglooproject.wicket.more.markup.html.action.AbstractOneParameterAjaxAction;
import org.iglooproject.wicket.more.markup.html.factory.AbstractDetachableFactory;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.model.BindingModel;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.model.ReadOnlyCollectionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserGroupPortfolioPanel extends Panel {

	private static final long serialVersionUID = -2237967697027843105L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserGroupPortfolioPanel.class);

	@SpringBean
	private IUserGroupService userGroupService;

	public UserGroupPortfolioPanel(String id, IModel<List<UserGroup>> userGroupListModel, int itemsPerPage) {
		super(id, userGroupListModel);
		
		add(
				DataTableBuilder.start(ReadOnlyCollectionModel.of(userGroupListModel, GenericEntityModel.factory()))
						.addLabelColumn(new ResourceModel("administration.usergroup.field.name"), Bindings.userGroup().name())
								.withLink(AdministrationUserGroupDescriptionPage.MAPPER_SOURCE.setParameter2(new ComponentPageModel(this)))
						.addLabelColumn(new ResourceModel("administration.usergroup.field.description"), Bindings.userGroup().description())
								.withClass(CssClassConstants.CELL_HIDDEN_SM_AND_LESS)
						.addActionColumn()
								.addConfirmAction(ActionRenderers.delete())
										.title(new AbstractDetachableFactory<IModel<UserGroup>, IModel<String>>() {
											private static final long serialVersionUID = 1L;
											@Override
											public IModel<String> create(IModel<UserGroup> parameter) {
												return new StringResourceModel(
														"administration.usergroup.delete.confirmation.title",
														BindingModel.of(parameter, Bindings.userGroup().name())
												);
											}
										})
										.content(new AbstractDetachableFactory<IModel<UserGroup>, IModel<String>>() {
											private static final long serialVersionUID = 1L;
											@Override
											public IModel<String> create(IModel<UserGroup> parameter) {
												return new StringResourceModel(
														"administration.usergroup.delete.confirmation.text",
														BindingModel.of(parameter, Bindings.userGroup().name())
												);
											}
										})
										.confirm()
										.onClick(new AbstractOneParameterAjaxAction<IModel<UserGroup>>() {
											private static final long serialVersionUID = 1L;
											@Override
											public void execute(AjaxRequestTarget target, IModel<UserGroup> parameter) {
												try {
													userGroupService.delete(parameter.getObject());
													Session.get().success(getString("common.success"));
													throw new RestartResponseException(getPage());
												} catch (RestartResponseException e) {
													throw e;
												} catch (Exception e) {
													LOGGER.error("Error on delete a user group.", e);
													getSession().error(getString("common.error.unexpected"));
													FeedbackUtils.refreshFeedback(target, getPage());
												}
											}
										})
										.when(new SerializablePredicate<UserGroup>() {
											private static final long serialVersionUID = 1L;
											@Override
											public boolean apply(UserGroup userGroup) {
												return BasicApplicationSession.get().hasRoleAdmin() && !userGroup.isLocked();
											}
										})
										.withClassOnElements(CssClassConstants.BTN_XS)
								.end()
						.withNoRecordsResourceKey("administration.usergroup.noGroups")
						.decorate()
								.count("administration.usergroup.count")
						.build("results", itemsPerPage)
		);
	}

}
