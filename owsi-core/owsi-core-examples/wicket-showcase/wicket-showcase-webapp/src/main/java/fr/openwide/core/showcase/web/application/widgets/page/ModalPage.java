package fr.openwide.core.showcase.web.application.widgets.page;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.odlabs.wiquery.core.events.MouseEvent;

import fr.openwide.core.showcase.web.application.widgets.component.AddUserPopupPanel;
import fr.openwide.core.showcase.web.application.widgets.component.PopoverTooltipModalPopupPanel;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.component.AjaxConfirmButton;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.component.AjaxConfirmLink;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.component.ConfirmLink;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.behavior.AjaxModalOpenBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.AbstractModalPopupPanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.statement.BootstrapModal;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.statement.BootstrapModalBackdrop;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class ModalPage extends WidgetsTemplate {
	private static final long serialVersionUID = -4802009584951257187L;

	public ModalPage(PageParameters parameters) {
		super(parameters);
		BootstrapModal options = BootstrapModal.modal();
		options.setBackdrop(BootstrapModalBackdrop.NORMAL);
		options.setModalOverflow(false);
		options.setFocusOnFirstNotHiddenInput();
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("widgets.menu.modal"), ModalPage.class));
		
		AddUserPopupPanel addUserPopupPanel = new AddUserPopupPanel("addUserPopupPanel");
		add(addUserPopupPanel);
		
		Button addUserBtn = new Button("addUserBtn");
		addUserBtn.add(new AjaxModalOpenBehavior(addUserPopupPanel, MouseEvent.CLICK));
		add(addUserBtn);
		
		Button addUserBtnDisabled = new Button("addUserBtnDisabled");
		addUserBtnDisabled.setEnabled(false);
		addUserBtnDisabled.add(new AjaxModalOpenBehavior(addUserPopupPanel, MouseEvent.CLICK));
		add(addUserBtnDisabled);
		
		// static modal
		AbstractModalPopupPanel<Void> staticBootstrapModal = new AbstractModalPopupPanel<Void>("staticBootstrapModal", null) {
			private static final long serialVersionUID = 1L;

			@Override
			protected Component createHeader(String wicketId) {
				return new Label(wicketId, new ResourceModel("widgets.modal.staticBootstrapModal.header"));
			}
			
			@Override
			protected Component createBody(String wicketId) {
				return new Label(wicketId, new ResourceModel("widgets.modal.staticBootstrapModal.body"));
			}
			
			@Override
			protected Component createFooter(String wicketId) {
				return new Label(wicketId, new ResourceModel("widgets.modal.staticBootstrapModal.footer"));
			}

			@Override
			protected IModel<String> getCssClassNamesModel() {
				return Model.of("static");
			}
		};
		staticBootstrapModal.setBootstrapModal(options);
		WebMarkupContainer staticBootstrapModalOpen = new WebMarkupContainer("staticBootstrapModalOpen");
		staticBootstrapModal.prepareLink(staticBootstrapModalOpen);
		add(staticBootstrapModal);
		add(staticBootstrapModalOpen);
		
		ConfirmLink<Void> confirmLink = new ConfirmLink<Void>("confirmLink", null,
				new ResourceModel("widgets.modal.confirmLink.header"),
				new ResourceModel("widgets.modal.confirmLink.body"),
				new ResourceModel("widgets.modal.confirmLink.yes"),
				new ResourceModel("widgets.modal.confirmLink.no"),
				null, false) {
			private static final long serialVersionUID = 3980878234185635872L;

			@Override
			public void onClick() {
				getSession().success(getString("widgets.modal.confirmLink.success"));
			}
		};
		add(confirmLink);
		
		AjaxConfirmLink<Void> ajaxConfirmLink = new AjaxConfirmLink<Void>("ajaxConfirmLink", null,
				new ResourceModel("widgets.modal.ajaxConfirmLink.header"),
				new ResourceModel("widgets.modal.ajaxConfirmLink.body"),
				new ResourceModel("widgets.modal.ajaxConfirmLink.yes"),
				new ResourceModel("widgets.modal.ajaxConfirmLink.no"),
				null, false) {
			private static final long serialVersionUID = 3980878234185635872L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				getSession().success(getString("widgets.modal.ajaxConfirmLink.success"));
				FeedbackUtils.refreshFeedback(target, getPage());
			}
		};
		add(ajaxConfirmLink);
		
		AjaxConfirmLink<Void> ajaxConfirmLinkDisabled = new AjaxConfirmLink<Void>("ajaxConfirmLinkDisabled", null,
				new ResourceModel("widgets.modal.ajaxConfirmLink.header"),
				new ResourceModel("widgets.modal.ajaxConfirmLink.body"),
				new ResourceModel("widgets.modal.ajaxConfirmLink.yes"),
				new ResourceModel("widgets.modal.ajaxConfirmLink.no"),
				null, false) {
			private static final long serialVersionUID = 3980878234185635872L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				getSession().success(getString("widgets.modal.ajaxConfirmLink.success"));
				FeedbackUtils.refreshFeedback(target, getPage());
			}
		};
		ajaxConfirmLinkDisabled.setEnabled(false);
		add(ajaxConfirmLinkDisabled);
		
		Form<?> form = new Form<Void>("form");
		AjaxConfirmButton ajaxConfirmButton = new AjaxConfirmButton("ajaxConfirmButton",
				new ResourceModel("widgets.modal.ajaxConfirmButton.header"),
				new ResourceModel("widgets.modal.ajaxConfirmButton.body"),
				new ResourceModel("widgets.modal.ajaxConfirmButton.yes"),
				new ResourceModel("widgets.modal.ajaxConfirmButton.no"),
				null, false, null) {
			private static final long serialVersionUID = -914995462538909927L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				getSession().success(getString("widgets.modal.ajaxConfirmButton.success"));
				FeedbackUtils.refreshFeedback(target, getPage());
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}
		};
		form.add(ajaxConfirmButton);
		add(form);

		
		Form<?> formDisabled = new Form<Void>("formDisabled");
		formDisabled.setEnabled(false);
		AjaxConfirmButton ajaxConfirmButtonDisabled = new AjaxConfirmButton("ajaxConfirmButtonDisabled",
				new ResourceModel("widgets.modal.ajaxConfirmButton.header"),
				new ResourceModel("widgets.modal.ajaxConfirmButton.body"),
				new ResourceModel("widgets.modal.ajaxConfirmButton.yes"),
				new ResourceModel("widgets.modal.ajaxConfirmButton.no"),
				null, false, null) {
			private static final long serialVersionUID = -914995462538909927L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				getSession().success(getString("widgets.modal.ajaxConfirmButton.success"));
				FeedbackUtils.refreshFeedback(target, getPage());
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}
		};
		formDisabled.add(ajaxConfirmButtonDisabled);
		add(formDisabled);
		
		PopoverTooltipModalPopupPanel popoverTooltipModalPopupPanel = new PopoverTooltipModalPopupPanel("popoverTooltipModalPopupPanel", null);
		WebMarkupContainer popoverTooltipModalOpen = new WebMarkupContainer("popoverTooltipModalOpen");
		popoverTooltipModalPopupPanel.setBootstrapModal(options);
		popoverTooltipModalPopupPanel.prepareLink(popoverTooltipModalOpen);
		add(popoverTooltipModalOpen);
		add(popoverTooltipModalPopupPanel);
	}
	
	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return ModalPage.class;
	}
}
