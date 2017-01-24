package fr.openwide.core.wicket.more.hibernatevalidator.propertyresolver;

import org.apache.wicket.bean.validation.IPropertyResolver;
import org.apache.wicket.bean.validation.Property;
import org.apache.wicket.markup.html.form.FormComponent;

import fr.openwide.core.wicket.more.model.BindingModel;

public class BindingModelPropertyResolver implements IPropertyResolver {

	public static BindingModelPropertyResolver get() {
		return new BindingModelPropertyResolver();
	}

	private BindingModelPropertyResolver() {
	}

	@Override
	public Property resolveProperty(FormComponent<?> component) {
		if (!(component.getModel() instanceof BindingModel)) {
			return null;
		}
		
		BindingModel<?, ?> bindingModel = (BindingModel<?, ?>) component.getModel();
		
		if (bindingModel == null || bindingModel.getChainedModel() == null || bindingModel.getChainedModel().getObject() == null) {
			return null;
		}
		
		return new Property(bindingModel.getChainedModel().getObject().getClass(), bindingModel.getPropertyExpression());
	}

}
