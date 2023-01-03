package org.iglooproject.wicket.more.notification.service;

import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.iglooproject.jpa.security.service.IRunAsSystemService;
import org.springframework.beans.factory.annotation.Autowired;

import igloo.wicket.offline.IOfflineComponentProvider;

public abstract class AbstractWicketRendererServiceImpl extends AbstractOfflinePanelRendererServiceImpl {
	
	@Autowired
	private IRunAsSystemService securityService;

	public AbstractWicketRendererServiceImpl(IWicketContextProvider wicketContextProvider) {
		super(wicketContextProvider);
	}
	
	@Override
	protected String renderComponent(final IOfflineComponentProvider<? extends Component> offlineComponent, final Locale locale, final String variation) {
		return securityService.runAsSystem(
			() -> AbstractWicketRendererServiceImpl.super.renderComponent(offlineComponent, locale, variation)
		);
	}
	
	@Override
	protected String renderPage(final IOfflineComponentProvider<? extends Page> offlineComponent, final Locale locale, final String variation) {
		return securityService.runAsSystem(
			() -> AbstractWicketRendererServiceImpl.super.renderPage(offlineComponent, locale, variation)
		);
	}
}
