package basicapp.front.referencedata.popup;

import basicapp.back.business.common.model.PostalCode;
import basicapp.back.business.referencedata.controller.ICityControllerService;
import basicapp.back.business.referencedata.model.City;
import basicapp.back.util.binding.Bindings;
import basicapp.front.referencedata.validator.CityUnicityFormValidator;
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
import org.apache.wicket.spring.injection.annot.SpringBean;

public abstract class CityPopup extends AbstractReferenceDataPopup<City> {

  private static final long serialVersionUID = -4941198698654382836L;

  @SpringBean private ICityControllerService cityControllerService;

  public CityPopup(String id) {
    super(id);
  }

  @Override
  protected Component createBody(String wicketId) {
    DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, getClass());

    IModel<City> model = getModel();

    form = new Form<>("form", model);

    TextField<String> labelFr =
        new TextField<>("labelFr", BindingModel.of(model, Bindings.city().label().fr()));
    TextField<String> labelEn =
        new TextField<>("labelEn", BindingModel.of(model, Bindings.city().label().en()));
    TextField<PostalCode> postalCode =
        new TextField<>(
            "postalCode", BindingModel.of(model, Bindings.city().postalCode()), PostalCode.class);

    body.add(
        form.add(
                labelFr
                    .setLabel(new ResourceModel("business.referenceData.label.fr"))
                    .setRequired(true),
                labelEn
                    .setLabel(new ResourceModel("business.referenceData.label.en"))
                    .setRequired(true),
                postalCode
                    .setLabel(new ResourceModel("business.city.postalCode"))
                    .setRequired(true),
                new CheckBox("enabled", BindingModel.of(model, Bindings.city().enabled()))
                    .setLabel(new ResourceModel("business.referenceData.enabled"))
                    .add(
                        Condition.isTrue(BindingModel.of(model, Bindings.city().disableable()))
                            .thenEnable())
                    .setOutputMarkupId(true))
            .add(new CityUnicityFormValidator(getModel(), labelFr, postalCode)));

    return body;
  }

  @Override
  protected void onSubmit(City city, AjaxRequestTarget target) {
    cityControllerService.save(city);
  }
}
