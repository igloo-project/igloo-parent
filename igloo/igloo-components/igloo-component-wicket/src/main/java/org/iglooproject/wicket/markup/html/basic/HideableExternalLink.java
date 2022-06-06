package org.iglooproject.wicket.markup.html.basic;

import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.Strings;

public class HideableExternalLink extends ExternalLink {
	
	private static final long serialVersionUID = 1L;

	public HideableExternalLink(String id, IModel<String> href) {
		super(id, href);
	}

	public HideableExternalLink(String id, String label) {
		super(id, label);
	}
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		setVisible(!Strings.isEmpty(getDefaultModelObjectAsString()));
	}

}
