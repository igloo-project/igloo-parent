package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component;

import org.apache.wicket.markup.html.WebMarkupContainer;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.statement.BootstrapModal;

public interface IModalPopupPanel {

	String getContainerMarkupId();

	WebMarkupContainer getContainer();

	BootstrapModal getBootstrapModal();
	
	boolean determineVisibility();

}
