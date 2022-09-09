package org.iglooproject.basicapp.web.application.administration.form;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.authority.BasicApplicationAuthorityUtils;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.basicapp.core.business.user.service.IUserGroupService;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.administration.model.RoleDataProvider;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationUserGroupDetailPage;
import org.iglooproject.basicapp.web.application.common.renderer.AuthorityRenderer;
import org.iglooproject.functional.Suppliers2;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.wicket.more.link.model.PageModel;
import org.iglooproject.wicket.more.markup.html.form.FormMode;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.repeater.sequence.SequenceView;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import igloo.bootstrap.modal.AbstractAjaxModalPopupPanel;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.condition.Condition;
import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.markup.html.form.CheckGroup;
import igloo.wicket.markup.html.panel.DelegatedMarkupPanel;
import igloo.wicket.model.BindingModel;

public class UserGroupPopup extends AbstractAjaxModalPopupPanel<UserGroup> {

	private static final long serialVersionUID = 5369095796078187845L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserGroupPopup.class);

	@SpringBean
	private IUserGroupService userGroupService;

	@SpringBean
	private BasicApplicationAuthorityUtils authorityUtils;

	private final IModel<FormMode> formModeModel = new Model<>(FormMode.ADD);

	private Form<UserGroup> form;

	public UserGroupPopup(String id) {
		super(id, new GenericEntityModel<Long, UserGroup>(new UserGroup()));
	}

	@Override
	protected Component createHeader(String wicketId) {
		return new CoreLabel(
			wicketId,
			addModeCondition()
				.then(new ResourceModel("administration.userGroup.action.add.title"))
				.otherwise(new StringResourceModel("administration.userGroup.action.edit.title", getModel()))
		);
	}

	@Override
	protected Component createBody(String wicketId) {
		DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, UserGroupPopup.class);
		
		form = new Form<>("form", getModel());
		body.add(form);
		
		form
			.add(
				new RequiredTextField<String>("name", BindingModel.of(getModel(), Bindings.userGroup().name()))
					.setLabel(new ResourceModel("business.userGroup.name")),
				new TextArea<String>("description", BindingModel.of(getModel(), Bindings.userGroup().description()))
					.setLabel(new ResourceModel("business.userGroup.description")),
				new CheckGroup<Authority>("authorities",
					BindingModel.of(getModel(), Bindings.userGroup().authorities()),
					Suppliers2.<Authority>hashSet()
				)
					.add(
						new SequenceView<Authority>("authorities", new RoleDataProvider()) {
							private static final long serialVersionUID = 1L;
							@Override
							protected void populateItem(Item<Authority> item) {
								item.add(
									new Check<Authority>("authority", item.getModel())
										.setLabel(AuthorityRenderer.get().asModel(item.getModel()))
								);
							}
						}
					)
					.setRenderBodyOnly(false)
			);
		
		return body;
	}

	@Override
	protected Component createFooter(String wicketId) {
		DelegatedMarkupPanel footer = new DelegatedMarkupPanel(wicketId, UserGroupPopup.class);
		
		footer.add(
				new AjaxButton("save", form) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void onSubmit(AjaxRequestTarget target) {
						try {
							UserGroup userGroup = UserGroupPopup.this.getModelObject();
							
							if (addModeCondition().applies()) {
								userGroupService.create(userGroup);
								Session.get().success(getString("common.success"));
								throw AdministrationUserGroupDetailPage.linkDescriptor(UserGroupPopup.this.getModel(), PageModel.of(getPage()))
										.newRestartResponseException();
							} else {
								userGroupService.update(userGroup);
								Session.get().success(getString("common.success"));
							}
							closePopup(target);
							target.add(getPage());
						} catch (RestartResponseException e) { // NOSONAR
							throw e;
						} catch (Exception e) {
							if (addModeCondition().applies()) {
								LOGGER.error("Error occured while creating user group", e);
							} else {
								LOGGER.error("Error occured while updating user group", e);
							}
							Session.get().error(getString("common.error.unexpected"));
						}
						FeedbackUtils.refreshFeedback(target, getPage());
					}
					
					@Override
					protected void onError(AjaxRequestTarget target) {
						FeedbackUtils.refreshFeedback(target, getPage());
					}
				}
				.add(new CoreLabel(
					"label",
					addModeCondition()
						.then(new ResourceModel("common.action.create"))
						.otherwise(new ResourceModel("common.action.save"))
				))
		);
		
		BlankLink cancel = new BlankLink("cancel");
		addCancelBehavior(cancel);
		footer.add(cancel);
		
		return footer;
	}

	public void setUpAdd(UserGroup userGroup) {
		getModel().setObject(userGroup);
		formModeModel.setObject(FormMode.ADD);
	}

	public void setUpEdit(UserGroup userGroup) {
		getModel().setObject(userGroup);
		formModeModel.setObject(FormMode.EDIT);
	}

	protected Condition addModeCondition() {
		return FormMode.ADD.condition(formModeModel);
	}

	protected Condition editModeCondition() {
		return FormMode.EDIT.condition(formModeModel);
	}

}
