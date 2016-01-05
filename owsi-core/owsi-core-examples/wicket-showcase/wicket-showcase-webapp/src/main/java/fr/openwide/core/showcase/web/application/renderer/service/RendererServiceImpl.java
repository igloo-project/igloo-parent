package fr.openwide.core.showcase.web.application.renderer.service;

import fr.openwide.core.showcase.web.application.ShowcaseApplication;
import fr.openwide.core.wicket.more.rendering.service.AbstractRendererServiceImpl;

public class RendererServiceImpl extends AbstractRendererServiceImpl {
	
	@Override
	protected String getApplicationName() {
		return ShowcaseApplication.NAME;
	}

}
