package fr.openwide.core.wicket.markup.html.model;

import org.apache.wicket.Component;
import org.apache.wicket.model.ComponentModel;
import org.apache.wicket.model.IModel;

/**
 * Give translation for a given enum. Reosurce key is built as in EnumLabel.
 * 
 * This model needs to be wrapped by component to be used.
 */
public class EnumLabelModel<E extends Enum<?>> extends ComponentModel<String> {

	private static final long serialVersionUID = -7290192562513833670L;

	private IModel<E> enumValueModel;

	private IModel<String> nullKeyModel;

	public EnumLabelModel(IModel<E> enumValueModel) {
		this(enumValueModel, null);
	}

	public EnumLabelModel(IModel<E> enumValueModel, IModel<String> nullKeyModel) {
		super();
		this.enumValueModel = enumValueModel;
		this.nullKeyModel = nullKeyModel;
	}
	
	protected String getObject(Component component) {
		if (enumValueModel != null && enumValueModel.getObject() != null) {
			return component.getString(enumValueModel.getObject().getClass().getSimpleName() + "." + enumValueModel.getObject().name());
		} else if (nullKeyModel != null && nullKeyModel.getObject() != null) {
			return component.getString(nullKeyModel.getObject());
		} else {
			return "";
		}
	}

}
