package org.iglooproject.basicapp.web.application.referencedata.form;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.more.business.generic.model.GenericListItem;
import org.iglooproject.jpa.more.business.generic.service.IGenericListItemService;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.iglooproject.wicket.more.markup.html.form.FormPanelMode;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.component.AbstractAjaxModalPopupPanel;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.component.DelegatedMarkupPanel;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractGenericListItemPopup<T extends GenericListItem<? super T>> extends AbstractAjaxModalPopupPanel<T> {

	private static final long serialVersionUID = 8594171911880178857L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractGenericListItemPopup.class);

	@SpringBean
	private IGenericListItemService genericListItemService;

	protected Form<T> form;

	protected final IModel<FormPanelMode> modeModel = new Model<>(FormPanelMode.ADD);

	public AbstractGenericListItemPopup(String id) {
		super(id, new GenericEntityModel<Long, T>());
	}

	@Override
	protected Component createHeader(String wicketId) {
		return new Label(wicketId, new StringResourceModel("listItem.${}.title", modeModel));
	}

	@Override
	abstract protected Component createBody(String wicketId);

	@Override
	protected Component createFooter(String wicketId) {
		DelegatedMarkupPanel footer = new DelegatedMarkupPanel(wicketId, AbstractGenericListItemPopup.class);
		
		// Validate button
		footer.add(new AjaxButton("save", form) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				T genericListItem = AbstractGenericListItemPopup.this.getModelObject();
				
				try {
					if (isAddMode()) {
						onSubmitAddMode(genericListItem);
						Session.get().success(getString("listItem.ADD.success"));
					} else {
						onSubmitEditMode(genericListItem);
						Session.get().success(getString("listItem.EDIT.success"));
					}
					closePopup(target);
					refresh(target);
				} catch (RestartResponseException e) { // NOSONAR
					throw e;
				} catch (Exception e) {
					if (isAddMode()) {
						LOGGER.error("Error adding a GenericListItem.", e);
					} else {
						LOGGER.error("Error updating a GenericListItem.", e);
					}
					Session.get().error(getString("common.error.unexpected"));
				}
				FeedbackUtils.refreshFeedback(target, getPage());
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				FeedbackUtils.refreshFeedback(target, getPage());
			}
		});
		
		BlankLink cancel = new BlankLink("cancel");
		addCancelBehavior(cancel);
		footer.add(cancel);
		
		return footer;
	}
	
	public void setUpAdd(T genericListItem) {
		getModel().setObject(genericListItem);
		modeModel.setObject(FormPanelMode.ADD);
	}
	
	public void setUpEdit(T genericListItem) {
		getModel().setObject(genericListItem);
		modeModel.setObject(FormPanelMode.EDIT);
	}
	
	protected void onSubmitAddMode(T genericListItem) {
		genericListItemService.create(genericListItem);
	}
	
	protected void onSubmitEditMode(T genericListItem) {
		genericListItemService.update(genericListItem);
	}
	
	protected boolean isAddMode() {
		return FormPanelMode.ADD.equals(modeModel.getObject());
	}
	
	protected abstract void refresh(AjaxRequestTarget target);
}
