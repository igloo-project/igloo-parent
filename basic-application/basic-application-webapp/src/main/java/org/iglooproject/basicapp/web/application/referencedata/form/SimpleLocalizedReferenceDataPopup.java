package org.iglooproject.basicapp.web.application.referencedata.form;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.basicapp.core.business.referencedata.model.LocalizedReferenceData;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.component.DelegatedMarkupPanel;
import org.iglooproject.wicket.more.model.BindingModel;

public abstract class SimpleLocalizedReferenceDataPopup<E extends LocalizedReferenceData<? super E>> extends AbstractGenericReferenceDataPopup<E> {

	private static final long serialVersionUID = -729754666642757497L;

	private TextField<String> labelFr;
	private TextField<String> labelEn;
	private CheckBox enabled;

	public SimpleLocalizedReferenceDataPopup(String id) {
		super(id);
	}

	@Override
	protected Component createBody(String wicketId) {
		DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, SimpleLocalizedReferenceDataPopup.class);
		
		IModel<E> model = getModel();
		
		form = new Form<E>("form", model);
		
		
		Condition disableableCondition = Condition.isTrue(
				BindingModel.of(model, Bindings.localizedReferenceData().disableable())
		);
		
		this.labelFr = new TextField<>("labelFr", BindingModel.of(model, Bindings.localizedReferenceData().label().fr()), String.class);
		this.labelEn = new TextField<>("labelEn", BindingModel.of(model, Bindings.localizedReferenceData().label().en()), String.class);
		this.enabled = new CheckBox("enabled", BindingModel.of(model, Bindings.localizedReferenceData().enabled()));
		
		body.add(
				form
						.add(
								labelFr
										.setLabel(new ResourceModel("business.localizedReferenceData.label.fr"))
										.setRequired(true),
								labelEn
										.setLabel(new ResourceModel("business.localizedReferenceData.label.en"))
										.setRequired(true),
								enabled
										.setLabel(new ResourceModel("business.referenceData.enabled"))
										.add(disableableCondition.thenEnable())
						)
		);
		
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

}
