package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.statement.BootstrapModal;

/**
 * @deprecated Use Bootstrap 4 CSS et JS from now on.
 */
@Deprecated
public interface IModalPopupPanel {

	String getContainerMarkupId();

	WebMarkupContainer getContainer();

	BootstrapModal getBootstrapModal();
	
	void configure();
	
	boolean determineVisibility();

}
