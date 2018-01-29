package org.iglooproject.basicapp.web.application.referencedata.form;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.more.business.referencedata.model.GenericReferenceData;
import org.iglooproject.jpa.more.business.referencedata.service.IGenericReferenceDataService;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.iglooproject.wicket.more.markup.html.form.FormPanelMode;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.component.AbstractAjaxModalPopupPanel;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.component.DelegatedMarkupPanel;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractGenericReferenceDataPopup<T extends GenericReferenceData<? super T, ?>> extends AbstractAjaxModalPopupPanel<T> {

	private static final long serialVersionUID = 8594171911880178857L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractGenericReferenceDataPopup.class);

	@SpringBean
	private IGenericReferenceDataService genericReferenceDataService;

	protected Form<T> form;

	protected final IModel<FormPanelMode> modeModel = new Model<>(FormPanelMode.ADD);

	public AbstractGenericReferenceDataPopup(String id) {
		super(id, new GenericEntityModel<Long, T>());
	}

	@Override
	protected Component createHeader(String wicketId) {
		return new CoreLabel(wicketId, new StringResourceModel("referenceData.${}.title", modeModel));
	}

	@Override
	abstract protected Component createBody(String wicketId);

	@Override
	protected Component createFooter(String wicketId) {
		DelegatedMarkupPanel footer = new DelegatedMarkupPanel(wicketId, AbstractGenericReferenceDataPopup.class);
		
		footer.add(new AjaxButton("save", form) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				T referenceData = AbstractGenericReferenceDataPopup.this.getModelObject();
				
				try {
					if (isAddMode()) {
						onSubmitAddMode(referenceData);
						Session.get().success(getString("referenceData.ADD.success"));
					} else {
						onSubmitEditMode(referenceData);
						Session.get().success(getString("referenceData.EDIT.success"));
					}
					closePopup(target);
					refresh(target);
				} catch (RestartResponseException e) { // NOSONAR
					throw e;
				} catch (Exception e) {
					if (isAddMode()) {
						LOGGER.error("Error adding a reference data.", e);
					} else {
						LOGGER.error("Error updating a reference data.", e);
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
	
	public void setUpAdd(T referenceData) {
		getModel().setObject(referenceData);
		modeModel.setObject(FormPanelMode.ADD);
	}
	
	public void setUpEdit(T referenceData) {
		getModel().setObject(referenceData);
		modeModel.setObject(FormPanelMode.EDIT);
	}
	
	protected void onSubmitAddMode(T referenceData) {
		genericReferenceDataService.create(referenceData);
	}
	
	protected void onSubmitEditMode(T referenceData) {
		genericReferenceDataService.update(referenceData);
	}
	
	protected boolean isAddMode() {
		return FormPanelMode.ADD.equals(modeModel.getObject());
	}
	
	protected abstract void refresh(AjaxRequestTarget target);
}
