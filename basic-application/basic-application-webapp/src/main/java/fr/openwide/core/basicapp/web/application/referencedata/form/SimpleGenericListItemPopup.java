package fr.openwide.core.basicapp.web.application.referencedata.form;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.commons.util.functional.Predicates2;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.wicket.more.markup.html.basic.ComponentBooleanProperty;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.DelegatedMarkupPanel;
import fr.openwide.core.wicket.more.model.BindingModel;

public abstract class SimpleGenericListItemPopup<T extends GenericListItem<T>> extends AbstractGenericListItemPopup<T> {

	private static final long serialVersionUID = 7104161263728328834L;

	public SimpleGenericListItemPopup(String id) {
		super(id);
	}

	@Override
	protected Component createBody(String wicketId) {
		DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, SimpleGenericListItemPopup.class);
		
		IModel<T> model = getModel();
		
		form = new Form<T>("form", model);
		
		TextField<String> libelle = new TextField<String>("label",BindingModel.of(model, Bindings.<T>genericListItem().label()));
		body.add(
				form
						.add(
								libelle
										.setLabel(new ResourceModel("business.listItem.label"))
										.setRequired(true),
								new CheckBox("enabled", BindingModel.of(model, Bindings.<T>genericListItem().enabled()))
										.setLabel(new ResourceModel("business.listItem.enabled"))
										.add(new EnclosureBehavior(ComponentBooleanProperty.ENABLE)
												.model(Predicates2.isTrue(), BindingModel.of(model, Bindings.<T>genericListItem().disableable())))
						)
		);
		
		return body;
	}
}
