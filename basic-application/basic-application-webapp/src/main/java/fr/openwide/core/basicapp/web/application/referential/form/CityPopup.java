package fr.openwide.core.basicapp.web.application.referential.form;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import fr.openwide.core.basicapp.core.business.referential.model.City;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.commons.util.functional.Predicates2;
import fr.openwide.core.wicket.more.markup.html.basic.ComponentBooleanProperty;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.DelegatedMarkupPanel;
import fr.openwide.core.wicket.more.model.BindingModel;

public abstract class CityPopup extends AbstractGenericListItemPopup<City> {

	private static final long serialVersionUID = 7104161263728328834L;

	public CityPopup(String id) {
		super(id);
	}

	@Override
	protected Component createBody(String wicketId) {
		DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, CityPopup.class);
		
		IModel<City> model = getModel();
		
		form = new Form<City>("form", model);
		
		TextField<String> libelle = new TextField<String>("label",BindingModel.of(model, Bindings.city().label()));
		body.add(
				form
						.add(
								libelle
										.setLabel(new ResourceModel("business.listItem.label"))
										.setRequired(true),
								new CheckBox("enabled", BindingModel.of(model, Bindings.city().enabled()))
										.setLabel(new ResourceModel("business.listItem.enabled"))
										.add(new EnclosureBehavior(ComponentBooleanProperty.ENABLE)
												.model(Predicates2.isTrue(), BindingModel.of(model, Bindings.city().disableable())))
						)
		);
		
		return body;
	}
}
