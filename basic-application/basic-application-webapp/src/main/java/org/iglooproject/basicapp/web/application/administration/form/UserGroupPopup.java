package org.iglooproject.basicapp.web.application.administration.form;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.AbstractLink;
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
import org.iglooproject.basicapp.web.application.administration.page.AdministrationUserGroupDescriptionPage;
import org.iglooproject.basicapp.web.application.common.renderer.AuthorityRenderer;
import org.iglooproject.commons.util.functional.Suppliers2;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.wicket.markup.html.form.CheckGroup;
import org.iglooproject.wicket.more.link.model.PageModel;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.iglooproject.wicket.more.markup.html.form.FormPanelMode;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.component.AbstractAjaxModalPopupPanel;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.component.DelegatedMarkupPanel;
import org.iglooproject.wicket.more.markup.repeater.sequence.SequenceView;
import org.iglooproject.wicket.more.model.BindingModel;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserGroupPopup extends AbstractAjaxModalPopupPanel<UserGroup> {

	private static final long serialVersionUID = 5369095796078187845L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserGroupPopup.class);

	@SpringBean
	private IUserGroupService userGroupService;
	
	@SpringBean
	private BasicApplicationAuthorityUtils authorityUtils;

	private Form<UserGroup> userGroupForm;

	private FormPanelMode mode;

	public UserGroupPopup(String id, IModel<UserGroup> userGroupModel) {
		this(id, userGroupModel, FormPanelMode.EDIT);
	}

	public UserGroupPopup(String id) {
		this(id, new GenericEntityModel<Long, UserGroup>(new UserGroup()), FormPanelMode.ADD);
	}

	protected UserGroupPopup(String id, IModel<UserGroup> userGroupModel, FormPanelMode mode) {
		super(id, userGroupModel);
		
		this.mode = mode;
	}

	@Override
	protected Component createHeader(String wicketId) {
		if (isAddMode()) {
			return new Label(wicketId, new ResourceModel("administration.usergroup.add.title"));
		} else {
			return new Label(wicketId, new StringResourceModel("administration.usergroup.update.title").setModel(getModel()));
		}
	}

	@Override
	protected Component createBody(String wicketId) {
		DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, UserGroupPopup.class);
		
		userGroupForm = new Form<UserGroup>("form", getModel());
		body.add(
				userGroupForm
						.add(
								new RequiredTextField<String>("name", BindingModel.of(userGroupForm.getModel(), Bindings.userGroup().name()))
										.setLabel(new ResourceModel("administration.usergroup.field.name")),
								new TextArea<String>("description", BindingModel.of(userGroupForm.getModel(), Bindings.userGroup().description()))
										.setLabel(new ResourceModel("administration.usergroup.field.description")),
								new CheckGroup<Authority>("authoritiesGroup",
										BindingModel.of(userGroupForm.getModel(), Bindings.userGroup().authorities()),
										Suppliers2.<Authority>hashSet()
								)
										.add(
												new SequenceView<Authority>("authorities", new RoleDataProvider()) {
													private static final long serialVersionUID = 1L;
													@Override
													protected void populateItem(Item<Authority> item) {
														item.add(
																new Check<Authority>("authorityCheck", item.getModel())
																		.setLabel(AuthorityRenderer.get().asModel(item.getModel()))
														);
													}
												}
										)
						)
		);
		
		return body;
	}

	@Override
	protected Component createFooter(String wicketId) {
		DelegatedMarkupPanel footer = new DelegatedMarkupPanel(wicketId, UserGroupPopup.class);
		
		// Validate button
		AjaxButton validate = new AjaxButton("save", userGroupForm) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				UserGroup userGroup = UserGroupPopup.this.getModelObject();
				
				try {
					if (isAddMode()) {
						userGroupService.create(userGroup);
						Session.get().success(getString("administration.usergroup.add.success"));
						throw AdministrationUserGroupDescriptionPage.linkDescriptor(UserGroupPopup.this.getModel(), PageModel.of(getPage()))
								.newRestartResponseException();
					} else {
						userGroupService.update(userGroup);
						Session.get().success(getString("administration.usergroup.update.success"));
					}
					closePopup(target);
					target.add(getPage());
				} catch (RestartResponseException e) { // NOSONAR
					throw e;
				} catch (Exception e) {
					if (isAddMode()) {
						LOGGER.error("Error occured while creating user group", e);
					} else {
						LOGGER.error("Error occured while updating user group", e);
					}
					Session.get().error(getString("common.error.unexpected"));
				}
				FeedbackUtils.refreshFeedback(target, getPage());
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				FeedbackUtils.refreshFeedback(target, getPage());
			}
		};
		Label validateLabel;
		if (isAddMode()) {
			validateLabel = new Label("validateLabel", new ResourceModel("common.action.create"));
		} else {
			validateLabel = new Label("validateLabel", new ResourceModel("common.action.save"));
		}
		validate.add(validateLabel);
		footer.add(validate);
		
		// Cancel button
		AbstractLink cancel = new AbstractLink("cancel") {
			private static final long serialVersionUID = 1L;
		};
		addCancelBehavior(cancel);
		footer.add(cancel);
		
		return footer;
	}

	protected boolean isEditMode() {
		return FormPanelMode.EDIT.equals(mode);
	}

	protected boolean isAddMode() {
		return FormPanelMode.ADD.equals(mode);
	}

	@Override
	public IModel<String> getCssClassNamesModel() {
		return Model.of("modal-usergroup");
	}

	@Override
	protected void onShow(AjaxRequestTarget target) {
		super.onShow(target);
		if (isAddMode()) {
			getModel().setObject(new UserGroup());
		}
	}
}
