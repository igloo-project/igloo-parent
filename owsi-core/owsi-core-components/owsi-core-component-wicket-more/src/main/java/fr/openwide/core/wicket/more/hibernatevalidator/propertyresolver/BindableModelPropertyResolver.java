package fr.openwide.core.wicket.more.hibernatevalidator.propertyresolver;

import org.apache.wicket.bean.validation.IPropertyResolver;
import org.apache.wicket.bean.validation.Property;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.AbstractPropertyModel;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.bindable.model.BindableModel;
import fr.openwide.core.wicket.more.model.WorkingCopyModel;

public class BindableModelPropertyResolver implements IPropertyResolver {

	public static BindableModelPropertyResolver get() {
		return new BindableModelPropertyResolver();
	}

	private BindableModelPropertyResolver() {
	}

	@Override
	public Property resolveProperty(FormComponent<?> component) {
		BindableModel<?> bindableModel = getBindableModel(component);
		
		if (bindableModel == null) {
			return null;
		}
		
		IModel<?> model = bindableModel.getDelegateModel();
		
		if (model instanceof WorkingCopyModel) {
			model = ((WorkingCopyModel<?>) model).getReferenceModel();
		}
		
		
		if (model == null || !(model instanceof AbstractPropertyModel)) {
			return null;
		}
		
		AbstractPropertyModel<?> propertyModel = (AbstractPropertyModel<?>) model;
		
		return new Property(propertyModel.getChainedModel().getObject().getClass(), propertyModel.getPropertyExpression());
	}

	private BindableModel<?> getBindableModel(FormComponent<?> component) {
		IModel<?> model = component.getModel();
		
		if (model instanceof BindableModel) {
			return (BindableModel<?>) model;
		}
		
		return null;
	}

}
