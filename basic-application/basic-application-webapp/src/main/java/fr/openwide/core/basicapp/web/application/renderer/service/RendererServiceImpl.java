package fr.openwide.core.basicapp.web.application.renderer.service;

import fr.openwide.core.basicapp.web.application.BasicApplicationApplication;
import fr.openwide.core.wicket.more.rendering.service.AbstractRendererServiceImpl;

public class RendererServiceImpl extends AbstractRendererServiceImpl {
	
	@Override
	protected String getApplicationName() {
		return BasicApplicationApplication.NAME;
	}

}
