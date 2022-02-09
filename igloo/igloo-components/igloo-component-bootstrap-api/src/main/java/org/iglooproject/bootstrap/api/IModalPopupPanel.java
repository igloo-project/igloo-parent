package org.iglooproject.bootstrap.api;

import org.apache.wicket.markup.html.WebMarkupContainer;

public interface IModalPopupPanel {

	String getContainerMarkupId();

	WebMarkupContainer getContainer();

	IBootstrapModal getBootstrapModal();

	void configure();

	boolean determineVisibility();

}
