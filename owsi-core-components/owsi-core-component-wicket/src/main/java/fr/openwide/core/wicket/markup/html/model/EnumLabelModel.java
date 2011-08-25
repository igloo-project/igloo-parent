package fr.openwide.core.wicket.markup.html.model;

import org.apache.wicket.Component;
import org.apache.wicket.model.ComponentModel;

/**
 * Give translation for a given enum. Reosurce key is built as in EnumLabel.
 * 
 * This model needs to be wrapped by component to be used.
 */
public class EnumLabelModel<E extends Enum<?>> extends ComponentModel<String> {

	private static final long serialVersionUID = -7290192562513833670L;

	private E enumValue;

	public EnumLabelModel(E enumValue) {
		super();
		this.enumValue = enumValue;
	}
	
	protected String getObject(Component component) {
		return component.getString(enumValue.getClass().getSimpleName() + "." + enumValue.name());
	}

}
