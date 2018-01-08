package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap4.modal.component;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap4.modal.statement.BootstrapModal;

public interface IModalPopupPanel {

	String getContainerMarkupId();

	WebMarkupContainer getContainer();

	BootstrapModal getBootstrapModal();
	
	void configure();
	
	boolean determineVisibility();

}
