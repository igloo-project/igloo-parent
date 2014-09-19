package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.util;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.util.io.IClusterable;

public abstract class AjaxResponseAction implements IClusterable {

	private static final long serialVersionUID = -5706493122591013088L;

	public abstract void execute(AjaxRequestTarget target);

	public void detach() {
		// Does nothing by default
	}

}
