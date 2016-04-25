package fr.openwide.core.basicapp.web.application.administration.form;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.basicapp.core.business.authority.BasicApplicationAuthorityUtils;
import fr.openwide.core.basicapp.core.business.user.model.UserGroup;
import fr.openwide.core.basicapp.core.business.user.service.IUserGroupService;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.basicapp.web.application.administration.page.AdministrationUserGroupDescriptionPage;
import fr.openwide.core.commons.util.functional.Suppliers2;
import fr.openwide.core.jpa.security.business.authority.model.Authority;
import fr.openwide.core.wicket.markup.html.form.CheckGroup;
import fr.openwide.core.wicket.more.link.model.PageModel;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;
import fr.openwide.core.wicket.more.markup.html.form.FormPanelMode;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.AbstractAjaxModalPopupPanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.DelegatedMarkupPanel;
import fr.openwide.core.wicket.more.markup.repeater.collection.CollectionView;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

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
												new CollectionView<Authority>(
														"authorities",
														Model.ofList(authorityUtils.getPublicAuthorities()),
														GenericEntityModel.<Authority>factory()
												) {
													private static final long serialVersionUID = 1L;
													@Override
													protected void populateItem(Item<Authority> item) {
														item.add(
																new Check<Authority>("authorityCheck", item.getModel())
																		.setLabel(new ResourceModel("administration.usergroup.authority." + item.getModelObject().getName()))
														);
													}
												}
										)
						)
		);
		
//		
//		ListView<Authority> authoritiesListView = new ListView<Authority>("authorities",
//				Model.ofList(authorityUtils.getPublicAuthorities())) {
//			private static final long serialVersionUID = -7557232825932251026L;
//			
//			@Override
//			protected void populateItem(ListItem<Authority> item) {
//				Authority authority = item.getModelObject();
//				
//				Check<Authority> authorityCheck = new Check<Authority>("authorityCheck",
//						new GenericEntityModel<Long, Authority>(authority));
//				
//				authorityCheck.setLabel();
//				
//				authorityCheckGroup.add(authorityCheck);
//				item.add(authorityCheck);
//			}
//		};
//		authorityCheckGroup.add(authoritiesListView);
		
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
