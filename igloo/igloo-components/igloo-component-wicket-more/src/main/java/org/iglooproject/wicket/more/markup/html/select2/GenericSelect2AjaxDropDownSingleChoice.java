package org.iglooproject.wicket.more.markup.html.select2;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.markup.html.select2.util.IDropDownChoiceWidth;
import org.iglooproject.wicket.more.markup.html.select2.util.Select2Utils;
import org.wicketstuff.select2.ChoiceProvider;
import org.wicketstuff.select2.Select2Choice;
import org.wicketstuff.select2.Settings;

public class GenericSelect2AjaxDropDownSingleChoice<T> extends Select2Choice<T> {

	private static final long serialVersionUID = 6355575209286187233L;

	protected GenericSelect2AjaxDropDownSingleChoice(String id, IModel<T> model, ChoiceProvider<T> choiceProvider) {
		super(id, model, choiceProvider);
		
		fillSelect2Settings(getSettings());
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		if (isRequired()) {
			Select2Utils.setRequiredSettings(getSettings());
		} else {
			Select2Utils.setOptionalSettings(getSettings());
		}
	}

	protected void fillSelect2Settings(Settings settings) {
		Select2Utils.setDefaultAjaxSettings(settings);
	}

	public GenericSelect2AjaxDropDownSingleChoice<T> setWidth(IDropDownChoiceWidth width) {
		getSettings().setWidth(width.getWidth());
		return this;
	}

}
