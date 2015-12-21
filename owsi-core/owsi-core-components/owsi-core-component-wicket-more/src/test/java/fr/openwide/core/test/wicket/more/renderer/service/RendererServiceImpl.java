package fr.openwide.core.test.wicket.more.renderer.service;

import fr.openwide.core.test.wicket.more.application.WicketMoreTestApplication;
import fr.openwide.core.wicket.more.rendering.service.AbstractRendererServiceImpl;

public class RendererServiceImpl extends AbstractRendererServiceImpl {
	
	@Override
	protected String getApplicationName() {
		return WicketMoreTestApplication.NAME;
	}

}
