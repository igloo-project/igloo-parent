package fr.openwide.core.wicket.markup.html.basic;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.springframework.util.StringUtils;

public class HideableLabel extends Label {

	private static final long serialVersionUID = 1L;

	public HideableLabel(String id, IModel<?> model) {
		super(id, model);
	}

	public HideableLabel(String id, String label) {
		super(id, label);
	}

	@Override
	public boolean isVisible() {
		return StringUtils.hasText(getDefaultModelObjectAsString());
	}

}
