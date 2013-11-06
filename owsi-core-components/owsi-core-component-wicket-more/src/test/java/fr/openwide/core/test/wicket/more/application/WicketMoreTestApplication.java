package fr.openwide.core.test.wicket.more.application;

import org.apache.wicket.Page;

import fr.openwide.core.wicket.more.application.CoreWicketApplication;

/**
 * Stub.
 */
public class WicketMoreTestApplication extends CoreWicketApplication {
	
	public WicketMoreTestApplication() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void mountApplicationPages() {
		// Rien à faire
	}

	@Override
	protected void mountApplicationResources() {
		// Rien à faire
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return Page.class;
	}

}
