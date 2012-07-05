package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.util.value.IValueMap;

public class ModalHeaderFooterBehavior extends Behavior {

	private static final long serialVersionUID = -5202496937402360163L;

	private Component header;

	private Component footer;

	public ModalHeaderFooterBehavior(Component header, Component footer) {
		super();
		
		this.header = header;
		this.footer = footer;
	}

	@Override
	public void onComponentTag(Component component, ComponentTag tag) {
		super.onComponentTag(component, tag);
		
		IValueMap attributes = tag.getAttributes();
		attributes.put("data-header-selector", "#" + header.getMarkupId());
		attributes.put("data-footer-selector", "#" + footer.getMarkupId());
	}

}
