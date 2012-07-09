package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.staticmap;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.model.IModel;

public class StaticMapImage extends WebComponent {
	private static final long serialVersionUID = 5646388743355410600L;
	
	public StaticMapImage(String id, IModel<String> model) {
		super(id, model);
		setEscapeModelStrings(false);
	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		checkComponentTag(tag, "img");
		tag.put("src", getDefaultModelObjectAsString());
	}
}
