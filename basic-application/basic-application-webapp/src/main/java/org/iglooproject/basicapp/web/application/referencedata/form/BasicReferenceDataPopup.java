package org.iglooproject.basicapp.web.application.referencedata.form;

import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.DelegatedMarkupPanel;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.basicapp.core.business.referencedata.model.ReferenceData;
import org.iglooproject.basicapp.core.util.binding.Bindings;

public class BasicReferenceDataPopup<E extends ReferenceData<? super E>>
    extends AbstractReferenceDataPopup<E> {

  private static final long serialVersionUID = -729754666642757497L;

  private TextField<String> labelFr;
  private TextField<String> labelEn;
  private CheckBox enabled;

  public BasicReferenceDataPopup(String id) {
    super(id);
  }

  @Override
  protected Component createBody(String wicketId) {
    DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, getClass());

    IModel<E> model = getModel();

    form = new Form<E>("form", model);
    body.add(form);

    this.labelFr =
        new TextField<>(
            "labelFr", BindingModel.of(model, Bindings.referenceData().label().fr()), String.class);
    this.labelEn =
        new TextField<>(
            "labelEn", BindingModel.of(model, Bindings.referenceData().label().en()), String.class);
    this.enabled =
        new CheckBox("enabled", BindingModel.of(model, Bindings.referenceData().enabled()));

    form.add(
        labelFr.setLabel(new ResourceModel("business.referenceData.label.fr")).setRequired(true),
        labelEn.setLabel(new ResourceModel("business.referenceData.label.en")).setRequired(true),
        enabled
            .setLabel(new ResourceModel("business.referenceData.enabled"))
            .add(
                Condition.isTrue(BindingModel.of(model, Bindings.referenceData().disableable()))
                    .thenEnable())
            .setOutputMarkupId(true));

    return body;
  }

  protected final TextField<String> getLabelFr() {
    return labelFr;
  }

  protected final TextField<String> getLabelEn() {
    return labelEn;
  }

  protected final CheckBox getEnabled() {
    return enabled;
  }

  @Override
  protected void refresh(AjaxRequestTarget target) {
    // nothing to do
  }
}
