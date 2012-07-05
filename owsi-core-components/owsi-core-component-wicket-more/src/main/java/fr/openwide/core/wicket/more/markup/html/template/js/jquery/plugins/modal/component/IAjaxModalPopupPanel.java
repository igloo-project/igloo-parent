package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.component;

import org.apache.wicket.ajax.AjaxRequestTarget;

public interface IAjaxModalPopupPanel extends IModalPopupPanel {

	void show(AjaxRequestTarget target);

}
