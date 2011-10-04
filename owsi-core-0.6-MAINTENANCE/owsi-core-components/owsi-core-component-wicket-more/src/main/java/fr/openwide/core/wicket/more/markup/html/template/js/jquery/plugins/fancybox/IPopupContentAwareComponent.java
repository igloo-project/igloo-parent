package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox;

import org.apache.wicket.ajax.AjaxRequestTarget;

public interface IPopupContentAwareComponent {

	void show(AjaxRequestTarget target);

	void hide(AjaxRequestTarget target);

}
