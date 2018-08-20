package org.iglooproject.basicapp.web.application.referencedata.form;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.basicapp.core.business.common.model.PostalCode;
import org.iglooproject.basicapp.core.business.referencedata.model.City;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.referencedata.validator.CityUnicityFormValidator;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.component.DelegatedMarkupPanel;
import org.iglooproject.wicket.more.model.BindingModel;

public abstract class CityPopup extends AbstractGenericReferenceDataPopup<City> {

	private static final long serialVersionUID = -4941198698654382836L;

	public CityPopup(String id) {
		super(id);
	}

	@Override
	protected Component createBody(String wicketId) {
		DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, CityPopup.class);
		
		IModel<City> model = getModel();
		
		form = new Form<City>("form", model);
		
		Condition disableableCondition = Condition.isTrue(
				BindingModel.of(model, Bindings.city().disableable())
		);
		
		TextField<String> labelFr = new TextField<String>(
				"labelFr", BindingModel.of(model, Bindings.city().label().fr())
		);
		
		TextField<String> labelEn = new TextField<String>(
				"labelEn", BindingModel.of(model, Bindings.city().label().en())
		);
		
		TextField<PostalCode> postalCode = new TextField<PostalCode>(
				"postalCode", BindingModel.of(model, Bindings.city().postalCode()), PostalCode.class);
		
		body.add(
				form
						.add(
								labelFr
										.setLabel(new ResourceModel("business.localizedReferenceData.label.fr"))
										.setRequired(true),
								labelEn
										.setLabel(new ResourceModel("business.localizedReferenceData.label.en"))
										.setRequired(true),
								postalCode
										.setLabel(new ResourceModel("business.city.postalCode"))
										.setRequired(true),
								new CheckBox("enabled", BindingModel.of(model, Bindings.city().enabled()))
										.setLabel(new ResourceModel("business.referenceData.enabled"))
										.add(disableableCondition.thenEnable())
										.setOutputMarkupId(true)
						)
						.add(new CityUnicityFormValidator(getModel(), labelFr, postalCode))
		);
		
		return body;
	}

}
