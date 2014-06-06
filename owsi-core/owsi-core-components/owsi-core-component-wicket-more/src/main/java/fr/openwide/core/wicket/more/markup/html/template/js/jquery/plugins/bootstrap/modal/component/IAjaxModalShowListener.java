package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component;

import org.apache.wicket.ajax.AjaxRequestTarget;

public interface IAjaxModalShowListener {
	
	void onShow(IAjaxModalPopupPanel modal, AjaxRequestTarget target);

}
