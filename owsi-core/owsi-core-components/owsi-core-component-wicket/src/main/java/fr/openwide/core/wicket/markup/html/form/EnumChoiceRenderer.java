package fr.openwide.core.wicket.markup.html.form;

import org.apache.wicket.Application;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

public class EnumChoiceRenderer<T extends Enum<T>> implements IChoiceRenderer<T> {

	private static final long serialVersionUID = 1943832999125151167L;
	
	private String rootLocalizationKey;
	
	public EnumChoiceRenderer(String rootLocalizationKey) {
		this.rootLocalizationKey = rootLocalizationKey;
	}

	@Override
	public Object getDisplayValue(T enumValue) {
		return Application.get().getResourceSettings().getLocalizer().getString(rootLocalizationKey + enumValue.name(), null);
	}

	@Override
	public String getIdValue(T enumValue, int index) {
		return enumValue.name();
	}
	
}
