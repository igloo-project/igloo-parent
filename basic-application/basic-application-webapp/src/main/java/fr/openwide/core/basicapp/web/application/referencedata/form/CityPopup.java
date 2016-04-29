package fr.openwide.core.basicapp.web.application.referencedata.form;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import fr.openwide.core.basicapp.core.business.common.model.PostalCode;
import fr.openwide.core.basicapp.core.business.referencedata.model.City;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.basicapp.web.application.referencedata.validator.CityUnicityFormValidator;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.DelegatedMarkupPanel;
import fr.openwide.core.wicket.more.model.BindingModel;

public abstract class CityPopup extends AbstractGenericListItemPopup<City> {

	private static final long serialVersionUID = -4941198698654382836L;

	private TextField<String> label;
	
	private CheckBox enabled;

	TextField<PostalCode> postalCode;
	
	public CityPopup(String id) {
		super(id);
	}

	@Override
	protected Component createBody(String wicketId) {
		DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, CityPopup.class);
		
		IModel<City> model = getModel();
		
		form = new Form<City>("form", model);
		
		Condition disableableCondition = Condition.isTrue(
				BindingModel.of(model, Bindings.genericListItem().disableable())
		);
		
		this.postalCode = new TextField<PostalCode>(
				"postalCode", BindingModel.of(model, Bindings.city().postalCode()), PostalCode.class);
		
		this.label = new TextField<String>(
				"label", BindingModel.of(model, Bindings.genericListItem().label())
		);
		
		this.enabled = new CheckBox(
				"enabled", BindingModel.of(model, Bindings.genericListItem().enabled())
		);
		
		body.add(
				form
						.add(
								label
										.setLabel(new ResourceModel("business.listItem.label"))
										.setRequired(true),
								postalCode
										.setLabel(new ResourceModel("business.postalCode"))
										.setRequired(true),
								enabled
										.setLabel(new ResourceModel("business.listItem.enabled"))
										.add(disableableCondition.thenEnable())
						)
						.add(new CityUnicityFormValidator(getModel(), label, postalCode))
		);
		
		return body;
	}
	
	protected final TextField<String> getLabel() {
		return label;
	}
	
	protected final CheckBox getEnabled() {
		return enabled;
	}
}
