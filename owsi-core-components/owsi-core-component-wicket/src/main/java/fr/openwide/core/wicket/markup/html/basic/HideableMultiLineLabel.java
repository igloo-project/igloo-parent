package fr.openwide.core.wicket.markup.html.basic;

import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.model.IModel;
import org.springframework.util.StringUtils;

public class HideableMultiLineLabel extends MultiLineLabel {

	private static final long serialVersionUID = 1L;

	public HideableMultiLineLabel(String id, IModel<?> model) {
		super(id, model);
	}

	public HideableMultiLineLabel(String id, String label) {
		super(id, label);
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		setVisible(StringUtils.hasText(getDefaultModelObjectAsString()));
	}

}
