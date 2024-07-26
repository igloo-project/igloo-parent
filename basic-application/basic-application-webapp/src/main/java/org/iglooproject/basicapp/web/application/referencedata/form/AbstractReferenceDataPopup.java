package org.iglooproject.basicapp.web.application.referencedata.form;

import igloo.bootstrap.modal.AbstractAjaxModalPopupPanel;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.condition.Condition;
import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.markup.html.panel.DelegatedMarkupPanel;
import igloo.wicket.model.Detachables;
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
import org.iglooproject.wicket.more.markup.html.form.FormMode;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractReferenceDataPopup<T extends GenericReferenceData<? super T, ?>>
    extends AbstractAjaxModalPopupPanel<T> {

  private static final long serialVersionUID = 8594171911880178857L;

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractReferenceDataPopup.class);

  @SpringBean private IGenericReferenceDataService genericReferenceDataService;

  protected Form<T> form;

  protected final IModel<FormMode> formModeModel = new Model<>(FormMode.ADD);

  public AbstractReferenceDataPopup(String id) {
    super(id, new GenericEntityModel<Long, T>());
  }

  @Override
  protected Component createHeader(String wicketId) {
    return new CoreLabel(
        wicketId, new StringResourceModel("referenceData.action.${}.title", formModeModel));
  }

  @Override
  protected abstract Component createBody(String wicketId);

  @Override
  protected Component createFooter(String wicketId) {
    DelegatedMarkupPanel footer = new DelegatedMarkupPanel(wicketId, getClass());

    footer.add(
        new AjaxButton("save", form) {
          private static final long serialVersionUID = 1L;

          @Override
          protected void onSubmit(AjaxRequestTarget target) {
            try {
              T referenceData = AbstractReferenceDataPopup.this.getModelObject();

              if (addModeCondition().applies()) {
                onSubmitAddMode(referenceData);
              } else {
                onSubmitEditMode(referenceData);
              }
              Session.get().success(getString("common.success"));
              closePopup(target);
              refresh(target);
            } catch (RestartResponseException e) { // NOSONAR
              throw e;
            } catch (Exception e) {
              if (addModeCondition().applies()) {
                LOGGER.error("Error adding a reference data.", e);
              } else {
                LOGGER.error("Error updating a reference data.", e);
              }
              Session.get().error(getString("common.error.unexpected"));
            }
            FeedbackUtils.refreshFeedback(target, getPage());
          }

          @Override
          protected void onError(AjaxRequestTarget target) {
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
    formModeModel.setObject(FormMode.ADD);
  }

  public void setUpEdit(T referenceData) {
    getModel().setObject(referenceData);
    formModeModel.setObject(FormMode.EDIT);
  }

  protected void onSubmitAddMode(T referenceData) {
    genericReferenceDataService.create(referenceData);
  }

  protected void onSubmitEditMode(T referenceData) {
    genericReferenceDataService.update(referenceData);
  }

  protected Condition addModeCondition() {
    return FormMode.ADD.condition(formModeModel);
  }

  protected Condition editModeCondition() {
    return FormMode.EDIT.condition(formModeModel);
  }

  protected abstract void refresh(AjaxRequestTarget target);

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(formModeModel);
  }
}
