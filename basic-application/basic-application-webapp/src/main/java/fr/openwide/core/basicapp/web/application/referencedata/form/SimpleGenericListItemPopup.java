package fr.openwide.core.basicapp.web.application.referencedata.form;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.DelegatedMarkupPanel;
import fr.openwide.core.wicket.more.model.BindingModel;

public abstract class SimpleGenericListItemPopup<T extends GenericListItem<? super T>>
		extends AbstractGenericListItemPopup<T> {

	private static final long serialVersionUID = 7104161263728328834L;
	
	private TextField<String> label;
	
	private CheckBox enabled;

	public SimpleGenericListItemPopup(String id) {
		super(id);
	}

	@Override
	protected Component createBody(String wicketId) {
		DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, SimpleGenericListItemPopup.class);
		
		IModel<T> model = getModel();
		
		form = new Form<T>("form", model);
		
		Condition disableableCondition = Condition.isTrue(
				BindingModel.of(model, Bindings.genericListItem().disableable())
		);
		
		this.label = new TextField<>(
				"label", BindingModel.of(model, Bindings.genericListItem().label()), String.class
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
								enabled
										.setLabel(new ResourceModel("business.listItem.enabled"))
										.add(disableableCondition.thenEnable())
						)
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
